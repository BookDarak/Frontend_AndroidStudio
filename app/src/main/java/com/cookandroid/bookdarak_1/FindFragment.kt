package com.cookandroid.bookdarak_1

import API.FindbookAPI
import adapter.BookFindAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil.setContentView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.bookdarak_1.databinding.ActivityMainBinding
import model.Book
import model.FindBookDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"





/**
 *
 * A simple [Fragment] subclass.
 * Use the [FindFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FindFragment : Fragment() {


    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var bookRecyclerViewAdapter: BookFindAdapter
    private lateinit var bookService: FindbookAPI



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        initBookService()

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private fun initBookService() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/v3/search/book?output=json") // 인터파크 베이스 주소;
            .addConverterFactory(GsonConverterFactory.create()) // Gson 변환기 사용;
            .build()

        bookService = retrofit.create(FindbookAPI::class.java)
    }

    fun bookServiceSearchBook(keyword: String) {

        bookService.Bookname(getString(R.string.apikey), keyword)
            .enqueue(object : Callback<FindBookDTO> {
                // 성공.

                override fun onResponse(
                    call: Call<FindBookDTO>,
                    response: Response<FindBookDTO>
                ) {


                    if (response.isSuccessful.not()) {
                        return
                    }

                    bookRecyclerViewAdapter.submitList(response.body()?.books.orEmpty()) // 새 리스트로 갱신
                }

                // 실패.
                override fun onFailure(call: Call<FindBookDTO>, t: Throwable) {
                    Log.e(M_TAG, t.toString())
                }
            })
    }








    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FindFragment.
         */
        // TODO: Rename and change types and number of parameters
        private const val M_TAG = "FindFragment"
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FindFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
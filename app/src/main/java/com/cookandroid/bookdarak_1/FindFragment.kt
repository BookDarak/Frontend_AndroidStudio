package com.cookandroid.bookdarak_1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cookandroid.bookdarak_1.data.api.FindBookAPI
import com.cookandroid.bookdarak_1.data.model.SearchResponse
import com.cookandroid.bookdarak_1.databinding.FragmentFindBinding
import com.cookandroid.bookdarak_1.ui.adapter.BookSearchViewHolder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


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




    companion object {

        private var userId: Int = -1
        private var param1: String? = null
        private var param2: String? = null
        private lateinit var binding: FragmentFindBinding
        private lateinit var bookRecyclerViewAdapter: BookSearchViewHolder
        private lateinit var bookService: FindBookAPI

        //private lateinit var historyAdapter: Find_HistoryAdapter

        fun newInstance(userId: Int): FindFragment {
            val fragment = FindFragment()
            val args = Bundle()
            args.putInt("USER_ID", userId)
            fragment.arguments = args
            return fragment
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("http://www.bookdarak.shop:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)  // 여기에 OkHttpClient 인스턴스를 추가합니다.
            .build()


        private val service: BookDarakApiService = retrofit.create(BookDarakApiService::class.java)



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


    }




    //private val db: FindBookDataBase by lazy {
    //    getAppDatabase(requireContext())
    //}





    //override fun onCreate(savedInstanceState: Bundle?) {
        //super.onCreate(savedInstanceState)

        //binding = FragmentFindBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        //initBookRecyclerView()
        //initHistoryRecyclerView()
        //initSearchEditText()

        //initBookService()
        //bookServiceLoadBestSellers()

          //arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
        //}
    //}
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

              userId = arguments?.getInt("USER_ID", -1) ?: -1
              //Log.d("FindFragment", "Fetched USER_ID: $userId")


              binding = FragmentFindBinding.inflate(inflater, container, false)




            val rootView = binding.root

            initBookRecyclerView()
            //initHistoryRecyclerView()
            initSearchEditText()

            initBookService()



            arguments?.let {
                param1 = it.getString(ARG_PARAM1)
                param2 = it.getString(ARG_PARAM2)
            }



        // Inflate the layout for this fragment
        return rootView
    }

    interface SearchClickListener {
        fun onSearchClicked(keyword: String)
    }

    private fun initBookService() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://dapi.kakao.com/") // 인터파크 베이스 주소;
            .addConverterFactory(GsonConverterFactory.create()) // Gson 변환기 사용;
            .build()

        bookService = retrofit.create(FindBookAPI::class.java)
    }

    private fun initBookRecyclerView() {
        bookRecyclerViewAdapter = BookSearchViewHolder(itemClickedListener = {
            val intent = Intent(requireContext(), BookinfoActivity::class.java)

            // 직렬화 해서 넘길 것.
            intent.putExtra("bookModel", it)
            intent.putExtra("USER_ID", userId)


            startActivity(intent)
        })



        binding.recyclerViewBooklist.layoutManager = LinearLayoutManager(requireContext() )
        binding.recyclerViewBooklist.adapter = bookRecyclerViewAdapter
    }



    fun bookServiceSearchBook(keyword: String) {

        bookService.FindBook(keyword)
            .enqueue(object : Callback<SearchResponse> {
                // 성공.

                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    //hideHistoryView()
                    //saveSearchKeyword(keyword)

                    if (response.isSuccessful.not()) {
                        return
                    }

                    bookRecyclerViewAdapter.submitList(response.body()?.documents.orEmpty()) // 새 리스트로 갱신
                }

                // 실패.
                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    //hideHistoryView()
                    Log.e(M_TAG, t.toString())
                }
            })

    }




    @SuppressLint("ClickableViewAccessibility")
    private fun initSearchEditText() {
        binding.edittextFind.setOnKeyListener { v, keyCode, event ->
            // 키보드 입력시 발생

            // 엔터 눌렀을 경우 (눌렀거나, 뗏을 때 -> 눌렀을 때 발생하도록.)
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                bookServiceSearchBook(binding.edittextFind.text.toString())
                return@setOnKeyListener true// 처리 되었음.

            }
            return@setOnKeyListener false // 처리 안됬음 을 나타냄.
        }

        binding.edittextFind.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                //showHistoryView()
            }
            return@setOnTouchListener false
        }
    }









}


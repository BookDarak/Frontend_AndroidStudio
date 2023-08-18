package com.cookandroid.bookdarak_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.cookandroid.bookdarak_1.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"




class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var userId: Int = -1

    companion object {
        fun newInstance(userId: Int): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putInt("USER_ID", userId)
            fragment.arguments = args
            return fragment
        }
    }
    // Retrofit 인스턴스 생성
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://www.bookdarak.shop:8080/") // 실제 API endpoint 주소로 변경
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val service: BookDarakApiService = retrofit.create(BookDarakApiService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        userId = arguments?.getInt("USER_ID", -1) ?: -1

        fetchRecommendationBooks()

        return binding.root
    }

    private fun fetchRecommendationBooks() {
        if (userId != -1) {
            service.getBookRecommendation(userId).enqueue(object : Callback<RecommendationResponse> {
                override fun onResponse(
                    call: Call<RecommendationResponse>,
                    response: Response<RecommendationResponse>
                ) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        updateUI(response.body()?.result)
                    } else {
                        Toast.makeText(context, "도서 추천을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
                    Toast.makeText(context, "오류: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(context, "사용자 ID를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(books: List<RecommendationResponse.Book>?) {
        books?.let {
            if (it.isNotEmpty()) {
                updateBookView(binding.imageRec11, binding.textRec11Title, binding.textRec11Author, it[0])
            }
            if (it.size > 1) {
                updateBookView(binding.imageRec12, binding.textRec12Title, binding.textRec12Author, it[1])
            }
            if (it.size > 2) {
                updateBookView(binding.imageRec13, binding.textRec13Title, binding.textRec13Author, it[2])
            }
        }
    }

    private fun updateBookView(imageView: ImageView, titleView: TextView, authorView: TextView, book: RecommendationResponse.Book) {
        Glide.with(this)
            .load(book.coverImage)
            .into(imageView)
        titleView.text = book.title
        authorView.text = book.author
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
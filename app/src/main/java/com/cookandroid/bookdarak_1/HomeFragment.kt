
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://www.bookdarak.shop:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: BookDarakApiService = retrofit.create(BookDarakApiService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        userId = arguments?.getInt("USER_ID", -1) ?: -1

        fetchAllData()

        return binding.root
    }

    private fun fetchAllData() {
        if (userId != -1) {
            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) { fetchRecommendationBooks() }
                withContext(Dispatchers.IO) { fetchGenderBasedRecommendationBooks() }
                withContext(Dispatchers.IO) { fetchUserInfo() }
                withContext(Dispatchers.IO) { fetchQuote() }
            }
        } else {
            Toast.makeText(context, "사용자 ID를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun fetchRecommendationBooks() {
        try {
            val response = service.getBookRecommendation(userId).execute()
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                withContext(Dispatchers.Main) {
                    updateUI(response.body()?.result)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "도서 추천을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun fetchGenderBasedRecommendationBooks() {
        try {
            val response = service.getGenderBasedRecommendation(userId).execute()
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                withContext(Dispatchers.Main) {
                    updateGenderBasedUI(response.body()?.result)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "성별 기반 도서 추천을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun fetchUserInfo() {
        try {
            val response = service.getUserInfo(userId).execute()
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                withContext(Dispatchers.Main) {
                    val userName = response.body()?.result?.name
                    binding.nickname.text = userName
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "유저 정보를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateBookView(imageView: ImageView, titleView: TextView, authorView: TextView, book: RecommendationResponse.Book) {
        Glide.with(this)
            .load(book.imgUrl)
            .into(imageView)
        titleView.text = book.name
        authorView.text = book.author
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

    private fun updateGenderBasedUI(books: List<RecommendationResponse.Book>?) {
        books?.let {
            if (it.isNotEmpty()) {
                updateBookView(binding.imageRec21, binding.textRec21Title, binding.textRec21Author, it[0])
            }
            if (it.size > 1) {
                updateBookView(binding.imageRec22, binding.textRec22Title, binding.textRec22Author, it[1])
            }
            if (it.size > 2) {
                updateBookView(binding.imageRec23, binding.textRec23Title, binding.textRec23Author, it[2])
            }
        }
    }
    private suspend fun fetchQuote() {
        try {
            val response = service.getQuote().execute()
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                withContext(Dispatchers.Main) {
                    updateQuoteUI(response.body()?.result)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "명언을 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun updateQuoteUI(quote: QuoteResponse.Quote?) {
        quote?.let {
            binding.quoteText.text = it.line
            binding.quoteSpeaker.text = it.speaker
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
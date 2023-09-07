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
import com.cookandroid.bookdarak_1.data.api.FindBookAPI
import com.cookandroid.bookdarak_1.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var userId: Int = -1
    private var userAgeGroup: String = ""


    companion object {
        fun newInstance(userId: Int): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putInt("USER_ID", userId)
            fragment.arguments = args

            return fragment
        }
        private lateinit var bookService: FindBookAPI
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
                withContext(Dispatchers.IO) { fetchUserDay() }
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
                    val userResult = response.body()?.result
                    val userName = userResult?.name
                    binding.nickname.text = userName

                    val age = userResult?.age
                    userAgeGroup = when {
                        age in 10..19 -> "10대"
                        age in 20..29 -> "20대"
                        age in 30..39 -> "30대"
                        age in 40..49 -> "40대"
                        else -> "기타"
                    }

                    val userGender = userResult?.gender ?: ""
                    if (userGender == "F") {
                        binding.textRec2.text = "여성 추천도서"
                    } else if (userGender == "M") {
                        binding.textRec2.text = "남성 추천도서"
                    } else {
                        binding.textRec2.text = "추천도서"
                    }

                    // 연령대를 기반으로 추천도서의 타이틀 수정
                    binding.textRec1.text = "$userAgeGroup 추천도서"
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

                /*
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://dapi.kakao.com/") // 인터파크 베이스 주소;
                        .addConverterFactory(GsonConverterFactory.create()) // Gson 변환기 사용;
                        .build()

                    bookService = retrofit.create(FindBookAPI::class.java)


                private fun initBookRecyclerView() {
                    FindFragment.bookRecyclerViewAdapter = BookSearchViewHolder(itemClickedListener = {
                        val intent = Intent(requireContext(), BookinfoActivity::class.java)

                        // 직렬화 해서 넘길 것.
                        intent.putExtra("bookModel", it)
                        intent.putExtra("USER_ID", FindFragment.userId)




                        startActivity(intent)
                    })



                    FindFragment.binding.recyclerViewBooklist.layoutManager = LinearLayoutManager(requireContext() )
                    FindFragment.binding.recyclerViewBooklist.adapter =
                        FindFragment.bookRecyclerViewAdapter
                }

                 */

            }
            if (it.size > 1) {
                updateBookView(binding.imageRec22, binding.textRec22Title, binding.textRec22Author, it[1])
            }
            if (it.size > 2) {
                updateBookView(binding.imageRec23, binding.textRec23Title, binding.textRec23Author, it[2])
            }
        }
/*
        // Inside HomeFragment, set click listeners for book cover images
        binding.imageRec11.setOnClickListener {
            navigateToBookDetail(bookList[0]) // Pass the book information to the detail page
        }

        binding.imageRec12.setOnClickListener {
            navigateToBookDetail(bookList[1])
        }

        binding.imageRec13.setOnClickListener {
            navigateToBookDetail(bookList[2])
        }

        // Create a function to navigate to the BookDetailFragment
        fun navigateToBookDetail(book: RecommendationResponse.Book) {
            val intent = Intent(requireContext(), BookinfoActivity::class.java)
            intent.putExtra("bookModel", book)
            startActivity(intent)
        }

 */

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
    private suspend fun fetchUserDay() {
        try {
            val response = service.getUserDay(userId).execute()
            if (response.isSuccessful && response.body()?.isSuccess == true) {
                withContext(Dispatchers.Main) {
                    val userDay = response.body()?.result
                    binding.userDayTextView.text = "$userDay 일차"
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "사용자 일자를 불러오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "오류: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.cookandroid.bookdarak_1

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import adapter.BookAdapter
import android.util.Log
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class CalendarFragment : Fragment() {

    lateinit var compactcalendar_view: CompactCalendarView
    lateinit var diaryTextView: TextView
    lateinit var title: TextView
    lateinit var bookRecyclerView: RecyclerView
    var userId: Int = -1

    companion object {
        fun newInstance(userId: Int): CalendarFragment {
            val fragment = CalendarFragment()
            val args = Bundle()
            args.putInt("USER_ID", userId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        compactcalendar_view = view.findViewById(R.id.compactcalendar_view)
        diaryTextView = view.findViewById(R.id.diaryTextView)
        title = view.findViewById(R.id.title)
        bookRecyclerView = view.findViewById(R.id.bookRecyclerView)
        userId = arguments?.getInt("USER_ID", -1) ?: -1
        Log.d("CalendarFragment", "Fetched USER_ID: $userId")

        // 현재의 달을 제목에 표시합니다.
        updateTitleWithMonth(Date())

        compactcalendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                updateDiary(dateClicked)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                updateTitleWithMonth(firstDayOfNewMonth)
                fetchCalendarDataForMonth(firstDayOfNewMonth)
            }
        })

        // 초기에 현재 날짜에 해당하는 이벤트를 보여줍니다.
        updateDiary(Date())

        // 책 리스트를 초기화하고 어댑터에 전달합니다.
        bookRecyclerView.layoutManager = LinearLayoutManager(context)
        bookRecyclerView.adapter = BookAdapter(listOf())

        fetchCalendarDataForMonth(Date()) // 초기 데이터 로드

        return view
    }

    private fun updateDiary(date: Date) {
        val eventsForTheDay = compactcalendar_view.getEvents(date)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = sdf.format(date)

        val bookDataForTheDay: MutableList<CalendarResult> = mutableListOf()

        eventsForTheDay.forEach { event ->
            if (event.data is CalendarResult) {
                bookDataForTheDay.add(event.data as CalendarResult)
            }
        }

        if (bookDataForTheDay.isNotEmpty()) {
            diaryTextView.text = "$dateString"
            bookRecyclerView.adapter = BookAdapter(bookDataForTheDay)
        } else {
            diaryTextView.text = "$dateString"
            bookRecyclerView.adapter = BookAdapter(emptyList())
        }
    }

    private fun fetchCalendarDataForMonth(date: Date) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = date

        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val startDate = sdf.format(calendar.time)

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        val endDate = sdf.format(calendar.time)

        fetchCalendarData(userId, startDate, endDate)
    }

    private fun updateTitleWithMonth(date: Date) {
        val sdf = SimpleDateFormat("yyyy년 MM월", Locale.getDefault())
        title.text = sdf.format(date)
    }

    private fun fetchCalendarData(userId: Int, startDate: String, endDate: String) {
        ApiClient.service.getCalendarData(userId, startDate, endDate).enqueue(object : Callback<CalendarResponse> {
            override fun onResponse(call: Call<CalendarResponse>, response: Response<CalendarResponse>) {
                if (response.isSuccessful) {
                    val results = response.body()?.result
                    updateCalendarWithEvents(results)
                } else {
                    Log.e("CalendarFragment", "Server returned error: ${response.code()} - ${response.message()}")
                    response.body()?.let {
                        Log.e("CalendarFragment", "Error code: ${it.code} - ${it.message}")
                    }
                }
            }

            override fun onFailure(call: Call<CalendarResponse>, t: Throwable) {
                Log.e("CalendarFragment", "Error fetching data: ${t.message}")
            }
        })
    }

    private fun updateCalendarWithEvents(results: List<CalendarResult>?) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        compactcalendar_view.removeAllEvents()

        results?.forEach { result ->
            val startDate = sdf.parse(result.startDate)?.time ?: return@forEach
            val endDate = sdf.parse(result.endDate)?.time ?: return@forEach

            for (time in startDate..endDate step 86400000L) {
                val event = Event(Color.RED, time, result)
                compactcalendar_view.addEvent(event)
            }
        }
    }
}

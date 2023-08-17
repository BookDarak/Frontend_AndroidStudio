package com.cookandroid.bookdarak_1

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import model.Book
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment() {

    lateinit var compactcalendar_view: CompactCalendarView
    lateinit var diaryTextView: TextView
    lateinit var title: TextView
    lateinit var bookRecyclerView: RecyclerView

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

        title.text = "북 캘린더"

        val events = listOf(
            Event(Color.RED, Date().time, "독서 기록 1"),
            Event(Color.BLUE, Date().time + 86400000L, "독서 기록 2"),
            Event(Color.GREEN, Date().time + 2 * 86400000L, "독서 기록 3")
        )

        compactcalendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                updateDiary(dateClicked)
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
            }
        })

        events.forEach { compactcalendar_view.addEvent(it) }

        // 초기에 현재 날짜에 해당하는 이벤트를 보여줍니다.
        updateDiary(Date())

        // 책 리스트를 생성하고 어댑터에 전달합니다.
        val books = listOf(
            Book(R.drawable.book_sample, "Book 1", "Author 1"),
            Book(R.drawable.book_sample, "Book 2", "Author 2"),
            Book(R.drawable.book_sample, "Book 3", "Author 3")
        )

        bookRecyclerView.layoutManager = LinearLayoutManager(context)
        Log.d("CalendarFragment", "Books: $books")
        bookRecyclerView.adapter = BookmarkActivity.BookAdapter(books)

        return view
    }

    // 이벤트를 보여주는 부분을 별도의 메서드로 분리합니다.
    private fun updateDiary(date: Date) {
        val eventsForTheDay = compactcalendar_view.getEvents(date)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateString = sdf.format(date)
        if (eventsForTheDay.isNotEmpty()) {
            diaryTextView.text = "$dateString\n" +
                    eventsForTheDay.joinToString("\n") { it.data.toString() }
        } else {
            diaryTextView.text = "$dateString"
        }
    }
}

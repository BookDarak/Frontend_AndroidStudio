package com.cookandroid.bookdarak_1

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import java.util.Date

class CalendarFragment : Fragment() {

    var userID: String = "userID"
    lateinit var fname: String
    lateinit var str: String
    lateinit var compactcalendar_view: CompactCalendarView
    lateinit var updateBtn: Button
    lateinit var deleteBtn: Button
    lateinit var saveBtn: Button
    lateinit var diaryTextView: TextView
    lateinit var diaryContent: TextView
    lateinit var title: TextView
    lateinit var contextEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        // Initialize UI components
        compactcalendar_view = view.findViewById(R.id.compactcalendar_view)
        diaryTextView = view.findViewById(R.id.diaryTextView)
        saveBtn = view.findViewById(R.id.saveBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)
        updateBtn = view.findViewById(R.id.updateBtn)
        diaryContent = view.findViewById(R.id.diaryContent)
        title = view.findViewById(R.id.title)
        contextEditText = view.findViewById(R.id.contextEditText)

        title.text = "북 캘린더"

        // 임의의 이벤트 데이터
        val events = listOf(
            Event(Color.RED, Date().time, "독서 기록 1"),
            Event(Color.BLUE, Date().time + 86400000L, "독서 기록 2"), // 1일 후
            Event(Color.GREEN, Date().time + 2 * 86400000L, "독서 기록 3") // 2일 후
        )

        // 캘린더 뷰에 리스너를 설정합니다.
        compactcalendar_view.setListener(object : CompactCalendarView.CompactCalendarViewListener {
            override fun onDayClick(dateClicked: Date) {
                val eventsForTheDay = compactcalendar_view.getEvents(dateClicked) // 특정 날짜의 모든 이벤트를 가져옵니다.
                if (eventsForTheDay.isNotEmpty()) {
                    diaryContent.visibility = View.VISIBLE
                    diaryContent.text = eventsForTheDay.joinToString("\n") { it.data.toString() } // 그 날의 모든 이벤트 데이터를 보여줍니다.
                } else {
                    Toast.makeText(context, "이 날짜에는 이벤트가 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date) {
                // 월이 변경되었을 때의 동작을 정의합니다.
            }
        })

        // 임의의 이벤트를 캘린더에 추가합니다.
        events.forEach { compactcalendar_view.addEvent(it) }

        return view
    }
}

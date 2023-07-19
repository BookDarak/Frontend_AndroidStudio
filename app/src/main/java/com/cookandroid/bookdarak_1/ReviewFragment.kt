package com.cookandroid.bookdarak_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import model.Review
import adapter.ReviewAdapter

class ReviewFragment : Fragment() {

    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_review, container, false)

        // Prepare reviews
        val reviews = listOf(
            Review("책먹는 여우", "내용 1"),
            Review("하마랑 책읽기", "내용 2"),
            Review("제목 3", "내용 3")
        )

        // Set up RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.review_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        reviewAdapter = ReviewAdapter(reviews)
        recyclerView.adapter = reviewAdapter

        return view
    }
}

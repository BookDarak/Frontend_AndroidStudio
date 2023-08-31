package com.cookandroid.bookdarak_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cookandroid.bookdarak_1.databinding.ActivityNaviBinding



private const val TAG_CALENDER = "calender_fragment"
private const val TAG_HOME = "home_fragment"
private const val TAG_MY_PAGE = "my_page_fragment"
private const val TAG_FIND = "find_fragment"
private const val TAG_REVIEW = "review_fragment"

class NaviActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNaviBinding
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getIntExtra("USER_ID", -1)

        // Set initial fragment
        setFragment(TAG_HOME, HomeFragment.newInstance(userId))
        // Set initial selected item in the navigation bar
        binding.navigationView.selectedItemId = R.id.homeFragment

        binding.navigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.calenderFragment -> setFragment(TAG_CALENDER, CalendarFragment.newInstance(userId))
                R.id.homeFragment -> setFragment(TAG_HOME, HomeFragment.newInstance(userId))
                R.id.myPageFragment-> setFragment(TAG_MY_PAGE, MyPageFragment.newInstance(userId))
                R.id.reviewFragment -> setFragment(TAG_REVIEW, ReviewFragment())
                R.id.findFragment -> setFragment(TAG_FIND, FindFragment.newInstance(userId))
            }
            true
        }
    }



    private fun setFragment(tag: String, fragment: Fragment) {
        val manager: FragmentManager = supportFragmentManager
        val fragTransaction = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null){
            fragTransaction.add(R.id.mainFrameLayout, fragment, tag)
        }

        val calender = manager.findFragmentByTag(TAG_CALENDER)
        val home = manager.findFragmentByTag(TAG_HOME)
        val myPage = manager.findFragmentByTag(TAG_MY_PAGE)
        val find = manager.findFragmentByTag(TAG_FIND)
        val review = manager.findFragmentByTag(TAG_REVIEW)


        if (calender != null){
            fragTransaction.hide(calender)
        }

        if (home != null){
            fragTransaction.hide(home)
        }

        if (myPage != null) {
            fragTransaction.hide(myPage)
        }
        if (find != null) {
            fragTransaction.hide(find)
        }
        if (review != null) {
            fragTransaction.hide(review)
        }

        if (tag == TAG_CALENDER) {
            if (calender!=null){
                fragTransaction.show(calender)
            }
        }
        else if (tag == TAG_HOME) {
            if (home != null) {
                fragTransaction.show(home)
            }
        }

        else if (tag == TAG_MY_PAGE){
            if (myPage != null){
                fragTransaction.show(myPage)
            }
        }

        else if (tag == TAG_FIND){
            if (find != null){
                fragTransaction.show(find)
            }
        }
        else if (tag == TAG_REVIEW){
            if (review != null){
                fragTransaction.show(review)
            }
        }

        fragTransaction.commitAllowingStateLoss()
    }
}
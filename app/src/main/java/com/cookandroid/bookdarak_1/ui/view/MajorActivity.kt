package com.cookandroid.bookdarak_1.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cookandroid.bookdarak_1.R
import com.cookandroid.bookdarak_1.databinding.ActivityMajorBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MajorActivity : AppCompatActivity() {

    //뷰바인딩
    private val binding by lazy { ActivityMajorBinding.inflate(layoutInflater) }

    //뷰모델
    //lateinit var bookSearchViewModel: BookSearchViewModel

    //내비게이션 컨트롤러
    private lateinit var navController: NavController

    //앱바
    private lateinit var appBarConfiguration: AppBarConfiguration

    //dataStore
    //힐트 안쓴 방식
    //private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)
    //힐트 사용 방식


    //워크매니저
    //private val workManager = WorkManager.getInstance(application)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupJetpackNavigation()



    }

    //바텀내비와 젯팩내비 연동하기
    private fun setupJetpackNavigation(){
        //내비 연결
        val host = supportFragmentManager
            .findFragmentById(R.id.booksearch_nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        //앱바 연결
        //모든프래그먼트를 탑레벨로 설정
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.fragment_favorite, R.id.fragment_search, R.id.fragment_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    //앱바 셋업을 도와주는 친구
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }



}
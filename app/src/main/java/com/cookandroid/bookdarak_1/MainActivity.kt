package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"

    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button
    private lateinit var editId: EditText
    private lateinit var editPw: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLogin = findViewById(R.id.button_login)
        buttonRegister = findViewById(R.id.button_register)
        editId = findViewById(R.id.edit_id)
        editPw = findViewById(R.id.edit_pw)

        buttonLogin.setOnClickListener {
            val id = editId.text.toString()
            val pw = editPw.text.toString()

            val loginRequest = LoginRequest(id, pw)

            ApiClient.service.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val userId = response.body()?.result?.userId ?: -1
                        Log.d(TAG, "Logged in user ID: $userId")

                        // 로그인 성공시 NaviActivity로 이동
                        val intent = Intent(this@MainActivity, NaviActivity::class.java)
                        startActivity(intent)
                    } else {
                        when (response.body()?.code) {
                            2014, 2015 -> dialog("이메일 오류")
                            2016, 2027 -> dialog("비밀번호 오류")
                            else -> dialog("fail")
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    dialog("서버 연결 실패")
                }
            })
        }

        // 회원가입 버튼
        buttonRegister.setOnClickListener {
            val intent = Intent(this, SingupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun dialog(type: String) {
        val dialog = AlertDialog.Builder(this)

        when (type) {
            "success" -> {
                dialog.setTitle("로그인 성공")
                dialog.setMessage("로그인 성공!")
            }
            "fail" -> {
                dialog.setTitle("로그인 실패")
                dialog.setMessage("아이디와 비밀번호를 확인해주세요")
            }
            "이메일 오류" -> {
                dialog.setTitle("로그인 실패")
                dialog.setMessage("이메일 주소가 올바르지 않습니다.")
            }
            "비밀번호 오류" -> {
                dialog.setTitle("로그인 실패")
                dialog.setMessage("비밀번호가 올바르지 않습니다.")
            }
            "서버 연결 실패" -> {
                dialog.setTitle("로그인 실패")
                dialog.setMessage("서버와 연결할 수 없습니다. 다시 시도해주세요.")
            }
        }

        dialog.setPositiveButton("확인") { _, _ ->
            Log.d(TAG, "User acknowledged the dialog")
        }
        dialog.show()
    }
}

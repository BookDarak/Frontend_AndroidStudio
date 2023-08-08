package com.cookandroid.bookdarak_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_singup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class activity_singup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)

        btn_join.setOnClickListener {
            val email = editText_id.text.toString()
            val password = editText_password.text.toString() // 여기 변수명이 잘못된 것 같아요. 수정할 필요가 있습니다.
            val name = editText_name.text.toString()
            val age = editText_age.text.toString().toIntOrNull() ?: 0
            val introduction = editText_introduction.text.toString()

            val signupRequest = SignupRequest(email, password, name, age, introduction)

            ApiClient.service.signup(signupRequest).enqueue(object: Callback<SignupResponse> {
                override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val intent = Intent(this@activity_singup, HomeFragment::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@activity_singup, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    Toast.makeText(this@activity_singup, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
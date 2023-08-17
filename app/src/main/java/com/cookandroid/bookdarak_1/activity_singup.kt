package com.cookandroid.bookdarak_1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SingupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)

        val btnJoin = findViewById<Button>(R.id.btn_join)
        val editTextId = findViewById<EditText>(R.id.editText_id)
        val editTextPassword = findViewById<EditText>(R.id.editText_password)
        val editTextName = findViewById<EditText>(R.id.editText_name)
        val editTextAge = findViewById<EditText>(R.id.editText_age)
        val editTextIntroduction = findViewById<EditText>(R.id.editText_introduction)

        btnJoin.setOnClickListener {
            val email = editTextId.text.toString()
            val password = editTextPassword.text.toString()
            val name = editTextName.text.toString()
            val age = editTextAge.text.toString().toIntOrNull() ?: 0
            val introduction = editTextIntroduction.text.toString()

            val signupRequest = SignupRequest(email, password, name, age, introduction)

            ApiClient.service.signup(signupRequest).enqueue(object: Callback<SignupResponse> {
                override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val intent = Intent(this@SingupActivity, HomeFragment::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@SingupActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                    Toast.makeText(this@SingupActivity, t.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

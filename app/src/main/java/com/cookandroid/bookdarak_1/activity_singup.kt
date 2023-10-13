package com.cookandroid.bookdarak_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cookandroid.bookdarak_1.databinding.ActivitySingupBinding  // <-- View Binding import
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.ArrayAdapter


class activity_singup : AppCompatActivity() {

    private lateinit var binding: ActivitySingupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingupBinding.inflate(layoutInflater)  // <-- View Binding 초기화를 먼저 해야합니다.
        setContentView(binding.root)

        // 스피너에 대한 설정을 binding 초기화 후에 합니다.
        val ageList = (10..99).toList().map { it.toString() }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ageList)
        binding.spinnerAge.adapter = adapter

        binding.btnJoin.setOnClickListener {
            val email = binding.editTextId.text.toString()
            val password = binding.editTextPassword.text.toString()
            val name = binding.editTextName.text.toString()
            val gender = when (binding.radioGroupGender.checkedRadioButtonId) {
                R.id.radioButtonMale -> "M"
                R.id.radioButtonFemale -> "F"
                else -> ""
            }
            val age = binding.spinnerAge.selectedItem.toString().toInt()  // <-- 이 부분을 여기로 옮겼습니다.
            val introduction = binding.editTextIntroduction.text.toString()

            val signupRequest = SignupRequest(email, password, name, gender, age, introduction)


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
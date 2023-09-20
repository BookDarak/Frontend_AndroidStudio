package com.cookandroid.bookdarak_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cookandroid.bookdarak_1.databinding.ActivitySingupBinding  // <-- View Binding import
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class activity_singup : AppCompatActivity() {

    private lateinit var binding: ActivitySingupBinding  // <-- View Binding 인스턴스 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySingupBinding.inflate(layoutInflater)  // <-- View Binding 초기화
        setContentView(binding.root)  // 바뀐 setContentView

        binding.btnJoin.setOnClickListener {  // <-- 'binding.'을 사용하여 뷰에 접근
            val email = binding.editTextId.text.toString()  // <-- 'binding.'을 사용하여 뷰에 접근
            val password = binding.editTextPassword.text.toString()  // <-- 'binding.'을 사용하여 뷰에 접근
            val name = binding.editTextName.text.toString()  // <-- 'binding.'을 사용하여 뷰에 접근
            val gender = when (binding.radioGroupGender.checkedRadioButtonId) {
                R.id.radioButtonMale -> "M"
                R.id.radioButtonFemale -> "F"
                else -> "" // Default or Error Case
            }
            val age = binding.editTextAge.text.toString().toIntOrNull() ?: 0  // <-- 'binding.'을 사용하여 뷰에 접근
            val introduction = binding.editTextIntroduction.text.toString()  // <-- 'binding.'을 사용하여 뷰에 접근

            val signupRequest = SignupRequest(email, password, name, gender, age, introduction) // gender 추가


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
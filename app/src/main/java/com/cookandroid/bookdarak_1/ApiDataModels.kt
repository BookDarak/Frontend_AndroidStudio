package com.cookandroid.bookdarak_1

data class SignupRequest(
    val email: String,
    val password: String,
    val name: String,
    val gender: String, // 이 줄 추가
    val age: Int,
    val introduction: String
)


data class SignupResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: ResultData
) {
    data class ResultData(val id: Int)
}


data class LoginRequest(val email: String, val password: String)

data class LoginResponse(val isSuccess: Boolean, val code: Int, val message: String, val result: LoginResult?) {
    data class LoginResult(val id: Int)  // <-- 'userId'를 'id'로 수정
}


data class RecommendationRequest(
    val age: Int
)

data class RecommendationResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: List<Book>
) {
    data class Book(
        val title: String,
        val author: String,
        val coverImage: String
    )
}
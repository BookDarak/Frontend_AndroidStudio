package com.cookandroid.bookdarak_1

data class SignupRequest(
    val email: String,
    val password: String,
    val name: String,
    val gender: String,
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
        val createdAt: String,
        val updatedAt: String,
        val id: Int,
        val name: String,
        val author: String,
        val isbn: String,
        val imgUrl: String
    )

}
data class UserInfoResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: UserResult
) {
    data class UserResult(
        val name: String,
        val introduction: String,
        val profileUrl: String?,
        val gender: String,
        val age: Int,
        val reviewCount: Int,
        val bookmarkCount: Int,
        val followCount: Int
    )
}
data class QuoteResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: Quote
) {
    data class Quote(
        val line: String,
        val speaker: String
    )
}
data class UserDayResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: Int
)
data class ReviewRequest(
    val rating: String,
    val content: String,   // 이 부분을 List<String>에서 String으로 수정
    val phrase: String?,
    val publicYn: String,
    val startDate: String,
    val endDate: String
)

data class ReviewResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: ReviewResult?
) {
    data class ReviewResult(
        val reviewId: Int  // 이 부분을 createdReviewId에서 reviewId로 수정
    )
}
data class ReviewDetailResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: ReviewDetailResult
) {
    data class ReviewDetailResult(
        val rating: Float,
        val content: String,
        val phrase: String?,
        val publicYn: String,
        val likeCount: Int,
        val startDate: String,
        val endDate: String
    )
}

data class ReviewIdResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: ReviewIdResult
) {
    data class ReviewIdResult(val reviewId: Int)
}

data class UpdateReviewRequest(
    val rating: String?,
    val content: String?,
    val phrase: String?,
    val publicYn: String?,
    val startDate: String?,
    val endDate: String?
)

data class UpdateReviewResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String
)

data class DeleteReviewResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: String
)

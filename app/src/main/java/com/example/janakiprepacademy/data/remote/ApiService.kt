package com.example.janakiprepacademy.data.remote

import retrofit2.Response
import retrofit2.http.*

data class HealthResponse(
    val status: String,
    val service: String,
    val motto: String,
    val dbConnected: Boolean,
    val memoryUsageMB: Int
)

data class SendOtpRequest(val email: String)
data class SendOtpResponse(
    val success: Boolean,
    val message: String,
    val otp: String? = null,
    val emailSent: Boolean? = null,
    val testOtp: String? = null
)

data class RegisterRequest(val name: String, val email: String, val password: String, val otp: String)
data class RegisterResponse(val success: Boolean, val message: String, val user: UserPayload?)

data class LoginRequest(val identifier: String, val password: String)
data class LoginResponse(val success: Boolean, val isAdmin: Boolean, val user: UserPayload?, val message: String?)

data class UserPayload(val name: String, val email: String, val district: String?, val isAdmin: Boolean?)

data class ExamsListResponse(val success: Boolean, val count: Int, val data: List<RemoteExam>)
data class RemoteExam(
    val id: String,
    val exam_code: String,
    val title: String,
    val exam_track: String,
    val description: String?,
    val duration_minutes: Int,
    val total_marks: Double,
    val total_questions: Int,
    val negative_marking: Double,
    val has_five_options: Boolean,
    val is_free: Boolean,
    val attempt_count: Int
)

data class SubmitAttemptRequest(
    val userId: String,
    val answers: Map<String, UserAnswerPayload>,
    val timeSpentSeconds: Long,
    val district: String
)
data class UserAnswerPayload(val selectedOption: String?)

data class SubmitAttemptResponse(val success: Boolean, val result: AttemptResultPayload?)
data class AttemptResultPayload(
    val examId: String,
    val score: Double,
    val correct: Int,
    val incorrect: Int,
    val skipped: Int,
    val accuracy: Double,
    val timeTakenSeconds: Long,
    val airRank: Int,
    val districtRank: Int,
    val percentile: Double
)

interface ApiService {
    @GET("api/health")
    suspend fun checkHealth(): Response<HealthResponse>

    @POST("api/auth/send-otp")
    suspend fun sendOtp(@Body req: SendOtpRequest): Response<SendOtpResponse>

    @POST("api/auth/register")
    suspend fun registerUser(@Body req: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/login")
    suspend fun login(@Body req: LoginRequest): Response<LoginResponse>

    @GET("api/exams")
    suspend fun getExams(@Query("track") track: String? = null): Response<ExamsListResponse>

    @POST("api/exams/{id}/submit")
    suspend fun submitAttempt(
        @Path("id") examId: String,
        @Body req: SubmitAttemptRequest
    ): Response<SubmitAttemptResponse>
}

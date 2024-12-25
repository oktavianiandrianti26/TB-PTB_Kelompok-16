package com.example.presensiapp.service

import androidx.lifecycle.LiveData
import com.example.presensiapp.models.AbsenRequest
import com.example.presensiapp.models.ApiResponse
import com.example.presensiapp.models.DailyTaskFormResponse
import com.example.presensiapp.models.DailyTaskResponse
import com.example.presensiapp.models.IzinRequest
import com.example.presensiapp.models.LoginRequest
import com.example.presensiapp.models.LoginResponse
import com.example.presensiapp.models.PermitResponse
import com.example.presensiapp.models.RegisterRequest
import com.example.presensiapp.models.RegisterResponse
import com.example.presensiapp.models.TaskReportResponse
import com.example.presensiapp.models.UpdateRequest
import com.example.presensiapp.models.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
//    @GET("users/{id}")
//    fun getUserById(@Path("id") userId: String): LiveData<ApiResponse<UserResponse>>

    @GET("todos/1")
    suspend fun getUserById(): UserResponse

    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

//    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
//        @Field("name") name: String,
//        @Field("email") email: String,
//        @Field("password") password: String
    @Body request: RegisterRequest
    ): RegisterResponse

    @GET("permit")
    suspend fun fetchPresensi(@Query("userId") userId: String): PermitResponse

//    @Multipart
//    @POST("presensi") // Sesuaikan endpoint
//    suspend fun submitAbsen(@Body absenRequest: AbsenRequest): Response<Unit>
@Multipart
@POST("presensi")
suspend fun submitAbsen(
    @Part("name") name: RequestBody,
    @Part("datetime") datetime: RequestBody,
    @Part("location") location: RequestBody,
    @Part("description") description: RequestBody,
    @Part("type") type: RequestBody,
    @Part("userId") userId: RequestBody,
    @Part image: MultipartBody.Part // Untuk gambar
): Response<Unit>

//    @Multipart
    @POST("izin")

    suspend fun submitIzin(
    @Body
    request: IzinRequest

//        @Part("name") name: RequestBody,
//        @Part("datetime") datetime: RequestBody,
//        @Part("description") description: RequestBody,
//        @Part("type") type: RequestBody,
//        @Part("userId") userId: RequestBody,
    ): Response<Unit>


    @Multipart
    @POST("createInventory")
    suspend fun createInventory(
        @Part("name") name: RequestBody,
        @Part("datetime") datetime: RequestBody,
        @Part("description") description: RequestBody,
        @Part("type") type: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part image1: MultipartBody.Part,
        @Part image2: MultipartBody.Part

    ): Response<Unit>

    @GET("get-report")
    suspend fun getTaskReport(
        @Query("userId") userId: Int,
    ): TaskReportResponse


//    @Multipart
//    @FormUrlEncoded

    @POST("profile")
    suspend fun updateUser(
//        @Part("name") name: RequestBody,
//        @Part("hp") nohp: RequestBody,
//        @Part("address") address: RequestBody,
////        @Part image: MultipartBody.Part // Untuk gambar
//        @Part("image") image: RequestBody,
//        @Field("name") name: String,
//        @Field("hp") nohp: String,
//        @Field("address") address: String,
    @Body request: UpdateRequest

//        @Part image: MultipartBody.Part // Untuk gambar
//        @Part("image") image: RequestBody,

    ): Response<Unit>


    @GET("dailytask")
    suspend fun fetchTask(): DailyTaskResponse

    @Multipart
    @POST("createTask")
    suspend fun submitTask(
        @Part("userId") id: RequestBody,
        @Part("taskId") taskId: RequestBody,
        @Part imagePart1: MultipartBody.Part,
        @Part imagePart2: MultipartBody.Part
    ): Response<DailyTaskFormResponse>


}
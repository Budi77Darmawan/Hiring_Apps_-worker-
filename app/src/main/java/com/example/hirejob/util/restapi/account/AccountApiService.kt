package com.example.hirejob.util.restapi.account

import com.example.hirejob.util.restapi.account.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface AccountApiService {

    @FormUrlEncoded
    @POST("account/register")
    suspend fun registerRequest(@Field("roleAccount") role: String?,
                                @Field("name") name: String?,
                                @Field("email") email: String?,
                                @Field("numberPhone") numberPhone: String?,
                                @Field("password") password: String?) : RegisterResponse

    @FormUrlEncoded
    @POST("account/login")
    suspend fun loginRequest(@Field("email") email: String?,
                             @Field("password") password: String?) : LoginResponse

    @FormUrlEncoded
    @PATCH("account")
    suspend fun updateAccountRequest(
        @Field("name") name: String?,
        @Field("numberPhone") numberPhone: String?)

    @Multipart
    @PATCH("freelancers")
    suspend fun updateFreelancersRequest(
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part?): UpdateFreelancersResponse
//
//    @Multipart
//    @PATCH("freelancers")
//    suspend fun updateRecruiters2Request(
//        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>): UpdateRecruitersResponse

}
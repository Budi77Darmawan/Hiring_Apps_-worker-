package com.example.hirejob.util.restapi.portfolio

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface PortfolioApiService {

    @GET("portofolio/{id}")
    suspend fun getPortofoliobyIDRequest(
        @Path("id") id: String?
    ): PortfolioResponse

    @Multipart
    @POST("portofolio/")
    suspend fun addPortfolioRequest(
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part?
    ): UpdatePortfolioResponse

    @DELETE("portofolio/{id}")
    suspend fun deletePortfolioRequest(
        @Path("id") id_exp: String?
    ): PortfolioResponse

    @Multipart
    @PATCH("portofolio/{id}")
    suspend fun updatePortfolioRequest(
        @Path("id") id_porto: String?,
        @PartMap data: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part?
    ): UpdatePortfolioResponse
}
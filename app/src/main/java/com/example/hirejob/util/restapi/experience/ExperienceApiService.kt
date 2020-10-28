package com.example.hirejob.util.restapi.experience

import retrofit2.http.*

interface ExperienceApiService {

    @GET("experience/{id}")
    suspend fun getExperiencebyIDRequest(
        @Path("id") id: String?
    ): ExperienceResponse

    @FormUrlEncoded
    @POST("experience/")
    suspend fun addExperienceRequest(
        @FieldMap data: Map<String, @JvmSuppressWildcards String>
    ): UpdateExperienceResponse

    @DELETE("experience/{id}")
    suspend fun deleteExperienceByIDRequest(
        @Path("id") id_exp: String?
    ): ExperienceResponse

    @FormUrlEncoded
    @PATCH("experience/{id}")
    suspend fun updateExperienceRequest(
        @Path("id") id_exp: String?,
        @FieldMap data: Map<String, @JvmSuppressWildcards String>
    ): UpdateExperienceResponse
}
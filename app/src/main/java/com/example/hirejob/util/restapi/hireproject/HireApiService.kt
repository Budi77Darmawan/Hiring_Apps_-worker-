package com.example.hirejob.util.restapi.hireproject

import retrofit2.http.*

interface HireApiService {

    @GET("hireproject")
    suspend fun getListHireProject(): HireListResponse

    @GET("hireproject/freelancers")
    suspend fun getListFreelancersProject(
        @Query("idProject") id_project: String?
    ): FreelancersProjectResponse

    @FormUrlEncoded
    @PATCH("hireproject/{id}")
    suspend fun updateHireProject(
        @Path("id") id_hire: String?,
        @Field("statusConfirm") statusConfirm: String?
    )
}
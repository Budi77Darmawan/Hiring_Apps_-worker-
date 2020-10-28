package com.example.hirejob.util.restapi.freelancers

import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FreelancersApiService {

    @GET("freelancers/")
    suspend fun getFreelancersRequest(
        @QueryMap data: Map<String, @JvmSuppressWildcards String>
    ): FreelancersResponse

    @GET("freelancers/{id}")
    suspend fun getFreelancersbyIDRequest(
        @Path("id") idFreelancer: String?
    ): FreelancersResponse
}
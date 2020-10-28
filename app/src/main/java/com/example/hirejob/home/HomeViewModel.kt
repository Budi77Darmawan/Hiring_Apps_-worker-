package com.example.hirejob.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirejob.util.restapi.account.LoginResponse
import com.example.hirejob.util.restapi.freelancers.FreelancersApiService
import com.example.hirejob.util.restapi.freelancers.FreelancersResponse
import kotlinx.coroutines.*
import okhttp3.RequestBody
import kotlin.coroutines.CoroutineContext

class HomeViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: FreelancersApiService
    val freelancersLiveData = MutableLiveData(listOf<FreelancersModel>())
    val loadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setFreelancersService(service: FreelancersApiService) {
        this.service = service
    }

    fun getFreelancersApi(search: String, typeSearch: String, job: String, status: String) {
        launch {
            loadingLiveData.value = true
            val queryMap = HashMap<String, String>()
            queryMap["search[$typeSearch]"] = search
            queryMap["jobDesc"] = job
            queryMap["statusJob"] = status
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getFreelancersRequest(queryMap)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is FreelancersResponse) {
                freelancersLiveData.value = response.data?.map {
                    FreelancersModel(
                        it.id_freelancer.orEmpty(),
                        it.name.orEmpty(),
                        it.job.orEmpty(),
                        it.status.orEmpty(),
                        it.city.orEmpty(),
                        it.description.orEmpty(),
                        it.image.orEmpty(),
                        it.skill.orEmpty(),
                        it.numberPhone.orEmpty(),
                        it.email.orEmpty()
                    )
                }
            }
            loadingLiveData.value = false
        }
    }
}
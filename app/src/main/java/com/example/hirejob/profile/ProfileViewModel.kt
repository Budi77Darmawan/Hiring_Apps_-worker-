package com.example.hirejob.profile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirejob.home.FreelancersModel
import com.example.hirejob.util.restapi.freelancers.FreelancersApiService
import com.example.hirejob.util.restapi.freelancers.FreelancersResponse
import com.example.hirejob.util.sharedpreferences.Constant
import com.example.hirejob.util.sharedpreferences.SharedPrefProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ProfileViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: FreelancersApiService
    val freelancersLiveData = MutableLiveData(listOf<FreelancersModel>())
    val loadingLiveData = MutableLiveData<Boolean>()
    private lateinit var sharedPref: SharedPrefProvider
    val idAccountLiveData = MutableLiveData<String>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun getSharedPreference(mContext: Context) {
        sharedPref = SharedPrefProvider(mContext)
        idAccountLiveData.value = sharedPref.getString(Constant.KEY_ACCOUNT).toString()
    }

    fun setFreelancersService(service: FreelancersApiService) {
        this.service = service
    }

    fun getDataFreelancersApi(id_free: String) {
        launch {
            loadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getFreelancersbyIDRequest(id_free)
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
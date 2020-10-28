package com.example.hirejob.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirejob.util.restapi.account.AccountApiService
import com.example.hirejob.util.restapi.account.RegisterResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegisterViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: AccountApiService
    val registerLiveData = MutableLiveData<Boolean>()
    val messageLiveData = MutableLiveData<String>()
    val loadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setAccountService(service: AccountApiService) {
        this.service = service
    }

    fun registerAccountApi(name: String, email: String, numberPhone: String, password: String) {
        launch {
            loadingLiveData.value = true
            registerLiveData.value = false
            val response = withContext(Dispatchers.IO) {
                try {
                    service.registerRequest("Freelancers", name, email, numberPhone, password)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is RegisterResponse) {
                registerLiveData.value = response.success
                messageLiveData.value = response.message
            }
            loadingLiveData.value = false
        }
    }
}
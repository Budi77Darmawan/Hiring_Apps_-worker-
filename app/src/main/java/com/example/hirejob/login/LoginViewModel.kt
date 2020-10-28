package com.example.hirejob.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirejob.util.restapi.account.AccountApiService
import com.example.hirejob.util.restapi.account.LoginResponse
import com.example.hirejob.util.sharedpreferences.Constant
import com.example.hirejob.util.sharedpreferences.SharedPrefProvider
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LoginViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: AccountApiService
    private lateinit var sharedPref: SharedPrefProvider
    val loginLiveData = MutableLiveData<Boolean>()
    val messageLiveData = MutableLiveData<String>()
    val loadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    private fun setSharedPreference(
        mContext: Context,
        name: String?,
        idAccount: String?,
        token: String?,
        remember: Boolean
    ) {
        sharedPref = SharedPrefProvider(mContext)
        sharedPref.putString(Constant.KEY_NAME, name)
        sharedPref.putString(Constant.KEY_ACCOUNT, idAccount)
        sharedPref.putString(Constant.KEY_TOKEN, token)
        sharedPref.putBoolean(Constant.KEY_REMEMBER, remember)
    }

    fun setAccountService(service: AccountApiService) {
        this.service = service
    }

    fun loginAccountApi(mContext: Context, email: String, password: String) {
        launch {
            loadingLiveData.value = true
            loginLiveData.value = false
            val response = withContext(Dispatchers.IO) {
                try {
                    service.loginRequest(email, password)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is LoginResponse) {
                loginLiveData.value = response.success
                messageLiveData.value = response.message
                setSharedPreference(
                    mContext,
                    response.data?.name,
                    response.data?.id,
                    response.data?.token,
                    response.success
                )
            }
            loadingLiveData.value = false
        }
    }
}
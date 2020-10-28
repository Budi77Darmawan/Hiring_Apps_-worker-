package com.example.hirejob.util.restapi

import android.content.Context
import android.util.Log
import com.example.hirejob.util.sharedpreferences.Constant
import com.example.hirejob.util.sharedpreferences.SharedPrefProvider
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor (private val mContext: Context) : Interceptor {

    private lateinit var sharedPref: SharedPrefProvider

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        sharedPref = SharedPrefProvider(mContext)
        val token = sharedPref.getString(Constant.KEY_TOKEN) ?: ""
        proceed(
            request().newBuilder()
                .addHeader("Authorization", "Bareer $token")
                .build()
        )
    }
}
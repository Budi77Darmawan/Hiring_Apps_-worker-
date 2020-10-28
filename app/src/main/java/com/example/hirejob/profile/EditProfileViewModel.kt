package com.example.hirejob.profile

import androidx.annotation.NonNull
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirejob.util.restapi.account.AccountApiService
import com.example.hirejob.util.restapi.account.UpdateFreelancersResponse
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import kotlin.coroutines.CoroutineContext

class EditProfileViewModel: ViewModel(), CoroutineScope {
    private lateinit var service: AccountApiService
    private var image: MultipartBody.Part? = null
    val messageLiveData = MutableLiveData<String>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setAccountService(service: AccountApiService) {
        this.service = service
    }

    fun updateAccountApi(name: String, numberPhone: String) {
        launch {
            try {
                service.updateAccountRequest(name, numberPhone)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    fun updateFreelancersApi(jobDesc: String, statusJob: String, description: String, cityAddress: String, imgFile: File?) {
        val job = createPartFromString(jobDesc)
        val status = createPartFromString(statusJob)
        val desc = createPartFromString(description)
        val address = createPartFromString(cityAddress)

        val partMap = HashMap<String, RequestBody>()
        partMap["jobDesc"] = job
        partMap["statusJob"] = status
        partMap["description"] = desc
        partMap["cityAddress"] = address

        if (imgFile != null) {
            val exs = imgFile.name.split(".")[1]
            val requestFile =
                imgFile.asRequestBody("image/${exs}".toMediaTypeOrNull())
            image =
                MultipartBody.Part?.createFormData("image", imgFile.name, requestFile)
        }
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.updateFreelancersRequest(partMap, image)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }

            if (response is UpdateFreelancersResponse) {
                messageLiveData.value = response.message
            }
        }
    }

    @NonNull
    private fun createPartFromString(json: String): RequestBody {
        val mediaType = "text/plain".toMediaType()
        return json
            .toRequestBody(mediaType)
    }
}
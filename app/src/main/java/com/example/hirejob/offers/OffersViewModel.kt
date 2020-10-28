package com.example.hirejob.offers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hirejob.home.FreelancersModel
import com.example.hirejob.util.restapi.hireproject.FreelancersProjectResponse
import com.example.hirejob.util.restapi.hireproject.HireApiService
import com.example.hirejob.util.restapi.hireproject.HireListResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class OffersViewModel : ViewModel(), CoroutineScope {
    private lateinit var service: HireApiService
    val hireProjectLiveData = MutableLiveData(listOf<HireProjectModel>())
    val freelancersProjectLiveData = MutableLiveData(listOf<FreelancersProjectModel>())
    val loadingLiveData = MutableLiveData<Boolean>()

    override val coroutineContext: CoroutineContext
        get() = Job() + Dispatchers.Main

    fun setHireProjectService(service: HireApiService) {
        this.service = service
    }

    fun getHireProjectApi() {
        launch {
            loadingLiveData.value = true
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getListHireProject()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is HireListResponse) {
                hireProjectLiveData.value = response.data?.map {
                    HireProjectModel(
                        it.id_hire.orEmpty(),
                        it.id_project.orEmpty(),
                        it.nameRec.orEmpty(),
                        it.company_name.orEmpty(),
                        it.nameFree.orEmpty(),
                        it.imageFree.orEmpty(),
                        it.imageProject.orEmpty(),
                        it.projectName.orEmpty(),
                        it.projectDeadline.orEmpty(),
                        it.projectDesc.orEmpty(),
                        it.projectJob.orEmpty(),
                        it.message.orEmpty(),
                        it.price.orEmpty(),
                        it.statusConfirm.orEmpty()
                    )
                }
            }
            loadingLiveData.value = false
        }
    }

    fun getListProjectApi(id_project: String?) {
        launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getListFreelancersProject(id_project)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is FreelancersProjectResponse) {
                freelancersProjectLiveData.value = response.data?.map {
                    FreelancersProjectModel(
                        it.id_project.orEmpty(),
                        it.nameFree.orEmpty(),
                        it.imageFree.orEmpty(),
                        it.jobFree.orEmpty()
                    )
                }
            }
        }
    }

    fun updateHireProjectApi(id_hire: String?, statusConfirm: String?) {
        launch {
            try {
                service.updateHireProject(id_hire, statusConfirm)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}
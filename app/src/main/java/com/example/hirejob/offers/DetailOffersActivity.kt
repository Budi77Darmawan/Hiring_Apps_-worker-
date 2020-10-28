package com.example.hirejob.offers

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.hirejob.R
import com.example.hirejob.databinding.ActivityDetailOffersBinding
import com.example.hirejob.util.restapi.ApiClient
import com.example.hirejob.util.restapi.hireproject.HireApiService
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottomsheet_offers.view.*

class DetailOffersActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailOffersBinding
    private lateinit var viewModel: OffersViewModel
    private lateinit var bottomSheetConfirm: BottomSheetDialog
    private var offers: HireProjectModel? = null
    private val baseUrl = "http://18.212.194.218:8080/images/"

    companion object {
        const val EXTRA_DATA = "data_offers"
        const val UPDATE_CODE = 9013
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_offers)

        bottomSheetConfirm = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_offers, null)
        bottomSheetConfirm.setContentView(view)

        offers = intent.getParcelableExtra<HireProjectModel>(EXTRA_DATA)

        binding.apply {
            tvProjectName.text = offers?.projectName
            tvDeadline.text = offers?.projectDeadline
            tvDesc.text = offers?.projectDesc
            tvMessage.text = offers?.message
            tvJob.text = offers?.projectJob
            tvPrice.text = offers?.price

            if (!offers?.imageProject.isNullOrEmpty()) {
                Glide.with(this@DetailOffersActivity)
                    .load("${baseUrl}${offers?.imageProject}")
                    .into(imgProject)
            }

            if (offers?.statusConfirm != "0") {
                btnConfirm.visibility = View.GONE
            }
        }

        viewModel = ViewModelProvider(this).get(OffersViewModel::class.java)
        val service =
            ApiClient.getApiClient(this)?.create(HireApiService::class.java)
        if (service != null) {
            viewModel.setHireProjectService(service)
        }
        viewModel.getListProjectApi(offers?.id_project)
        subscribeLiveData(offers?.id_project)

        binding.btnBack.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)
        view.btn_reject.setOnClickListener(this)
        view.btn_accept.setOnClickListener(this)
    }

    private fun showRecyclerList(list: List<FreelancersProjectModel>) {
        binding.rvListFreelancers.layoutManager = LinearLayoutManager(this)
        val listFreelancersProjectAdapter = ListFreelancersProjectAdapter(list)
        binding.rvListFreelancers.adapter = listFreelancersProjectAdapter
    }

    private fun subscribeLiveData(id_project: String?) {
        viewModel.getListProjectApi(id_project)
        viewModel.freelancersProjectLiveData.observe(this, {
            showRecyclerList(it)
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_back -> onBackPressed()
            R.id.btn_confirm -> {
                bottomSheetConfirm.show()
            }
            R.id.btn_reject -> {
                viewModel.updateHireProjectApi(offers?.id_hire, "-1")
                subscribeLiveData(offers?.id_project)
                bottomSheetConfirm.dismiss()
                binding.btnConfirm.visibility = View.GONE
            }
            R.id.btn_accept -> {
                viewModel.updateHireProjectApi(offers?.id_hire, "1")
                subscribeLiveData(offers?.id_project)
                bottomSheetConfirm.dismiss()
                binding.btnConfirm.visibility = View.GONE

            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
    }
}
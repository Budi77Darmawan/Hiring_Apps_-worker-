package com.example.hirejob.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.hirejob.R
import com.example.hirejob.databinding.FragmentProfileBinding
import com.example.hirejob.home.DetailFreelancersActivity
import com.example.hirejob.home.DetailViewPagerAdapter
import com.example.hirejob.home.FreelancersModel
import com.example.hirejob.home.detailviewpager.DetailFragment
import com.example.hirejob.home.detailviewpager.ExperienceFragment
import com.example.hirejob.home.detailviewpager.PortfolioFragment
import com.example.hirejob.offers.DetailOffersActivity
import com.example.hirejob.util.restapi.ApiClient
import com.example.hirejob.util.restapi.freelancers.FreelancersApiService

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var data: FreelancersModel
    private var idAccount = "0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val service =
                ApiClient.getApiClient(requireContext())?.create(FreelancersApiService::class.java)
        if (service != null) {
            viewModel.setFreelancersService(service)
        }
        viewModel.getSharedPreference(requireContext())
        subscribeLiveData()

        binding.btnEdit.setOnClickListener{
            val intent = Intent(activity, EditProfileActivity::class.java)
            intent.putExtra(EditProfileActivity.EXTRA_DATA, data)
            startActivityForResult(intent, EditProfileActivity.UPDATE_CODE)
        }
        binding.btnLogout.setOnClickListener{
            LogoutDialog().show(requireActivity().supportFragmentManager, LogoutDialog.TAG)
        }

        return binding.root
    }

    private fun setUpTabs(id_account: String, data: FreelancersModel) {
        val adapter = DetailViewPagerAdapter(childFragmentManager)
        adapter.addFragment(DetailFragment(data), "Detail")
        adapter.addFragment(ExperienceFragment(id_account, true), "Experience")
        adapter.addFragment(PortfolioFragment(id_account, true), "Portfolio")
        binding.vpProfile.adapter = adapter
        binding.TabLayout2.setupWithViewPager(binding.vpProfile)
    }

    private fun subscribeLiveData() {
        viewModel.idAccountLiveData.observe(this, {
            idAccount = it
            viewModel.getDataFreelancersApi(it)
        })

        viewModel.freelancersLiveData.observe(this, {
            if (it.isNotEmpty()) {
                data = it[0]
                setUpTabs(idAccount, data)
                setView()
            }
        })

        viewModel.loadingLiveData.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    private fun setView() {
        binding.apply {
            tvName.text = if (data.name.isNullOrEmpty()) "Full name" else data.name
            tvJob.text = if (data.jobDesc.isNullOrEmpty()) "Job tittle" else data.jobDesc
            tvCity.text = if (data.cityAddress.isNullOrEmpty()) "Address" else data.cityAddress
            tvStatus.text = if (data.statusJob.isNullOrEmpty()) "Status work" else data.statusJob
        }
        if (!data.image.isNullOrEmpty()) {
            Glide.with(this)
                    .load("http://18.212.194.218:8080/images/${data.image}")
                    .into(binding.imgProfil)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == EditProfileActivity.UPDATE_CODE) {
            subscribeLiveData()
        }
    }
}
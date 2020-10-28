package com.example.hirejob.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hirejob.R
import com.example.hirejob.databinding.ActivityDetailFreelancersBinding
import com.example.hirejob.home.detailviewpager.DetailFragment
import com.example.hirejob.home.detailviewpager.ExperienceFragment
import com.example.hirejob.home.detailviewpager.PortfolioFragment
import kotlinx.android.synthetic.main.activity_detail_freelancers.*

class DetailFreelancersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailFreelancersBinding
    private var data: FreelancersModel? = null

    companion object {
        const val EXTRA_DATA = "data_freelancers"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_freelancers)

        data = intent.getParcelableExtra(EXTRA_DATA)

        binding.let {
            it.tvName.text = if (data?.name.isNullOrEmpty()) "Full name" else data?.name
            it.tvJob.text = if (data?.jobDesc.isNullOrEmpty()) "Job tittle" else data?.jobDesc
            it.tvCity.text = if (data?.cityAddress.isNullOrEmpty()) "Address" else data?.cityAddress
            it.tvStatus.text = if (data?.statusJob.isNullOrEmpty()) "Status work" else data?.statusJob
            if (!data?.image.isNullOrEmpty()) {
                Glide.with(this)
                        .load("http://18.212.194.218:8080/images/${data?.image}")
                        .into(it.imgProfil)
            }
        }
        setUpTabs(data?.id_account, data)

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setUpTabs(idFreelancer: String?, data: FreelancersModel?) {
        val adapter = DetailViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(DetailFragment(data), "Detail")
        adapter.addFragment(ExperienceFragment(idFreelancer, false), "Experience")
        adapter.addFragment(PortfolioFragment(idFreelancer, false), "Portfolio")
        binding.vpDetail.adapter = adapter
        binding.TabLayout.setupWithViewPager(binding.vpDetail)
    }
}

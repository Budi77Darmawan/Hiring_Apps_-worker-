package com.example.hirejob.offers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hirejob.R
import com.example.hirejob.databinding.FragmentOffersBinding
import com.example.hirejob.util.restapi.ApiClient
import com.example.hirejob.util.restapi.hireproject.HireApiService

class OffersFragment : Fragment() {
    private lateinit var binding: FragmentOffersBinding
    private lateinit var viewModel: OffersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_offers, container, false)

        viewModel = ViewModelProvider(this).get(OffersViewModel::class.java)
        val service =
            ApiClient.getApiClient(requireContext())?.create(HireApiService::class.java)
        if (service != null) {
            viewModel.setHireProjectService(service)
        }
        subscribeLiveData()

        return binding.root
    }

    private fun showRecyclerList(list: List<HireProjectModel>) {
        binding.rvOffers.layoutManager = LinearLayoutManager(requireContext())
        val listHireProjectAdapter = ListHireProjectAdapter(list)
        binding.rvOffers.adapter = listHireProjectAdapter

        listHireProjectAdapter.setOnItemClickCallback(object :
            ListHireProjectAdapter.OnItemClickCallback {
            override fun onItemClicked(id: Int) {
                val intent = Intent(activity, DetailOffersActivity::class.java)
                intent.putExtra(DetailOffersActivity.EXTRA_DATA, list[id])
                startActivityForResult(intent, DetailOffersActivity.UPDATE_CODE)
            }
        })
    }

    private fun subscribeLiveData() {
        viewModel.getHireProjectApi()
        viewModel.hireProjectLiveData.observe(this, {
            if (it.isNullOrEmpty()) {
                binding.imgBox.visibility = View.VISIBLE
                binding.descImg.visibility = View.VISIBLE
            } else {
                showRecyclerList(it)
                binding.imgBox.visibility = View.GONE
                binding.descImg.visibility = View.GONE
            }
        })

        viewModel.loadingLiveData.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_CANCELED && requestCode == DetailOffersActivity.UPDATE_CODE) {
            subscribeLiveData()
        }
    }
}
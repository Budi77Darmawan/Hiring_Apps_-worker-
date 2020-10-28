package com.example.hirejob.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hirejob.R
import com.example.hirejob.databinding.FragmentHomeBinding
import com.example.hirejob.util.restapi.ApiClient
import com.example.hirejob.util.restapi.freelancers.FreelancersApiService
import com.example.hirejob.util.sharedpreferences.Constant
import com.example.hirejob.util.sharedpreferences.SharedPrefProvider
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var sharedPref: SharedPrefProvider
    private var job = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        sharedPref = SharedPrefProvider(requireContext())
        val name = sharedPref.getString(Constant.KEY_NAME)
        if (!name.isNullOrEmpty()) {
            val say = "Hey, $name!"
            binding.tvName.text = say
        }

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val service =
                ApiClient.getApiClient(requireContext())?.create(FreelancersApiService::class.java)
        if (service != null) {
            viewModel.setFreelancersService(service)
        }
        subscribeLiveData()

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        c[year, month] = day
        val format = SimpleDateFormat("EEE, dd MMMM yyyy", Locale.ENGLISH)
        val dateString = format.format(c.time)
        binding.tvDate.text = dateString

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.job_array,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerJob.adapter = adapter
        }
        binding.spinnerJob.onItemSelectedListener = this

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when(position) {
            0 -> job = ""
            1 -> job = "Android Developer"
            2 -> job = "Web Developer"
            3 -> job = "IOS Developer"
        }
        subscribeLiveData()
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
    }

    private fun showRecyclerList(list: List<FreelancersModel>) {
        binding.rvFreelancers.layoutManager = LinearLayoutManager(requireContext())
        val listFreelancersAdapter = ListFreelancersAdapter(list)
        binding.rvFreelancers.adapter = listFreelancersAdapter

        listFreelancersAdapter.setOnItemClickCallback(object :
            ListFreelancersAdapter.OnItemClickCallback {
            override fun onItemClicked(id: Int) {
                val intent = Intent(activity, DetailFreelancersActivity::class.java)
                intent.putExtra(DetailFreelancersActivity.EXTRA_DATA, list[id])
                startActivity(intent)
            }
        })
    }

    private fun subscribeLiveData() {
        viewModel.getFreelancersApi("", "name", job, "")
        viewModel.freelancersLiveData.observe(this, {
            showRecyclerList(it)
        })

        viewModel.loadingLiveData.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }
}
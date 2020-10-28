package com.example.hirejob.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hirejob.R
import com.example.hirejob.databinding.FragmentSearchBinding
import com.example.hirejob.home.DetailFreelancersActivity
import com.example.hirejob.home.FreelancersModel
import com.example.hirejob.home.HomeViewModel
import com.example.hirejob.home.ListFreelancersAdapter
import com.example.hirejob.util.restapi.ApiClient
import com.example.hirejob.util.restapi.freelancers.FreelancersApiService
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.bottomsheet_search.view.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*

class SearchFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var bottomSheetSearch: BottomSheetDialog
    private var idSearch = 0
    private var search = ""
    private var typeSearch = ""
    private var job = ""
    private var status = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val service =
            ApiClient.getApiClient(requireContext())?.create(FreelancersApiService::class.java)
        if (service != null) {
            viewModel.setFreelancersService(service)
        }

        bottomSheetSearch = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottomsheet_search, null)
        bottomSheetSearch.setContentView(view)

        view.chip_search.setOnCheckedChangeListener { chipGroup, checkedId ->
            when (chipGroup.findViewById<Chip>(checkedId)?.text.toString()) {
                "Location" -> {
                    idSearch = 1
                    binding.search.queryHint = "Location"
                }
                "Skill" -> {
                    idSearch = 2
                    binding.search.queryHint = "Skill"
                }
                else -> {
                    idSearch = 0
                    binding.search.queryHint = "Freelancers Name"
                    view.chip_name.isChecked = true
                }
            }
        }

        view.chip_job.setOnCheckedChangeListener { chipGroup, checkedId ->
            job = when (chipGroup.findViewById<Chip>(checkedId)?.text.toString()) {
                "Android Developer" -> "Android Developer"
                "IOS Developer" -> "IOS Developer"
                "Web Developer" -> "Web Developer"
                else -> ""
            }
        }

        view.chip_status.setOnCheckedChangeListener { chipGroup, checkedId ->
            status = when (chipGroup.findViewById<Chip>(checkedId)?.text.toString()) {
                "Freelancer" -> "Freelancer"
                "Full Time" -> "Full Time"
                "Part Time" -> "Part Time"
                else -> ""
            }
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    binding.descSearch.visibility = View.GONE
                    search = newText.toLowerCase(Locale.getDefault())
                    typeSearch = when (idSearch) {
                        1 -> "cityAddress"
                        2 -> "skill_name"
                        else -> "name"
                    }
                    subscribeLiveData()
                    rv_Freelancers.adapter?.notifyDataSetChanged()
                } else {
                    binding.descImg.visibility = View.GONE
                    binding.imgBox.visibility = View.GONE
                    search = ""
                    subscribeLiveData()
                    rv_Freelancers.adapter?.notifyDataSetChanged()
                }
                return true
            }
        })

        binding.filter.setOnClickListener(this)
        view.btn_submit.setOnClickListener(this)
        return binding.root
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
        viewModel.getFreelancersApi(search, typeSearch, job, status)
        viewModel.freelancersLiveData.observe(this, {
            if (!it.isNullOrEmpty()) {
                binding.rvFreelancers.visibility = View.VISIBLE
                binding.descImg.visibility = View.GONE
                binding.imgBox.visibility = View.GONE
                showRecyclerList(it)
            } else {
                binding.rvFreelancers.visibility = View.GONE
                binding.descImg.visibility = View.VISIBLE
                binding.imgBox.visibility = View.VISIBLE
            }
        })

        viewModel.loadingLiveData.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.filter -> {
                bottomSheetSearch.show()
            }
            R.id.btn_submit -> {
                binding.descSearch.visibility = View.GONE
                subscribeLiveData()
                bottomSheetSearch.dismiss()
            }
        }
    }

}

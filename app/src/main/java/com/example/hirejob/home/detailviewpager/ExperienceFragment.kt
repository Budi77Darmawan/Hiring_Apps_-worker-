package com.example.hirejob.home.detailviewpager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hirejob.R
import com.example.hirejob.databinding.FragmentExperienceBinding
import com.example.hirejob.profile.AddExperienceActivity
import com.example.hirejob.profile.EditExperienceActivity
import com.example.hirejob.util.restapi.ApiClient
import com.example.hirejob.util.restapi.experience.ExperienceApiService
import com.example.hirejob.util.restapi.experience.ExperienceResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottomsheet_profile.view.*
import kotlinx.coroutines.*

class ExperienceFragment(private val id: String?, private val more: Boolean) : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentExperienceBinding
    private lateinit var bottomSheetMore: BottomSheetDialog
    private lateinit var service: ExperienceApiService
    private lateinit var coroutineScope: CoroutineScope
    private var list = listOf<ExperienceModel>()
    private var idExp = ""
    private var pos = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experience, container, false)
        binding.rvExperience.setHasFixedSize(true)

        if(!more) {
            binding.btnAdd.visibility = View.GONE
        } else {
            binding.btnAdd.visibility = View.VISIBLE
        }
        service = ApiClient.getApiClient(requireContext())!!.create(ExperienceApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        bottomSheetMore = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottomsheet_profile, null)
        bottomSheetMore.setContentView(view)

        binding.btnAdd.setOnClickListener(this)
        view.btn_delete.setOnClickListener(this)
        view.btn_edit.setOnClickListener(this)

        getExperienceApi()

        return binding.root
    }

    private fun getExperienceApi() {
        binding.progressBar.visibility = View.VISIBLE

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getExperiencebyIDRequest(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is ExperienceResponse) {
                if (response.data != null) {
                    list = response.data.map {
                        ExperienceModel(
                            it.id_exp.orEmpty(),
                            it.id_account.orEmpty(),
                            it.company_name.orEmpty(),
                            it.position.orEmpty(),
                            it.start.orEmpty(),
                            it.end.orEmpty(),
                            it.description.orEmpty()
                        )
                    }
                    binding.imgBox.visibility = View.GONE
                    binding.descImg.visibility = View.GONE
                } else {
                    binding.imgBox.visibility = View.VISIBLE
                    binding.descImg.visibility = View.VISIBLE
                }
                showRecyclerList(list)
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun deleteExperienceApi() {
        binding.progressBar.visibility = View.VISIBLE

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.deleteExperienceByIDRequest(idExp)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is ExperienceResponse) {
                if (response.success) {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    getExperienceApi()
                }
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun showRecyclerList(list: List<ExperienceModel>) {
        binding.rvExperience.layoutManager = LinearLayoutManager(requireContext())
        val listExperienceAdapter = ListExperienceAdapter(list, more)
        binding.rvExperience.adapter = listExperienceAdapter

        listExperienceAdapter.setOnItemClickCallback(object :
            ListExperienceAdapter.OnItemClickCallback {
            override fun onMoreClicked(id_exp: String, position: Int) {
                bottomSheetMore.show()
                idExp = id_exp
                pos = position
            }
            override fun onItemClicked(id: Int) {
                Toast.makeText(requireContext(), list[id].job, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_add -> {
                val intent = Intent(activity, AddExperienceActivity::class.java)
                startActivityForResult(intent, 9013)
                bottomSheetMore.dismiss()
            }
            R.id.btn_delete -> {
                deleteExperienceApi()
                bottomSheetMore.dismiss()
            }
            R.id.btn_edit -> {
                val intent = Intent(activity, EditExperienceActivity::class.java)
                intent.putExtra(EditExperienceActivity.EXTRA_DATA, list[pos])
                startActivityForResult(intent, 9013)
                bottomSheetMore.dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == EditExperienceActivity.UPDATE_DATA) {
            getExperienceApi()
        }
        if (resultCode == Activity.RESULT_OK && requestCode == AddExperienceActivity.ADD_DATA) {
            getExperienceApi()
        }
    }
}
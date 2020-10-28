package com.example.hirejob.home.detailviewpager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hirejob.R
import com.example.hirejob.databinding.FragmentPortfolioBinding
import com.example.hirejob.profile.AddPortfolioActivity
import com.example.hirejob.profile.EditPortfolioActivity
import com.example.hirejob.util.restapi.ApiClient
import com.example.hirejob.util.restapi.portfolio.PortfolioApiService
import com.example.hirejob.util.restapi.portfolio.PortfolioResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottomsheet_profile.view.*
import kotlinx.coroutines.*

class PortfolioFragment(private val id: String?, private val more: Boolean) : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentPortfolioBinding
    private var list = listOf<PortfolioModel>()
    private lateinit var bottomSheetMore: BottomSheetDialog
    private lateinit var service: PortfolioApiService
    private lateinit var coroutineScope: CoroutineScope
    private var idPortfolio = ""
    private var pos = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_portfolio, container, false)
        binding.rvPortfolio.setHasFixedSize(true)

        service = ApiClient.getApiClient(requireContext())!!.create(PortfolioApiService::class.java)
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

        if(!more) {
            binding.btnAdd.visibility = View.GONE
        } else {
            binding.btnAdd.visibility = View.VISIBLE
        }

        bottomSheetMore = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottomsheet_profile, null)
        bottomSheetMore.setContentView(view)

        binding.btnAdd.setOnClickListener(this)
        view.btn_delete.setOnClickListener(this)
        view.btn_edit.setOnClickListener(this)

        getPortfolioApi()

        return binding.root
    }
    private fun getPortfolioApi() {
        binding.progressBar.visibility = View.VISIBLE

        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.getPortofoliobyIDRequest(id)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is PortfolioResponse) {
                if (response.data != null) {
                    list = response.data.map {
                        PortfolioModel(
                            it.id_portofolio.orEmpty(),
                            it.id_account.orEmpty(),
                            it.name.orEmpty(),
                            it.image.orEmpty(),
                            it.description.orEmpty(),
                            it.link_repo.orEmpty(),
                            it.type_portofolio.orEmpty()
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

    private fun deletePortfolioApi() {
        coroutineScope.launch {
            val response = withContext(Dispatchers.IO) {
                try {
                    service.deletePortfolioRequest(idPortfolio)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            if (response is PortfolioResponse) {
                if (response.success) {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                    getPortfolioApi()
                }
            }
        }
    }

    private fun showRecyclerList(list: List<PortfolioModel>) {
        binding.rvPortfolio.layoutManager = LinearLayoutManager(requireContext())
        val listPortfolioAdapter = ListPortfolioAdapter(list, more)
        binding.rvPortfolio.adapter = listPortfolioAdapter

        listPortfolioAdapter.setOnItemClickCallback(object :
            ListPortfolioAdapter.OnItemClickCallback {
            override fun onMoreClicked(id_portfolio: String, position: Int) {
                bottomSheetMore.show()
                idPortfolio = id_portfolio
                pos = position
            }

            override fun onItemClicked(id: Int) {
                Toast.makeText(requireContext(), list[id].name, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_add -> {
                val intent = Intent(activity, AddPortfolioActivity::class.java)
                startActivityForResult(intent, 9013)
                bottomSheetMore.dismiss()
            }
            R.id.btn_delete -> {
                deletePortfolioApi()
                bottomSheetMore.dismiss()
            }
            R.id.btn_edit -> {
                val intent = Intent(activity, EditPortfolioActivity::class.java)
                intent.putExtra(EditPortfolioActivity.EXTRA_DATA, list[pos])
                startActivityForResult(intent, 9013)
                bottomSheetMore.dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == EditPortfolioActivity.UPDATE_DATA) {
            getPortfolioApi()
        }
        if (resultCode == Activity.RESULT_OK && requestCode == AddPortfolioActivity.ADD_DATA) {
            getPortfolioApi()
        }
    }
}
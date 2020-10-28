package com.example.hirejob.home.detailviewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hirejob.R
import com.example.hirejob.databinding.FragmentDetailBinding
import com.example.hirejob.home.FreelancersModel

class DetailFragment(private val data: FreelancersModel?) : Fragment() {
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        binding.apply {
            binding.tvDesc.text = data?.description
            binding.tvNumberphone.text = data?.numberPhone
            binding.tvEmail.text = data?.email
        }

        if (data?.skill != null) {
            val arrSkill = data.skill.split(",")
            showRecyclerGrid(arrSkill)
        } else {
            binding.rvSkill.visibility = View.GONE
            binding.tvDescskill.visibility = View.VISIBLE
        }

        return binding.root
    }

    private fun showRecyclerGrid(list: List<String>) {
        binding.rvSkill.layoutManager = GridLayoutManager(requireContext(), 4)
        val gridSkillAdapter = GridSkillAdapter(list)
        binding.rvSkill.adapter = gridSkillAdapter
    }
}
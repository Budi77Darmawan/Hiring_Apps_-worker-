package com.example.hirejob.home.detailviewpager

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hirejob.R
import com.example.hirejob.databinding.ItemGridSkillBinding

class GridSkillAdapter(private val items: List<String>) :
    RecyclerView.Adapter<GridSkillAdapter.ListViewHolder>() {

    inner class ListViewHolder(val binding: ItemGridSkillBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_grid_skill,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvSkill.text = item
    }
}
package com.example.hirejob.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hirejob.R
import com.example.hirejob.databinding.ItemListSeBinding

class ListFreelancersProjectAdapter(private val items: List<FreelancersProjectModel>) :
    RecyclerView.Adapter<ListFreelancersProjectAdapter.ListViewHolder>() {
    private val baseUrl = "http://18.212.194.218:8080/images/"

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        return ListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list_se,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = items[position]

        holder.binding.tvNameSe.text = item.name_freelancers
        holder.binding.tvJobSe.text = item.job_freelancers

        if (!item.image_freelancers.isNullOrEmpty()) {
            val image = baseUrl + item.image_freelancers
            Glide.with(holder.binding.root)
                    .load(image)
                    .into(holder.binding.imgProfile)
        }

    }

    inner class ListViewHolder(val binding: ItemListSeBinding) :
        RecyclerView.ViewHolder(binding.root)
}
package com.example.hirejob.offers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hirejob.R
import com.example.hirejob.databinding.ItemListOffersBinding

class ListHireProjectAdapter(private val items: List<HireProjectModel>) :
    RecyclerView.Adapter<ListHireProjectAdapter.ListViewHolder>() {
    private val baseUrl = "http://18.212.194.218:80800/images/"

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        return ListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list_offers,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = items[position]

        holder.binding.tvProjectname.text = item.projectName
        val offers = "${item.nameRec} - ${item.company_name}"
        holder.binding.tvNameOffers.text = offers

        when (item.statusConfirm) {
            "-1" -> {
                holder.binding.tvStatus.text = "Rejected"
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_status2)
            }
            "1" -> {
                holder.binding.tvStatus.text = "Accepted"
                holder.binding.tvStatus.setBackgroundResource(R.drawable.bg_status1)
            }
        }
        if (!item.imageProject.isNullOrEmpty()) {
            val image = baseUrl + item.imageProject
            Glide.with(holder.binding.root)
                    .load(image)
                    .into(holder.binding.imgProject)
        }
        holder.binding.cardView.setOnClickListener {
            onItemClickCallback.onItemClicked(position)
        }
    }

    inner class ListViewHolder(val binding: ItemListOffersBinding) :
        RecyclerView.ViewHolder(binding.root)
}
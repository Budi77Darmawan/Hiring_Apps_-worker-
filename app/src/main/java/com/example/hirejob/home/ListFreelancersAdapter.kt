package com.example.hirejob.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hirejob.R
import com.example.hirejob.databinding.ItemListFreelancersBinding

class ListFreelancersAdapter(private val items: List<FreelancersModel>) :
    RecyclerView.Adapter<ListFreelancersAdapter.ListViewHolder>() {
    private val baseUrl = "http://18.212.194.218:8080/images/"

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
                R.layout.item_list_freelancers,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = items[position]

        holder.binding.tvName.text = if (item.name.isNullOrEmpty()) "Full name" else item.name
        holder.binding.tvJob.text = if (item.jobDesc.isNullOrEmpty()) "Job tittle" else item.jobDesc
        val skill = item.skill

        if (skill.isNullOrEmpty()) {
            holder.binding.tvSkill1.visibility = View.GONE
            holder.binding.tvSkill2.visibility = View.GONE
            holder.binding.tvSkill3.visibility = View.GONE
            holder.binding.tvSkill4.visibility = View.GONE
        } else {
            holder.binding.tvSkill1.visibility = View.VISIBLE
            holder.binding.tvSkill2.visibility = View.VISIBLE
            holder.binding.tvSkill3.visibility = View.VISIBLE
            holder.binding.tvSkill4.visibility = View.VISIBLE
            val arrSkill = skill.split(",")
            when (arrSkill.size) {
                3 -> {
                    holder.binding.tvSkill1.text = arrSkill[0]
                    holder.binding.tvSkill2.text = arrSkill[1]
                    holder.binding.tvSkill3.text = arrSkill[2]
                    holder.binding.tvSkill4.visibility = View.GONE
                }
                2 -> {
                    holder.binding.tvSkill1.text = arrSkill[0]
                    holder.binding.tvSkill2.text = arrSkill[1]
                    holder.binding.tvSkill3.visibility = View.GONE
                    holder.binding.tvSkill4.visibility = View.GONE
                }
                1 -> {
                    holder.binding.tvSkill1.text = arrSkill[0]
                    holder.binding.tvSkill2.visibility = View.GONE
                    holder.binding.tvSkill3.visibility = View.GONE
                    holder.binding.tvSkill4.visibility = View.GONE
                }
                else -> {
                    val value = "${arrSkill.size - 3}+"
                    holder.binding.tvSkill1.text = arrSkill[0]
                    holder.binding.tvSkill2.text = arrSkill[1]
                    holder.binding.tvSkill3.text = arrSkill[2]
                    holder.binding.tvSkill4.text = value
                }
            }
        }
        if (!item.image.isNullOrEmpty()) {
            val image = baseUrl + item.image
            Glide.with(holder.binding.root)
                    .load(image)
                    .into(holder.binding.imgFreelancers)
        } else {
            holder.binding.imgFreelancers.setImageResource(R.drawable.ic_profile)
        }
        holder.binding.cardView.setOnClickListener {
            onItemClickCallback.onItemClicked(position)
        }
    }

    inner class ListViewHolder(val binding: ItemListFreelancersBinding) :
        RecyclerView.ViewHolder(binding.root)
}
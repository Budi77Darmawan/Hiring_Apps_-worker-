package com.example.hirejob.home.detailviewpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hirejob.R
import com.example.hirejob.databinding.ItemListExperienceBinding

class ListExperienceAdapter(private val items: List<ExperienceModel>, private val more: Boolean) :
    RecyclerView.Adapter<ListExperienceAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onMoreClicked(id_exp: String, position: Int)
        fun onItemClicked(id: Int)
    }

    inner class ListViewHolder(val binding: ItemListExperienceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list_experience,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = items[position]

        holder.binding.tvNameCompany.text = item.company
        holder.binding.tvJob.text = item.job
        holder.binding.tvJobDate.text = item.start.split("T")[0]
        holder.binding.tvJobDecs.text = item.description

        if (!more) {
            holder.binding.btnMore.visibility = View.GONE
        } else {
            holder.binding.btnMore.visibility = View.VISIBLE
        }

        holder.binding.layout.setOnClickListener {
            onItemClickCallback.onItemClicked(position)
        }
        holder.binding.btnMore.setOnClickListener {
            onItemClickCallback.onMoreClicked(item.id_exp, position)
        }
    }
}
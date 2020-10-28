package com.example.hirejob.home.detailviewpager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hirejob.R
import com.example.hirejob.databinding.ItemListPortfolioBinding

class ListPortfolioAdapter(private val items: List<PortfolioModel>, private val more: Boolean) :
    RecyclerView.Adapter<ListPortfolioAdapter.ListViewHolder>() {
    private val baseUrl = "http://18.212.194.218:8080/images/"

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onMoreClicked(id_portfolio: String, position: Int)
        fun onItemClicked(id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        return ListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_list_portfolio,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = items[position]

        holder.binding.tvName.text = item.name
        holder.binding.tvTypeportfolio.text = item.type_portfolio

        if (!more) {
            holder.binding.btnMore.visibility = View.GONE
        } else {
            holder.binding.btnMore.visibility = View.VISIBLE
        }
        if (!item.image.isNullOrEmpty()) {
            val image = baseUrl + item.image
            Glide.with(holder.binding.root)
                    .load(image)
                    .into(holder.binding.imgPortofolio)
        }
        holder.binding.cardView.setOnClickListener {
            onItemClickCallback.onItemClicked(position)
        }
        holder.binding.btnMore.setOnClickListener {
            onItemClickCallback.onMoreClicked(item.id_portfolio, position)
        }
    }

    inner class ListViewHolder(val binding: ItemListPortfolioBinding) :
        RecyclerView.ViewHolder(binding.root)
}
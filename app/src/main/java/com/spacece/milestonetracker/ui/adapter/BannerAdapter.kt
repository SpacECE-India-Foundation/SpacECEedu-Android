package com.spacece.milestonetracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spacece.milestonetracker.databinding.ItemBannerBinding
import com.spacece.milestonetracker.utils.loadImage

class BannerAdapter() : ListAdapter<Int, BannerAdapter.BannerViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val binding = ItemBannerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BannerViewHolder(
        private val binding: ItemBannerBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(banner: Int) = with(binding) {
            binding.apply {
                ivBanner.loadImage(banner)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Int, newItem: Int) = oldItem == newItem
    }
}



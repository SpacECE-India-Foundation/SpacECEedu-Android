package com.spacece.milestonetracker.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.Review
import com.spacece.milestonetracker.databinding.ItemReviewBinding
import com.spacece.milestonetracker.utils.setupLoadMoreView
import com.spacece.milestonetracker.utils.setupText

class ReviewAdapter(
    private val activity: Activity,
    private val onRetryClick: () -> Unit
) : ListAdapter<Review, ReviewAdapter.SampleViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleViewHolder {
        val binding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SampleViewHolder(binding, activity, onRetryClick)
    }

    override fun onBindViewHolder(holder: SampleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class SampleViewHolder(
        private val binding: ItemReviewBinding,
        private val activity: Activity,
        private val onRetryClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) = with(binding) {
            tvName.setupText(activity.getString(R.string.app_name))
            root.setOnClickListener {
                //TODO: on item click
            }
            layoutLoadMore.setupLoadMoreView(
                isLastItem = review.isLastItem, uiState = review.uiState, onRetryClick
            )
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Review, newItem: Review) = oldItem == newItem
    }
}



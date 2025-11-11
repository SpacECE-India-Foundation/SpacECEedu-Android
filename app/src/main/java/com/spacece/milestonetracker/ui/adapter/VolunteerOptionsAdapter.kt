package com.spacece.milestonetracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.VolunteerOption
import com.spacece.milestonetracker.databinding.ItemVolunteerOptionsBinding
import com.spacece.milestonetracker.utils.setVisibility

class VolunteerOptionsAdapter() :
    ListAdapter<VolunteerOption, VolunteerOptionsAdapter.OptionViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding = ItemVolunteerOptionsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OptionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OptionViewHolder(
        private val binding: ItemVolunteerOptionsBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(volunteerOption: VolunteerOption) = with(binding) {
            binding.apply {
                tvTitle.setText(volunteerOption.title)
                tvDescription.setText(volunteerOption.description)
                tvRnr1.setText(volunteerOption.rnr1)
                tvRnr2.setText(volunteerOption.rnr2)
                tvRnr3.setText(volunteerOption.rnr3)
                tvRnr4.setText(volunteerOption.rnr4)
                tvTime.setText(volunteerOption.time)
                tvPerks1.setText(volunteerOption.perk1)
                tvPerks2.setText(volunteerOption.perk2)
                tvPerks3.setText(volunteerOption.perk3)
                tvPerks4.setText(volunteerOption.perk4)
                llRnr4.setVisibility(volunteerOption.rnr4 != R.string.text_empty)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<VolunteerOption>() {
        override fun areItemsTheSame(oldItem: VolunteerOption, newItem: VolunteerOption) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: VolunteerOption, newItem: VolunteerOption) =
            oldItem == newItem
    }
}



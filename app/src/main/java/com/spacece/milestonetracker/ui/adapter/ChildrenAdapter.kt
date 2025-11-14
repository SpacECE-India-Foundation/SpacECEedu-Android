package com.spacece.milestonetracker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.Child
import com.spacece.milestonetracker.databinding.ItemAddBinding
import com.spacece.milestonetracker.databinding.ItemChildBinding
import com.spacece.milestonetracker.utils.setupText

class ChildrenAdapter(
    private var children: List<Child>,
    private val onChildClick: (Child) -> Unit,
    private val onAddClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_CHILD = 1
    private val VIEW_TYPE_ADD = 2

    override fun getItemViewType(position: Int): Int {
        return if (position < children.size) VIEW_TYPE_CHILD else VIEW_TYPE_ADD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_CHILD) {
            val binding =
                ItemChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChildVH(binding)
        } else {
            val binding = ItemAddBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AddChildVH(binding)
        }
    }

    override fun getItemCount(): Int = children.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChildVH && position < children.size) {
            holder.bind(children[position])
        } else if (holder is AddChildVH) {
            holder.bind()
        }
    }

    fun updateList(newList: List<Child>) {
        children = newList
        notifyDataSetChanged()
    }

    inner class ChildVH(private val binding: ItemChildBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(child: Child) {
            // Load image — use default from model if null
            Glide.with(binding.imgChild.context)
                .load(
                    "https://hustle-7c68d043.mileswebhosting.com/spacece/" + child.image
                ).circleCrop()
                .error(
                    if (child.gender.equals(
                            "male",
                            true
                        )
                    ) R.drawable.boy else R.drawable.girl
                ).into(binding.imgChild)

            binding.tvName.setupText(child.childName)
            // Handle click
            binding.root.setOnClickListener {
                onChildClick(child)
            }
            if (bindingAdapterPosition == 0) {
                binding.root.performClick()
            }
        }
    }

    inner class AddChildVH(private val binding: ItemAddBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.root.setOnClickListener {
                onAddClick()
            }
        }
    }
}

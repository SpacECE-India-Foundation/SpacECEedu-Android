package com.spacece.milestonetracker.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.Chat
import com.spacece.milestonetracker.databinding.ItemChatLeftBinding
import com.spacece.milestonetracker.databinding.ItemChatRightBinding
import com.spacece.milestonetracker.utils.*

class CommunityChatAdapter(
    private val activity: Activity,
    private val onRetryClick: () -> Unit
) : ListAdapter<Chat, RecyclerView.ViewHolder>(DiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).senderId == "101") ChatSender.SELF.value else ChatSender.OTHER.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ChatSender.SELF.value) {
            val binding = ItemChatRightBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ChatRightViewHolder(binding, activity, onRetryClick)
        } else {
            val binding = ItemChatLeftBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ChatLeftViewHolder(binding, activity, onRetryClick)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = getItem(position)
        val previousChat = if (position > 0) getItem(position - 1) else null
        when (holder) {
            is ChatLeftViewHolder -> holder.bind(chat, previousChat)
            is ChatRightViewHolder -> holder.bind(chat, previousChat)
        }
    }

    class ChatLeftViewHolder(
        private val binding: ItemChatLeftBinding,
        private val activity: Activity,
        private val onRetryClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat, previousChat: Chat?) = with(binding) {
            tvName.setupText(chat.senderName)
            tvMessage.setupText(chat.message)
            tvTime.setupText(chat.timestamp.toClockTime())
            tvDate.setupText(chat.timestamp.toCalenderDate())
            ivProfile.loadCircleImage(
                url = chat.senderImage,
                placeholder = R.drawable.ic_profile
            )
            if (chat.senderId == previousChat?.senderId) {
                viewSpace.gone()
                ivProfile.gone()
                tvName.gone()
            }
            if (chat.timestamp.toCalenderDate() == previousChat?.timestamp?.toCalenderDate()) {
                tvDate.gone()
            }
            root.setOnClickListener {
               onRetryClick
            }
        }
    }

    class ChatRightViewHolder(
        private val binding: ItemChatRightBinding,
        private val activity: Activity,
        private val onRetryClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat, previousChat: Chat?) = with(binding) {
            tvMessage.setupText(chat.message)
            tvTime.setupText(chat.timestamp.toClockTime())
            tvDate.setupText(chat.timestamp.toCalenderDate())
            if (chat.timestamp.toCalenderDate() == previousChat?.timestamp?.toCalenderDate()) {
                tvDate.gone()
            }
            root.setOnClickListener {
                //TODO: on item click
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Chat, newItem: Chat) = oldItem == newItem
    }
}



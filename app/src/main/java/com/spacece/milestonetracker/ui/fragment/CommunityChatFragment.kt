package com.spacece.milestonetracker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.Chat
import com.spacece.milestonetracker.databinding.FragmentCommunityChatBinding
import com.spacece.milestonetracker.ui.activity.ParentMainActivity
import com.spacece.milestonetracker.ui.adapter.CommunityChatAdapter
import com.spacece.milestonetracker.ui.base.BaseFragment
import com.spacece.milestonetracker.utils.setOnClickListeners
import com.spacece.milestonetracker.utils.setupRecyclerCache

class CommunityChatFragment : BaseFragment(), OnClickListener {
    private lateinit var binding: FragmentCommunityChatBinding
    private lateinit var communityChatAdapter: CommunityChatAdapter
    private var chatList = arrayListOf<Chat>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewsAndListeners()
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(listOf(ivBack, ivSend))
        communityChatAdapter = CommunityChatAdapter(requireActivity()) {
            //TODO: handle on retry click
        }
        rvChat.adapter = communityChatAdapter
        rvChat.setupRecyclerCache(200)
        chatList.addAll(sampleChats)
        communityChatAdapter.submitList(chatList)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                (activity as ParentMainActivity).onBackPressAction()
            }

            R.id.iv_send -> {
               validateMessageAndSend()
            }
        }
    }

    fun validateMessageAndSend(){
        val newChat = Chat(
            id = System.currentTimeMillis().toString(), // unique id
            senderId = "101",
            senderImage = "https://i.pravatar.cc/150?img=1",
            senderName = "Amit",
            message = binding.edtMessage.text.toString(),
            timestamp = System.currentTimeMillis()
        )

        val updatedList = chatList.toMutableList().apply { add(newChat) }
        chatList = ArrayList(updatedList)

        communityChatAdapter.submitList(updatedList) {
            binding.rvChat.scrollToPosition(updatedList.size - 1)
        }

        binding.edtMessage.text?.clear()
    }
    val sampleChats = listOf(
        // 🔹 Day 1 (Sep 27, 2023)
        Chat("1", "101", "https://i.pravatar.cc/150?img=1", "Amit", "Hey, how are you? All good here, just working on a project.", 1695800000000),
        Chat("2", "102", "https://i.pravatar.cc/150?img=2", "Neha", "I’m good! What about you?", 1695800050000),
        Chat("3", "103", "https://i.pravatar.cc/150?img=3", "Rahul", "Nice! What project is it?", 1695800100000),
        Chat("4", "101", "https://i.pravatar.cc/150?img=1", "Amit", "All good here, just working on a project. All good here, just working on a project.", 1695800150000),
        Chat("5", "101", "https://i.pravatar.cc/150?img=1", "Amit", "Building an Android chat app UI.", 1695800200000),
        Chat("6", "102", "https://i.pravatar.cc/150?img=2", "Neha", "Yes, I’d love to see the design once ready.", 1695800250000),
        Chat("7", "103", "https://i.pravatar.cc/150?img=3", "Rahul", "Let me know if you need testing help.", 1695800300000),
        Chat("8", "103", "https://i.pravatar.cc/150?img=3", "Rahul", "I can test the UI for you.", 1695800350000),
        Chat("9", "101", "https://i.pravatar.cc/150?img=1", "Amit", "Sure! I’ll share screenshots soon. All good here, just working on a project.", 1695800400000),
        Chat("10", "102", "https://i.pravatar.cc/150?img=2", "Neha", "By the way, what tech stack are you using?", 1695800450000),
        Chat("11", "103", "https://i.pravatar.cc/150?img=3", "Rahul", "Mostly Kotlin with Jetpack Compose.", 1695800500000),
        Chat("12", "103", "https://i.pravatar.cc/150?img=3", "Rahul", "Compose makes UI simple!", 1695800550000),
        Chat("13", "101", "https://i.pravatar.cc/150?img=1", "Amit", "Absolutely, love Compose.", 1695800600000),
        Chat("14", "101", "https://i.pravatar.cc/150?img=1", "Amit", "I’ll push the code to GitHub later.", 1695800650000),
        Chat("15", "102", "https://i.pravatar.cc/150?img=2", "Neha", "Looking forward to it!", 1695800700000),
        Chat("16", "102", "https://i.pravatar.cc/150?img=2", "Neha", "Can I help with testing? All good here, just working on a project.", 1695800750000),
        Chat("17", "103", "https://i.pravatar.cc/150?img=3", "Rahul", "Yes, please test the login flow.", 1695800800000),
        Chat("18", "101", "https://i.pravatar.cc/150?img=1", "Amit", "Sure, will do it today.", 1695800850000),

        // 🔹 Day 2 (Sep 28, 2023)
        Chat("19", "101", "https://i.pravatar.cc/150?img=1", "Amit", "Good morning everyone!", 1695886400000),
        Chat("20", "102", "https://i.pravatar.cc/150?img=2", "Neha", "Morning! How’s the progress?", 1695886460000),
        Chat("21", "103", "https://i.pravatar.cc/150?img=3", "Rahul", "Almost done with login flow.", 1695886520000),
        Chat("22", "101", "https://i.pravatar.cc/150?img=1", "Amit", "I’ll integrate Firebase today.", 1695886580000),

        // 🔹 Day 3 (Sep 29, 2023)
        Chat("23", "102", "https://i.pravatar.cc/150?img=2", "Neha", "Firebase config looks good 👍", 1695972800000),
        Chat("24", "103", "https://i.pravatar.cc/150?img=3", "Rahul", "Great! Let’s test notifications.", 1695972860000),
        Chat("25", "101", "https://i.pravatar.cc/150?img=1", "Amit", "I’ll send a test notification.", 1695972920000),
        Chat("26", "102", "https://i.pravatar.cc/150?img=2", "Neha", "Received the push notification 🎉", 1695972980000),

        // 🔹 Day 4 (Sep 30, 2023)
        Chat("27", "103", "https://i.pravatar.cc/150?img=3", "Rahul", "UI polish looks nice!", 1696059200000),
        Chat("28", "101", "https://i.pravatar.cc/150?img=1", "Amit", "Thanks, working on animations now.", 1696059260000),
        Chat("29", "102", "https://i.pravatar.cc/150?img=2", "Neha", "Excited for the demo build 🚀", 1696059320000),
        Chat("30", "103", "https://i.pravatar.cc/150?img=3", "Rahul", "Let’s aim for release next week.", 1696059380000)
    ).sortedByDescending { it.timestamp }

}



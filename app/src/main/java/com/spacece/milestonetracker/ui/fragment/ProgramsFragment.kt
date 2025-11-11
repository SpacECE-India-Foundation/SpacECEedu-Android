package com.spacece.milestonetracker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.databinding.FragmentProgramsBinding
import com.spacece.milestonetracker.ui.activity.ParentMainActivity
import com.spacece.milestonetracker.ui.base.BaseFragment
import com.spacece.milestonetracker.utils.setOnClickListeners

class ProgramsFragment : BaseFragment(), OnClickListener {
    private lateinit var binding: FragmentProgramsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProgramsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewsAndListeners()
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(
            listOf(
                rlCommunityChat, rlCommunityCare,
                rlParentSupport, rlParentReviews,
            )
        )
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.rl_community_chat -> openFragment(R.string.text_community_chat)
            R.id.rl_community_care -> openFragment(R.string.text_community_care)
            R.id.rl_parent_support -> openFragment(R.string.text_parent_support)
            R.id.rl_parent_reviews -> openFragment(R.string.text_parents_reviews)
        }
    }

    private fun openFragment(tabOrFragId: Int) {
        (activity as ParentMainActivity).replaceFragment(tabOrFragId)
    }
}

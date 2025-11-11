package com.spacece.milestonetracker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.databinding.FragmentCommunityCareBinding
import com.spacece.milestonetracker.ui.activity.ParentMainActivity
import com.spacece.milestonetracker.ui.base.BaseFragment
import com.spacece.milestonetracker.utils.setOnClickListeners

class CommunityCareFragment : BaseFragment(), OnClickListener {
    private lateinit var binding: FragmentCommunityCareBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommunityCareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewsAndListeners()
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(listOf(ivBack, btnContact))
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                (activity as ParentMainActivity).onBackPressAction()
            }

            R.id.btn_contact -> {
                (requireActivity() as? ParentMainActivity)?.let {
                    it.binding.bnvMain.selectedItemId = R.id.nav_contact
                }
            }
        }
    }
}

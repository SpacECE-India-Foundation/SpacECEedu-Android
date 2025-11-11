package com.spacece.milestonetracker.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.databinding.ActivityMainAdminBinding
import com.spacece.milestonetracker.ui.base.BaseActivity
import com.spacece.milestonetracker.utils.bindLayout

class AdminMainActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityMainAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindLayout(layoutId = R.layout.activity_main_admin)

        setupViewsAndListeners()
        initViewModelObservers()
    }

    private fun setupViewsAndListeners() = with(binding) {

    }

    private fun initViewModelObservers() {

    }

    override fun onClick(v: View) {

    }
}
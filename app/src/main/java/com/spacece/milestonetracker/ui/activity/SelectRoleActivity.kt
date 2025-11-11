package com.spacece.milestonetracker.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.databinding.ActivitySelectRoleBinding
import com.spacece.milestonetracker.ui.base.BaseActivity
import com.spacece.milestonetracker.utils.*

class SelectRoleActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivitySelectRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindLayout(layoutId = R.layout.activity_select_role)
        setupViewsAndListeners()
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(listOf(llParent, llAdmin, tvContinueAsGuest))
        tvContinueAsGuest.performClick()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_parent -> {
                sharedPrefs.setUserType(UserType.PARENT.value)
                startActivity(LoginActivity::class.java)
            }

            R.id.ll_admin -> {
                sharedPrefs.setUserType(UserType.ADMIN.value)
                startActivity(LoginActivity::class.java)
            }

            R.id.tv_continue_as_guest -> {
                sharedPrefs.setGuestLoggedIn(true)
                sharedPrefs.setUserType(UserType.PARENT.value)
                startActivity(ParentMainActivity::class.java)
                finishAffinity()
            }
        }
    }
}
package com.spacece.milestonetracker.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.LoginRequest
import com.spacece.milestonetracker.databinding.ActivityResetPasswordBinding
import com.spacece.milestonetracker.ui.base.BaseActivity
import com.spacece.milestonetracker.utils.bindLayout
import com.spacece.milestonetracker.utils.setOnClickListeners

class ResetPasswordActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindLayout(layoutId = R.layout.activity_reset_password)
        setupViewsAndListeners()
        initViewModelObservers()
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(listOf(ivBack, btnSave))
    }

    private fun initViewModelObservers() {
        authViewModel.loginResponse.observe(this) { response ->
            response.getContentIfNotHandled()?.let {
                /*if (it.getOrNull()?.status == STATUS_CODE_SUCCESS) {
                    showToast(getString(R.string.text_success))
                    //TODO redirect to new activity
                } else {
                    showToast(getString(R.string.text_failure))
                }*/
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                finish()
            }

            R.id.btn_save-> {
                checkAndVerifyLogin()
            }
        }
    }

    private fun checkAndVerifyLogin() = with(binding) {
        //TODO make fields validation then call api
        authViewModel.login(LoginRequest("username", "password"))
    }
}
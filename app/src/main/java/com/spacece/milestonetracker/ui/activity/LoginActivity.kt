package com.spacece.milestonetracker.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.LoginRequest
import com.spacece.milestonetracker.data.remote.STATUS_SUCCESS
import com.spacece.milestonetracker.databinding.ActivityLoginBinding
import com.spacece.milestonetracker.ui.base.BaseActivity
import com.spacece.milestonetracker.utils.UserType
import com.spacece.milestonetracker.utils.bindLayout
import com.spacece.milestonetracker.utils.clearInputErrorOnTextChangeListeners
import com.spacece.milestonetracker.utils.gone
import com.spacece.milestonetracker.utils.isInternetAvailable
import com.spacece.milestonetracker.utils.isValidEmail
import com.spacece.milestonetracker.utils.setButtonProgress
import com.spacece.milestonetracker.utils.setOnClickListeners
import com.spacece.milestonetracker.utils.setupSpannableText
import com.spacece.milestonetracker.utils.setupText
import com.spacece.milestonetracker.utils.showToast
import com.spacece.milestonetracker.utils.startActivity

class LoginActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindLayout(layoutId = R.layout.activity_login)
        setupViewsAndListeners()
        initViewModelObservers()
    }

    private fun setupViewsAndListeners() = with(binding) {
        if (sharedPrefs.getUserType() == UserType.PARENT.value) {
            tvLoginAs.setupText(getString(R.string.text_login_as_parent))
        } else {
            tvLoginAs.setupText(getString(R.string.text_login_as_admin))
        }
        tvRegister.setupSpannableText(R.string.text_don_t_have_an_account)
        setOnClickListeners(listOf(ivBack, btnLogin, tvRegister, tvForgotPassword, ivGmail))
        clearInputErrorOnTextChangeListeners(listOf(tiEdtEmail, tiEdtPassword))
        progressBar.gone()
    }

    private fun initViewModelObservers() = with(binding) {
        authViewModel.loginResponse.observe(this@LoginActivity) { response ->
            btnLogin.setButtonProgress(progressBar, false)
            response.getContentIfNotHandled()?.let {
                val result = it.getOrNull()
                if (result?.status == STATUS_SUCCESS) {
                    if (sharedPrefs.getUserType() == UserType.PARENT.value) {
                        startActivity(ParentMainActivity::class.java)
                    } else {
                        startActivity(AdminMainActivity::class.java)
                    }
                    finishAffinity()
                } else {
                    showToast(result?.message ?: getString(R.string.text_something_went_wrong))
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                finish()
            }

            R.id.btn_login -> {
                checkAndInitiateLogin()
            }

            R.id.tv_forgot_password -> {
                startActivity(ForgotPasswordActivity::class.java)
            }

            R.id.tv_register -> {
                startActivity(SignupActivity::class.java)
            }
        }
    }

    private fun checkAndInitiateLogin() = with(binding) {
        val email = tiEdtEmail.text?.toString()
        val password = tiEdtPassword.text?.toString()

        if (email.isNullOrEmpty()) {
            tiEdtEmail.error = getString(R.string.text_please_enter_email)
            tiEdtEmail.requestFocus()
        } else if (!email.isValidEmail()) {
            tiEdtEmail.error = getString(R.string.text_invalid_email)
            tiEdtEmail.requestFocus()
        } else if (password.isNullOrEmpty()) {
            tiEdtPassword.error = getString(R.string.text_please_enter_password)
            tiEdtPassword.requestFocus()
        } else {
            if (isInternetAvailable()) {
                btnLogin.setButtonProgress(progressBar, true)
                authViewModel.login(LoginRequest(email, password))
            } else {
                showToast(getString(R.string.text_no_internet))
            }
        }
    }
}
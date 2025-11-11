package com.spacece.milestonetracker.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.SignupRequest
import com.spacece.milestonetracker.data.remote.STATUS_SUCCESS
import com.spacece.milestonetracker.databinding.ActivitySignupBinding
import com.spacece.milestonetracker.ui.base.BaseActivity
import com.spacece.milestonetracker.utils.*

class SignupActivity : BaseActivity(), OnClickListener {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindLayout(layoutId = R.layout.activity_signup)
        setupViewsAndListeners()
        initViewModelObservers()
    }

    private fun setupViewsAndListeners() = with(binding) {
        if (sharedPrefs.getUserType() == UserType.PARENT.value) {
            tvLoginAs.setupText(getString(R.string.text_signup_as_parent))
        } else {
            tvLoginAs.setupText(getString(R.string.text_signup_as_admin))
        }
        tvLogin.setupSpannableText(R.string.text_already_have_an_account)
        setOnClickListeners(listOf(ivBack, btnSignup, tvLogin, ivGmail))
        clearInputErrorOnTextChangeListeners(
            listOf(
                tiEdtName, tiEdtPhone, tiEdtEmail,
                tiEdtPassword, tiEdtConfirmPassword
            )
        )
        progressBar.gone()
    }

    private fun initViewModelObservers() = with(binding) {
        authViewModel.parentRegisterResponse.observe(this@SignupActivity) { response ->
            btnSignup.setButtonProgress(progressBar, false)
            response.getContentIfNotHandled()?.let {
                val result = it.getOrNull()
                if (result?.status == STATUS_SUCCESS) {
                    showToast(result.message ?: getString(R.string.text_success))
                    finish()
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

            R.id.btn_signup -> {
                checkAndInitiateSignup()
            }

            R.id.tv_login -> {
                finish()
            }
        }
    }

    private fun checkAndInitiateSignup() = with(binding) {
        val name = tiEdtName.text?.toString()
        val phone = tiEdtPhone.text?.toString()
        val email = tiEdtEmail.text?.toString()
        val password = tiEdtPassword.text?.toString()
        val confirmPassword = tiEdtConfirmPassword.text?.toString()

        if (name.isNullOrEmpty()) {
            tiEdtName.error = getString(R.string.text_please_enter_name)
            tiEdtName.requestFocus()
        } else if (phone.isNullOrEmpty()) {
            tiEdtPhone.error = getString(R.string.text_please_enter_phone)
            tiEdtPhone.requestFocus()
        } else if (phone.length < 10) {
            tiEdtPhone.error = getString(R.string.text_invalid_phone)
            tiEdtPhone.requestFocus()
        } else if (email.isNullOrEmpty()) {
            tiEdtEmail.error = getString(R.string.text_please_enter_email)
            tiEdtEmail.requestFocus()
        } else if (!email.isValidEmail()) {
            tiEdtEmail.error = getString(R.string.text_invalid_email)
            tiEdtEmail.requestFocus()
        } else if (password.isNullOrEmpty()) {
            tiEdtPassword.error = getString(R.string.text_please_enter_password)
            tiEdtPassword.requestFocus()
        } else if (!password.isStrongPassword()) {
            tiEdtPassword.error = getString(R.string.text_weak_password)
            tiEdtPassword.requestFocus()
        } else if (confirmPassword.isNullOrEmpty()) {
            tiEdtConfirmPassword.error = getString(R.string.text_please_confirm_password)
            tiEdtConfirmPassword.requestFocus()
        }  else if (password != confirmPassword) {
            tiEdtConfirmPassword.error = getString(R.string.text_password_not_matched)
            tiEdtConfirmPassword.requestFocus()
        } else {
            if (isInternetAvailable()) {
                btnSignup.setButtonProgress(progressBar, true)
                authViewModel.signup(
                    SignupRequest(
                        name, phone, email, password,
                    )
                )
            } else {
                showToast(getString(R.string.text_no_internet))
            }
        }
    }
}
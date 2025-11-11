package com.spacece.milestonetracker.ui.base

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.spacece.milestonetracker.data.local.SharedPrefs
import com.spacece.milestonetracker.ui.activity.StartupActivity
import com.spacece.milestonetracker.viewModel.vmHelper.getViewModel
import com.spacece.milestonetracker.utils.startActivity
import com.spacece.milestonetracker.viewModel.AuthViewModel
import com.spacece.milestonetracker.viewModel.UserViewModel

open class BaseActivity : AppCompatActivity() {
    private var backPressCallback: OnBackPressedCallback? = null
    protected lateinit var sharedPrefs: SharedPrefs
    protected lateinit var authViewModel: AuthViewModel
    protected lateinit var userViewModel: UserViewModel

    protected var isLoading = false
    protected var pageNo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefs = SharedPrefs(this)
        authViewModel = getViewModel { AuthViewModel(this) }
        userViewModel = getViewModel { UserViewModel(this) }

        setupBackPressHandler()
    }

    private fun setupBackPressHandler() {
        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressAction()
            }
        }
        backPressCallback?.let {
            onBackPressedDispatcher.addCallback(this, it)
        }
    }

    open fun onBackPressAction() {
        if (!isTaskRoot) finish()
        else moveTaskToBack(true)
    }

    fun initiateLogout() {
        authViewModel.logout()
        startActivity(StartupActivity::class.java)
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (backPressCallback != null)
            backPressCallback?.remove()
    }
}
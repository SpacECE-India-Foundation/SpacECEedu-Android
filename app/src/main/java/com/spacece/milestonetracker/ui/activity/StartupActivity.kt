package com.spacece.milestonetracker.ui.activity

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.databinding.ActivityStartupBinding
import com.spacece.milestonetracker.ui.base.BaseActivity
import com.spacece.milestonetracker.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StartupActivity : BaseActivity() {
    private lateinit var binding: ActivityStartupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = bindLayout(layoutId = R.layout.activity_startup)

        checkAndProceedNext()
    }

    private fun checkAndProceedNext() {
        lifecycleScope.launch {
            delay(1000)
            if (sharedPrefs.isUserLoggedIn() || sharedPrefs.isGuestLoggedIn()) {
                if (sharedPrefs.getUserType() == UserType.PARENT.value) {
                    startActivity(ParentMainActivity::class.java)
                } else {
                    startActivity(AdminMainActivity::class.java)
                }
            } else {
                startActivity(SelectRoleActivity::class.java)
            }
            finish()
        }
    }
}
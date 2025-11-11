package com.spacece.milestonetracker.ui.activity

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import com.razorpay.PaymentResultListener
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.local.AppModule.aapDataBase
import com.spacece.milestonetracker.data.model.User
import com.spacece.milestonetracker.databinding.ActivityMainParentBinding
import com.spacece.milestonetracker.ui.base.BaseActivity
import com.spacece.milestonetracker.ui.fragment.*
import com.spacece.milestonetracker.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ParentMainActivity : BaseActivity(), OnClickListener, PaymentResultListener {
    lateinit var binding: ActivityMainParentBinding
    private var selectedTabOrFragId = R.id.nav_home
    private val selectedTab = "selected_tab"
    private val selectedFrag = "selected_frag"


    companion object {
        var user: User? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindLayout(layoutId = R.layout.activity_main_parent)
        binding.bnvMain.selectedItemId = savedInstanceState?.getInt(selectedTab) ?: R.id.nav_home
        replaceFragment(savedInstanceState?.getInt(selectedFrag) ?: R.id.nav_home)

        setupViewsAndListeners()
        initViewModelObservers()
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(
            listOf(
                ivMenu, layoutDrawer.tvProfile, layoutDrawer.tvAboutUs,
                layoutDrawer.tvPrivacyPolicy, layoutDrawer.tvTnc,
                layoutDrawer.tvHelpSupport, layoutDrawer.rlBtnAuth
            )
        )
        bnvMain.setOnItemSelectedListener { item ->
            replaceFragment(item.itemId)
            true
        }
        layoutDrawer.apply {
            if (sharedPrefs.isUserLoggedIn()) {
                tvAuth.setupText(getString(R.string.text_logout))
            } else {
                tvAuth.setupText(getString(R.string.text_login))
            }
            ivAuth.loadImage(
                if (sharedPrefs.isUserLoggedIn())
                    R.drawable.ic_auth else R.drawable.ic_user
            )
        }
    }

    private fun initViewModelObservers() {
        /*userViewModel.getProfileResponse.observe(this) { response ->
            response.getContentIfNotHandled()?.let {
                if (it.getOrNull()?.status == ApiConstants.STATUS_CODE_SUCCESS) {
                    setupUserDetails()
                }
            }
        }*/
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_menu -> binding.drawerLayout.openDrawer(GravityCompat.START)

            R.id.tv_profile -> {
                if (sharedPrefs.isUserLoggedIn()) {
                    binding.bnvMain.selectedItemId = R.id.nav_home
                    replaceFragment(R.string.text_my_profile)
                } else {
                    startActivity(LoginActivity::class.java)
                }
            }

            R.id.tv_about_us -> startActivity(
                WebViewActivity::class.java,
                WEB_VIEW_TITLE to getString(R.string.text_about_us),
                WEB_VIEW_URL to ABOUT_US_URL,
            )

            R.id.tv_privacy_policy -> startActivity(
                WebViewActivity::class.java,
                WEB_VIEW_TITLE to getString(R.string.text_privacy_policy),
                WEB_VIEW_URL to PRIVACY_POLICY_URL,
            )

            R.id.tv_tnc -> startActivity(
                WebViewActivity::class.java,
                WEB_VIEW_TITLE to getString(R.string.text_terms_n_conditions),
                WEB_VIEW_URL to TERMS_AND_CONDITIONS_URL,
            )

            R.id.tv_help_support -> binding.bnvMain.selectedItemId = R.id.nav_contact

            R.id.rl_btn_auth -> {
                if (sharedPrefs.isUserLoggedIn()) {
                    initiateLogout()
                } else {
                    startActivity(LoginActivity::class.java)
                }
            }
        }
        if (v.id != R.id.iv_menu) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    private fun setupUserDetails() = with(binding.layoutDrawer) {
        if (sharedPrefs.isUserLoggedIn()) {
            lifecycleScope.launch {
                user = userViewModel.getUserDetails()
                withContext(Dispatchers.Main) {
                    tvName.setupText(user?.current_user_name)
                    ivProfile.loadImage(user?.current_user_image, R.drawable.ic_user)
                }
            }
        }
    }

    fun replaceFragment(tabOrFragId: Int) {
        selectedTabOrFragId = tabOrFragId
        binding.llHeader.setVisibility(
            !(isHomeChildFragSelected() || isProgramsChildFragSelected())
        )

        val tag = when (tabOrFragId) {
            R.string.text_my_profile -> {
                ProfileFragment::class.java.simpleName
            }

            R.string.text_milestone_tracker -> {
                MileStoneTrackerFragment::class.java.simpleName
            }

            R.string.text_add_child -> {
                AddChildFragment::class.java.simpleName
            }

            R.string.text_join_us_as_volunteer -> {
                VolunteerFragment::class.java.simpleName
            }

            R.id.nav_programs -> {
                binding.tvTitle.setupText(getString(R.string.text_programs))
                ProgramsFragment::class.java.simpleName
            }

            R.string.text_community_chat -> {
                CommunityChatFragment::class.java.simpleName
            }

            R.string.text_community_care -> {
                CommunityCareFragment::class.java.simpleName
            }

            R.string.text_parent_support -> {
                ParentSupportFragment::class.java.simpleName
            }

            R.string.text_parents_reviews -> {
                ParentReviewsFragment::class.java.simpleName
            }

            R.id.nav_support -> {
                binding.tvTitle.setupText(getString(R.string.text_support_us))
                SupportFragment::class.java.simpleName
            }

            R.id.nav_contact -> {
                binding.tvTitle.setupText(getString(R.string.text_contact_us))
                ContactFragment::class.java.simpleName
            }

            else -> {
                binding.tvTitle.setupText(getString(R.string.app_name))
                HomeFragment::class.java.simpleName
            }
        }

        val transaction = supportFragmentManager.beginTransaction()

        supportFragmentManager.fragments.forEach { fragment ->
            transaction.hide(fragment)
        }

        var fragment = supportFragmentManager.findFragmentByTag(tag)

        if (fragment == null) {
            fragment = when (tabOrFragId) {
                R.string.text_my_profile -> ProfileFragment()
                R.string.text_milestone_tracker -> MileStoneTrackerFragment()
                R.string.text_add_child -> AddChildFragment()
                R.string.text_join_us_as_volunteer -> VolunteerFragment()
                R.id.nav_programs -> ProgramsFragment()
                R.string.text_community_chat -> CommunityChatFragment()
                R.string.text_community_care -> CommunityCareFragment()
                R.string.text_parent_support -> ParentSupportFragment()
                R.string.text_parents_reviews -> ParentReviewsFragment()
                R.id.nav_support -> SupportFragment()
                R.id.nav_contact -> ContactFragment()
                else -> HomeFragment()
            }
            transaction.add(R.id.fl_fragment_container, fragment, tag)
        } else {
            transaction.show(fragment)
        }

        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        //userViewModel.getProfile()
        setupUserDetails()
    }

    override fun onBackPressAction() = with(binding) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (isHomeChildFragSelected()) {
            replaceFragment(R.id.nav_home)
        } else if (isProgramsChildFragSelected()) {
            replaceFragment(R.id.nav_programs)
        } else if (bnvMain.selectedItemId != R.id.nav_home) {
            bnvMain.menu.findItem(R.id.nav_home).isChecked = true
            replaceFragment(R.id.nav_home)
        } else super.onBackPressAction()
    }

    private fun isHomeChildFragSelected(): Boolean {
        return when (selectedTabOrFragId) {
            R.string.text_my_profile -> true
            R.string.text_milestone_tracker -> true
            R.string.text_add_child -> true
            R.string.text_join_us_as_volunteer -> true
            else -> false
        }
    }

    private fun isProgramsChildFragSelected(): Boolean {
        return when (selectedTabOrFragId) {
            R.string.text_community_chat,
            R.string.text_community_care,
            R.string.text_parent_support,
            R.string.text_parents_reviews -> true

            else -> false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(selectedTab, binding.bnvMain.selectedItemId)
        outState.putInt(selectedFrag, selectedTabOrFragId)
    }

    override fun onPaymentSuccess(razorpayPaymentID: String) {
        (supportFragmentManager.findFragmentByTag(SupportFragment::class.java.simpleName) as? SupportFragment)
            ?.onPaymentSuccessFromActivity(razorpayPaymentID)
    }

    override fun onPaymentError(code: Int, description: String?) {
        (supportFragmentManager.findFragmentByTag(SupportFragment::class.java.simpleName) as? SupportFragment)
            ?.onPaymentErrorFromActivity(code, description)
    }
}
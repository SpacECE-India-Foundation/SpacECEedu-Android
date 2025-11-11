package com.spacece.milestonetracker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.User
import com.spacece.milestonetracker.databinding.FragmentProfileBinding
import com.spacece.milestonetracker.ui.activity.ParentMainActivity
import com.spacece.milestonetracker.ui.base.BaseFragment
import com.spacece.milestonetracker.utils.loadImage
import com.spacece.milestonetracker.utils.setOnClickListeners
import com.spacece.milestonetracker.utils.setupText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : BaseFragment(), OnClickListener {
    private lateinit var binding: FragmentProfileBinding
    private var user: User? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewsAndListeners()
        setupUserDetails()
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(listOf(ivBack,rlBtnAuth))
    }

    private fun setupUserDetails() = with(binding) {
        if (sharedPrefs.isUserLoggedIn()) {
            lifecycleScope.launch {
                user = userViewModel.getUserDetails()
                withContext(Dispatchers.Main) {
                    ivUser.loadImage(user?.current_user_image, R.drawable.ic_user)
                    tvType.setupText(
                        user?.current_user_type?.lowercase()?.replaceFirstChar { it.uppercase() })
                    tiEdtName.setupText(user?.current_user_name)
                    tiEdtEmail.setupText(user?.current_user_email)
                    tiEdtPhone.setupText(user?.current_user_mob)
                }
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                (activity as ParentMainActivity).onBackPressAction()
            }

            R.id.rl_btn_auth -> {
                if (sharedPrefs.isUserLoggedIn()) {
                    (activity as ParentMainActivity).initiateLogout()
                }
            }
        }
    }

}

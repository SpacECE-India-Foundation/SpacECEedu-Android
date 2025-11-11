package com.spacece.milestonetracker.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.spacece.milestonetracker.data.local.SharedPrefs
import com.spacece.milestonetracker.viewModel.AuthViewModel
import com.spacece.milestonetracker.viewModel.MilestoneTrackerViewModel
import com.spacece.milestonetracker.viewModel.UserViewModel
import com.spacece.milestonetracker.viewModel.vmHelper.getViewModel

open class BaseFragment : Fragment() {
    protected lateinit var authViewModel: AuthViewModel
    protected lateinit var userViewModel: UserViewModel
    protected lateinit var milestoneTrackerViewModel: MilestoneTrackerViewModel

    lateinit var sharedPrefs: SharedPrefs

    protected var isLoading = false
    protected var pageNo = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPrefs = SharedPrefs(requireActivity())
        authViewModel = getViewModel { AuthViewModel(requireActivity()) }
        userViewModel = getViewModel { UserViewModel(requireActivity()) }
        milestoneTrackerViewModel = getViewModel { MilestoneTrackerViewModel(requireActivity()) }
    }
}
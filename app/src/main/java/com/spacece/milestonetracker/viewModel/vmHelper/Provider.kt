package com.spacece.milestonetracker.viewModel.vmHelper

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(noinline creator: () -> T): T {
    return ViewModelProvider(this, object : ViewModelProvider.Factory {
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
            @Suppress("UNCHECKED_CAST")
            return creator() as VM
        }
    })[T::class.java]
}

inline fun <reified T : ViewModel> Fragment.getViewModel(noinline creator: () -> T): T {
    return ViewModelProvider(this, object : ViewModelProvider.Factory {
        override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
            @Suppress("UNCHECKED_CAST")
            return creator() as VM
        }
    })[T::class.java]
}

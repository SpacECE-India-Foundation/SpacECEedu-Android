package com.spacece.milestonetracker.viewModel.vmHelper

class Event<out T>(private val content: T) {
    private var hasBeenHandled = false
    fun getContentIfNotHandled(): T? =
        if (hasBeenHandled) null else {
            hasBeenHandled = true
            content
        }
    //fun peekContent(): T = content
}
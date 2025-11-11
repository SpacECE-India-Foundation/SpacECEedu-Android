package com.spacece.milestonetracker.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.databinding.LayoutLoadMoreBinding

fun View.setVisibility(condition: Boolean) {
    this.visibility = if (condition) View.VISIBLE else View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun TextView.setupText(text: String?) {
    this.text = text ?: ""
}

fun TextView.setupSpannableText(stringId: Int) {
    this.text = context.getSpannableString(context.getString(stringId))
}

fun ImageView.loadImage(drawable: Int) {
    try {
        this.setImageDrawable(AppCompatResources.getDrawable(this.context, drawable))
    } catch (_: Exception) {
    }
}

fun ImageView.loadImage(url: String?, placeholder: Int) {
    try {
        Glide.with(this.context.applicationContext)
            .load(url).placeholder(placeholder)
            .error(placeholder).into(this)
    } catch (_: Exception) {
    }
}

fun ImageView.loadCircleImage(url: String?, placeholder: Int) {
    try {
        Glide.with(this.context.applicationContext)
            .load(url).placeholder(placeholder)
            .error(placeholder).circleCrop().into(this)
    } catch (_: Exception) {
    }
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message.cleanText(), Toast.LENGTH_SHORT).show()
}


fun AppCompatButton.setButtonProgress(progressBar: ProgressBar, isLoading: Boolean) {
    if (isLoading) this.setTextColor(this.context.getColor(android.R.color.transparent))
    else this.setTextColor(this.context.getColor(android.R.color.white))
    this.isClickable = !isLoading
    progressBar.setVisibility(isLoading)
}

fun LayoutLoadMoreBinding.setupLoadMoreView(
    isLastItem: Boolean, uiState: UIState, onRetryClick: () -> Unit,
) {
    val context = root.context
    if (!isLastItem) this.llLoadMore.gone()
    else {
        this.llLoadMore.visible()
        tvLoadMore.gone()
        progressBar.gone()
        btnRetry.gone()
        when (uiState) {
            UIState.DataView -> {
                btnRetry.visible()
                btnRetry.setupText(context.getString(R.string.text_load_more))
            }

            UIState.LoadingMore -> {
                progressBar.visible()
            }

            UIState.NoMoreData -> {
                tvLoadMore.visible()
                tvLoadMore.setupText(context.getString(R.string.text_you_have_seen_all))
            }

            else -> {
                tvLoadMore.visible()
                btnRetry.visible()
                tvLoadMore.setupText(context.getString(R.string.text_failed_to_load_more))
            }
        }
    }
    btnRetry.setOnClickListener {
        onRetryClick.invoke()
    }
}

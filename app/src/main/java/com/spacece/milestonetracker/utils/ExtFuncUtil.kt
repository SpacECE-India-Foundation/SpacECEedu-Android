package com.spacece.milestonetracker.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.util.Patterns
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.spacece.milestonetracker.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

inline fun <reified T : ViewDataBinding> AppCompatActivity.bindLayout(layoutId: Int): T {
    return DataBindingUtil.setContentView(this, layoutId)
}

fun RecyclerView.setupRecyclerCache(size: Int) {
    this.setItemViewCacheSize(size)
    this.itemAnimator = null
}

fun Context.startActivity(activityClass: Class<*>) {
    startActivity(Intent(this, activityClass))
}

fun Context.startActivity(activityClass: Class<*>, vararg extras: Pair<String, Any>) {
    val intent = Intent(this, activityClass)
    extras.forEach { (key, value) ->
        when (value) {
            is Int -> intent.putExtra(key, value)
            is Long -> intent.putExtra(key, value)
            is Float -> intent.putExtra(key, value)
            is Double -> intent.putExtra(key, value)
            is Boolean -> intent.putExtra(key, value)
            is String -> intent.putExtra(key, value)
        }
    }
    startActivity(intent)
}

fun View.OnClickListener.setOnClickListeners(viewsList: List<View>) {
    viewsList.forEach { view -> view.setOnClickListener(this) }
}

fun clearInputErrorOnTextChangeListeners(edtFields: List<EditText>) {
    edtFields.forEach { editText ->
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                editText.error = null
            }

            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int, count: Int
            ) {
            }
        })
    }
}

fun String.cleanText(): String {
    return this.replace(STRING_TOKEN_C1, "")
        .replace(STRING_TOKEN_C2, "")
}

fun Context.getSpannableString(string: String): SpannableString {
    try {
        val color1 = getColor(R.color.color_gray_dark)
        val color2 = getColor(R.color.color_secondary)

        val cleanStringBuilder = StringBuilder()

        data class SpanInfo(val start: Int, val end: Int, val color: Int, val isC2: Boolean)

        val spans = mutableListOf<SpanInfo>()

        var i = 0
        var cleanIndex = 0
        while (i < string.length) {
            if (i + 3 <= string.length && string.substring(i, i + 3) == STRING_TOKEN_C1) {
                i += 3
                val endTag = string.indexOf(STRING_TOKEN_C1, i)
                if (endTag == -1) {
                    cleanStringBuilder.append(string.substring(i))
                    break
                }
                val text = string.substring(i, endTag)
                cleanStringBuilder.append(text)
                spans.add(SpanInfo(cleanIndex, cleanIndex + text.length, color1, false))
                cleanIndex += text.length
                i = endTag + 3
            } else if (i + 3 <= string.length && string.substring(
                    i,
                    i + 3
                ) == STRING_TOKEN_C2
            ) {
                i += 3
                val endTag = string.indexOf(STRING_TOKEN_C2, i)
                if (endTag == -1) {
                    cleanStringBuilder.append(string.substring(i))
                    break
                }
                val text = string.substring(i, endTag)
                cleanStringBuilder.append(text)
                spans.add(SpanInfo(cleanIndex, cleanIndex + text.length, color2, true))
                cleanIndex += text.length
                i = endTag + 3
            } else {
                cleanStringBuilder.append(string[i])
                cleanIndex++
                i++
            }
        }

        val spannable = SpannableString(cleanStringBuilder.toString())

        spans.forEach { span ->
            if (span.start < spannable.length && span.end <= spannable.length) {
                // Color
                spannable.setSpan(
                    ForegroundColorSpan(span.color),
                    span.start,
                    span.end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                if (span.isC2) {
                    // Bold
                    spannable.setSpan(
                        StyleSpan(Typeface.BOLD),
                        span.start,
                        span.end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    // Underline
                    spannable.setSpan(
                        UnderlineSpan(),
                        span.start,
                        span.end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }
        }
        return spannable

    } catch (_: Exception) {
        return SpannableString(string.cleanText())
    }
}

fun Context.share(string: String) {
    try {
        startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, string)
        }, "Share App"))
    } catch (_: Exception) {
    }
}

fun Context.openPlayStore() {
    val appPackageName = packageName
    try {
        startActivity(Intent(Intent.ACTION_VIEW, "market://details?id=$appPackageName".toUri()))
    } catch (_: Exception) {
        startActivity(Intent(Intent.ACTION_VIEW, PLAY_STORE_URL.toUri()))
    }
}

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

fun Context.openInBrowser(url: String?) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, url?.toUri()))
    } catch (_: Exception) {
    }
}

fun Context.openWhatsAppSupport() {
    try {
        val uri = "https://wa.me/${getString(R.string.text_spacece_phone)}".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.whatsapp")
        startActivity(intent)
    } catch (_: Exception) {
    }
}

fun Context.openCallSupport() {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:${getString(R.string.text_spacece_phone).takeLast(10)}".toUri()
        }
        val chooser = Intent.createChooser(intent, "Choose Dialer")
        startActivity(chooser)
    } catch (_: Exception) {
    }
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.openEmailSupport() {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:${getString(R.string.text_spacece_email)}".toUri()
        }
        startActivity(intent)
    } catch (_: Exception) {
    }
}

fun Context.openMapSearch() {
    try {
        val gmmIntentUri = "geo:0,0?q=${Uri.encode(MAP_LOCATION)}".toUri()
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps") // Optional: Force Google Maps
        }
        val chooser = Intent.createChooser(mapIntent, "Open with Maps")
        startActivity(chooser)
    } catch (_: Exception) {
    }
}

fun Long.toCalenderDate(): String {
    val sdf = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
    return sdf.format(Date(this))
}

fun Long.toClockTime(): String {
    val sdf = SimpleDateFormat("hh:mma", Locale.getDefault())
    return sdf.format(Date(this))
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isStrongPassword(): Boolean {
    return Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")
        .matches(this)
}

fun Int.dpValue(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}
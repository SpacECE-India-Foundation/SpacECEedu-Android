package com.spacece.milestonetracker.utils

import android.app.Activity
import android.widget.Toast
import com.razorpay.Checkout

import org.json.JSONObject

class PaymentHelper(private val activity: Activity) {

    fun startPayment(amount: Int, name: String, email: String, phone: String) {
        val checkout = Checkout()
        checkout.setKeyID("rzp_test_KQpgNv8PbMeQk1")

        try {
            val options = JSONObject()
            options.put("name", "SpacECEedu")
            options.put("description", "Donation Support")
            options.put("image", "http://example.com/image/rzp.jpg")
            options.put("theme.color", "#EAAE15")
            options.put("currency", "INR")
            options.put("amount", amount * 100)
            options.put("prefill.email", email)
            options.put("prefill.contact", phone)

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 3)
            options.put("retry", retryObj)

            checkout.open(activity, options)

        } catch (e: Exception) {
            Toast.makeText(activity, "Payment failed to start", Toast.LENGTH_SHORT).show()
        }
    }

}

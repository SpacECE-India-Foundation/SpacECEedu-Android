package com.spacece.milestonetracker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.widget.doOnTextChanged
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.databinding.FragmentSupportBinding
import com.spacece.milestonetracker.ui.base.BaseFragment
import com.spacece.milestonetracker.utils.PaymentHelper
import com.spacece.milestonetracker.utils.clearInputErrorOnTextChangeListeners
import com.spacece.milestonetracker.utils.gone
import com.spacece.milestonetracker.utils.isInternetAvailable
import com.spacece.milestonetracker.utils.isValidEmail
import com.spacece.milestonetracker.utils.setButtonProgress
import com.spacece.milestonetracker.utils.setOnClickListeners
import com.spacece.milestonetracker.utils.setupText
import com.spacece.milestonetracker.utils.showToast

class SupportFragment : BaseFragment(), OnClickListener{
    private lateinit var binding: FragmentSupportBinding
    private lateinit var paymentHelper: PaymentHelper

    private val allowedValues = listOf("5000", "10000", "15000", "20000")
    private var toggleButtons = emptyList<ToggleButton>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSupportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        paymentHelper = PaymentHelper(requireActivity())
        setupViewsAndListeners()
    }

    private fun setupViewsAndListeners() = with(binding) {
        toggleButtons = listOf(tb1, tb2, tb3, tb4)
        progressBar.gone()
        setOnClickListeners(listOf(btnPay) + toggleButtons)
        clearInputErrorOnTextChangeListeners(listOf(edtAmount, edtName, edtEmail, edtPhone))
        edtAmount.doOnTextChanged { text, _, _, _ ->
            if (text.toString() !in allowedValues) {
                resetButtons(null)
            }
        }
    }

    override fun onClick(v: View) {
        binding.apply {
            when (v) {
                in toggleButtons -> {
                    resetButtons(v as ToggleButton)
                    edtAmount.setupText(allowedValues[toggleButtons.indexOf(v)])
                }

                btnPay -> {
                    validateAndProceedToPay()
                }
            }
        }
    }

    private fun resetButtons(selectedButton: ToggleButton?) {
        toggleButtons.forEach {
            it.isChecked = it == selectedButton
        }
    }

    private fun validateAndProceedToPay() = with(binding) {
        val amount = edtAmount.text?.toString()?.toIntOrNull()
        val name = edtName.text?.toString()?.trim()
        val email = edtEmail.text?.toString()?.trim()
        val phone = edtPhone.text?.toString()?.trim()

        if (amount == null || amount <= 0) {
            edtAmount.error = getString(R.string.text_please_enter_valid_amount)
            edtAmount.requestFocus()
        } else if (name.isNullOrEmpty()) {
            edtName.error = getString(R.string.text_please_enter_name)
            edtName.requestFocus()
        } else if (email.isNullOrEmpty()) {
            edtEmail.error = getString(R.string.text_please_enter_email)
            edtEmail.requestFocus()
        } else if (!email.isValidEmail()) {
            edtEmail.error = getString(R.string.text_invalid_email)
            edtEmail.requestFocus()
        } else if (phone.isNullOrEmpty()) {
            edtPhone.error = getString(R.string.text_please_enter_phone)
            edtPhone.requestFocus()
        } else if (phone.length < 10) {
            edtPhone.error = getString(R.string.text_invalid_phone)
            edtPhone.requestFocus()
        } else {
            if (requireActivity().isInternetAvailable()) {
                btnPay.setButtonProgress(progressBar, true)
                paymentHelper.startPayment(amount, name, email, phone)
            } else {
                requireActivity().showToast(getString(R.string.text_no_internet))
            }
        }
    }

    fun onPaymentSuccessFromActivity(paymentId: String) {
        binding.btnPay.setButtonProgress(binding.progressBar, false)
        Toast.makeText(requireContext(), "Payment Success: $paymentId", Toast.LENGTH_SHORT).show()
    }

    fun onPaymentErrorFromActivity(code: Int, description: String?) {
        binding.btnPay.setButtonProgress(binding.progressBar, false)
        Toast.makeText(requireContext(), "Payment Failed: $description", Toast.LENGTH_SHORT).show()
    }
}

package com.spacece.milestonetracker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.databinding.FragmentContactBinding
import com.spacece.milestonetracker.ui.base.BaseFragment
import com.spacece.milestonetracker.utils.*

class ContactFragment : BaseFragment(), OnClickListener {
    private lateinit var binding: FragmentContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewsAndListeners()
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(
            listOf(
                llEmail, llPhone, llLocate, llVisit, btnSend, llInstagram,
                llFaceboook, llWhatsapp, llLinkedin, llTwitter, llYoutube
            )
        )
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ll_email -> {
                requireContext().openEmailSupport()
            }

            R.id.ll_phone -> {
                requireContext().openCallSupport()
            }

            R.id.ll_locate -> {
                requireContext().openMapSearch()
            }

            R.id.ll_visit -> {
                requireContext().openInBrowser(getString(R.string.text_spacece_website))
            }

            R.id.ll_instagram -> {
                requireContext().openInBrowser(INSTAGRAM_URL)
            }

            R.id.ll_faceboook -> {
                requireContext().openInBrowser(FACEBOOK_URL)
            }

            R.id.ll_whatsapp -> {
                requireContext().openWhatsAppSupport()
            }

            R.id.ll_linkedin -> {
                requireContext().openInBrowser(LINKEDIN_URL)
            }

            R.id.ll_twitter -> {
                requireContext().openInBrowser(TWITTER_URL)
            }

            R.id.ll_youtube -> {
                requireContext().openInBrowser(YOUTUBE_URL)
            }
        }
    }
}

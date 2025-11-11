package com.spacece.milestonetracker.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.VolunteerOption
import com.spacece.milestonetracker.databinding.FragmentVolunteerBinding
import com.spacece.milestonetracker.ui.activity.ParentMainActivity
import com.spacece.milestonetracker.ui.adapter.VolunteerOptionsAdapter
import com.spacece.milestonetracker.ui.base.BaseFragment
import com.spacece.milestonetracker.utils.*

class VolunteerFragment : BaseFragment(), OnClickListener {
    private lateinit var binding: FragmentVolunteerBinding
    val volunteerOptionList = listOf(
        VolunteerOption(
            title = R.string.text_fundraising_volunteer,
            description = R.string.text_support_education_healthcare,
            rnr1 = R.string.text_events,
            rnr2 = R.string.text_donor_outreach,
            rnr3 = R.string.text_social_media,
            rnr4 = R.string.text_relations,
            time = R.string.text_flexible_few_hours,
            perk1 = R.string.text_skills,
            perk2 = R.string.text_networking,
            perk3 = R.string.text_certificate,
            perk4 = R.string.text_make_an_impact
        ),
        VolunteerOption(
            title = R.string.text_teaching_assistant,
            description = R.string.text_support_learning_afterschool,
            rnr1 = R.string.text_homework,
            rnr2 = R.string.text_reading,
            rnr3 = R.string.text_math_english_science,
            rnr4 = R.string.text_group_support,
            time = R.string.text_hours_per_week_flexible,
            perk1 = R.string.text_teaching_mentoring_skills,
            perk2 = R.string.text_confidence,
            perk3 = R.string.text_certificate,
            perk4 = R.string.text_direct_impact
        ),
        VolunteerOption(
            title = R.string.text_event_organizer,
            description = R.string.text_plan_manage_events,
            rnr1 = R.string.text_logistics,
            rnr2 = R.string.text_volunteer_vendor_coordination,
            rnr3 = R.string.text_execution_reporting,
            rnr4 = R.string.text_empty,
            time = R.string.text_event_based_weekends,
            perk1 = R.string.text_event_management_experience,
            perk2 = R.string.text_leadership,
            perk3 = R.string.text_networking,
            perk4 = R.string.text_appreciation
        ),
        VolunteerOption(
            title = R.string.text_social_media_volunteer,
            description = R.string.text_promote_ngos_mission,
            rnr1 = R.string.text_content_creation,
            rnr2 = R.string.text_campaigns,
            rnr3 = R.string.text_engagement,
            rnr4 = R.string.text_awareness,
            time = R.string.text_flexible_remote_possible,
            perk1 = R.string.text_digital_marketing_skills,
            perk2 = R.string.text_creativity,
            perk3 = R.string.text_certificate,
            perk4 = R.string.text_amplify_impact
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVolunteerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewsAndListeners()
        setupVolunteerOptions()
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(listOf(ivBack,btnContact))
    }

    private fun setupVolunteerOptions() = with(binding) {
        vpOptions.adapter = VolunteerOptionsAdapter().apply {
            submitList(volunteerOptionList)
        }

        ciOptions.setViewPager(vpOptions)
        ciOptions.tintIndicator(requireContext().getColor(R.color.color_yellow))

        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            var position = 0
            override fun run() {
                if (position == volunteerOptionList.size) position = 0
                vpOptions.currentItem = position++
                handler.postDelayed(this, 3000)
            }
        }
        handler.post(runnable)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                (activity as ParentMainActivity).onBackPressAction()
            }

            R.id.btn_contact -> {
                (requireActivity() as? ParentMainActivity)?.let {
                    it.binding.bnvMain.selectedItemId = R.id.nav_contact
                }
            }
        }
    }
}

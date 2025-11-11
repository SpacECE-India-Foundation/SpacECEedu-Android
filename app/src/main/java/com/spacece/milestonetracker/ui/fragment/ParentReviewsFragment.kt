package com.spacece.milestonetracker.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.spacece.milestonetracker.R
import com.spacece.milestonetracker.data.model.Review
import com.spacece.milestonetracker.databinding.DialogReviewBinding
import com.spacece.milestonetracker.databinding.FragmentParentsReviewsBinding
import com.spacece.milestonetracker.ui.activity.ParentMainActivity
import com.spacece.milestonetracker.ui.adapter.ReviewAdapter
import com.spacece.milestonetracker.ui.base.BaseFragment
import com.spacece.milestonetracker.utils.*

class ParentReviewsFragment : BaseFragment(), OnClickListener {
    private lateinit var binding: FragmentParentsReviewsBinding
    private lateinit var reviewAdapter: ReviewAdapter
    private var reviewList = arrayListOf<Review>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentParentsReviewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewsAndListeners()
    }

    private fun setupViewsAndListeners() = with(binding) {
        setOnClickListeners(listOf(ivBack, btnReview))
        reviewAdapter = ReviewAdapter(requireActivity()) {
            //TODO: handle on retry click
        }
        rvReviews.adapter = reviewAdapter
        rvReviews.setupRecyclerCache(200)
        reviewList.addAll(
            listOf(
                Review(), Review(), Review(), Review(),
                Review(), Review(), Review(), Review()
            )
        )
        updateListUIState(UIState.DataView)
        tvEmpty.gone()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_back -> {
                (activity as ParentMainActivity).onBackPressAction()
            }

            R.id.btn_review -> {
                openReviewAppDialog()
            }
        }
    }

    private fun updateListUIState(uiState: UIState) {
        if (reviewList.isEmpty()) return
        val lastIndex = reviewList.lastIndex
        val updatedList = reviewList.toMutableList().apply {
            this[lastIndex] = this[lastIndex].copy(
                isLastItem = true,
                uiState = uiState
            )
        }
        reviewAdapter.submitList(updatedList)
    }

    fun openReviewAppDialog() {
        val dialogBuilder = AlertDialog.Builder(
            requireActivity(),
            R.style.TransparentDialogTheme
        )
        val dialogBinding = DialogReviewBinding.inflate(layoutInflater)
        dialogBuilder.setView(dialogBinding.root)
        val reviewDialog = dialogBuilder.create()
        dialogBinding.apply {
            ivClose.setOnClickListener {
                reviewDialog.dismiss()
            }
            btnPost.setOnClickListener {
                reviewDialog.dismiss()
            }
        }
        reviewDialog.show()
    }

}



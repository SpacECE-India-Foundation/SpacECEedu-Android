package com.spacece.milestonetracker.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.spacece.milestonetracker.databinding.ItemQuestionsBinding
import com.spacece.milestonetracker.utils.setupText

class ChildDetailQuestionsAdapter(
    private val activity: Activity,
    private val onChecked: (String, String?) -> Unit
) : ListAdapter<Pair<String,String>, ChildDetailQuestionsAdapter.QuestionsViewHolder>(DiffCallback) {

    private var savedAnswers: Map<String, String> = emptyMap()

    fun updateSavedAnswers(answers: Map<String, String>) {
        savedAnswers = answers
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsViewHolder {
        val binding = ItemQuestionsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return QuestionsViewHolder(binding, onChecked)
    }

    override fun onBindViewHolder(holder: QuestionsViewHolder, position: Int) {
        holder.bind(getItem(position),savedAnswers)
    }

    class QuestionsViewHolder(
        private val binding: ItemQuestionsBinding,
        private val onChecked: (String, String?) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(question: Pair<String,String>,savedAnswers: Map<String, String>) = with(binding) {
            tvQuestion.setupText(question.second)
            rgOptions.setOnCheckedChangeListener(null)

            val savedAnswers = savedAnswers[question.first]
            // restore saved answer
            when (savedAnswers) {
                "1" -> rgOptions.check(rbYes.id)
                "2" -> rgOptions.check(rbNo.id)
                else -> rgOptions.clearCheck()
            }
            rgOptions.setOnCheckedChangeListener { _, checkedId ->
                val selectedAnswer = when (checkedId) {
                    rbYes.id -> "1"
                    rbNo.id -> "2"
                    else -> null
                }
                onChecked(question.first, selectedAnswer)
            }
        }
    }


    companion object DiffCallback : DiffUtil.ItemCallback<Pair<String, String>>() {
        override fun areItemsTheSame(
            oldItem: Pair<String, String>,
            newItem: Pair<String, String>
        ) = oldItem.first == newItem.first
        override fun areContentsTheSame(
            oldItem: Pair<String, String>,
            newItem: Pair<String, String>
        ) = oldItem == newItem
    }
}
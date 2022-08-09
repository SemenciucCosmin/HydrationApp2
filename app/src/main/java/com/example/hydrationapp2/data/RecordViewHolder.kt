package com.example.hydrationapp2.data

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.hydrationapp2.R
import com.example.hydrationapp2.databinding.RecordListItemBinding

class RecordViewHolder(private val binding: RecordListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(record: FullRecord) {
        binding.recordDate.text = getDetailedDate(record.date)
        binding.recordIntake.text = getIntake(record.intake, record.measurementUnit)
        binding.recordPercentage.text = getPercentage(record.intake, record.goal)
        binding.recordGoalMessage.text = getGoalMessage(record.goal, record.measurementUnit)

        binding.checkMark.visibility =
            if (record.intake >= record.goal) View.VISIBLE else View.INVISIBLE
    }

    private fun getIntake(intake: Int, unit: String): String {
        return "$intake $unit"
    }

    private fun getDetailedDate(date: String): String {
        val dates = date.split(".")
        return "${dates[WEEK_DAY]}, ${dates[MONTH_DAY]} ${dates[MONTH]}"
    }

    private fun getGoalMessage(goal: Int, unit: String): String {
        return itemView.resources.getString(R.string.goal_statistic).format(goal, unit)
    }

    private fun getPercentage(intake: Int, goal: Int): String {
        val percentage = (intake * 100) / goal
        return "$percentage%"
    }


}
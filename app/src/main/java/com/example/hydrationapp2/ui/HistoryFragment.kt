package com.example.hydrationapp2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hydrationapp2.HydrationApplication
import com.example.hydrationapp2.R
import com.example.hydrationapp2.data.BAR_WIDTH
import com.example.hydrationapp2.data.FIRST
import com.example.hydrationapp2.data.FullRecord
import com.example.hydrationapp2.data.GOAL_MAX_HEIGHT
import com.example.hydrationapp2.data.GOAL_MIN_HEIGHT
import com.example.hydrationapp2.data.LAST
import com.example.hydrationapp2.data.RecordListAdapter
import com.example.hydrationapp2.data.SHORT_DATE_PATTERN
import com.example.hydrationapp2.data.SQUARE_OFFSET
import com.example.hydrationapp2.data.SQUARE_SIZE
import com.example.hydrationapp2.data.SQUARE_SPACE
import com.example.hydrationapp2.databinding.FragmentHistoryBinding
import com.github.mikephil.charting.data.BarData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HydrationViewModel by activityViewModels {
        HydrationViewModel.HydrationViewModelFactory(
            (activity?.application as HydrationApplication)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        binding.historyFragment = this@HistoryFragment

        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.line_divider
            )!!
        )

        val adapter = RecordListAdapter()
        binding.apply {
            recordsRecyclerView.layoutManager = LinearLayoutManager(context)
            recordsRecyclerView.adapter = adapter
            recordsRecyclerView.setHasFixedSize(true)
            recordsRecyclerView.addItemDecoration(itemDecorator)
        }

        viewModel.getSettingsPreferences().observe(viewLifecycleOwner) { value ->
            viewModel.getFullRecords(value.goal, value.measurementUnit)
                .observe(this.viewLifecycleOwner) { records ->
                    records.let {
                        adapter.submitList(it.reversed())
                        setBarChart(records.reversed())
                    }
                }
        }
        return binding.root
    }

    fun navigateToSettings() {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
    }

    private fun setBarChart(databaseRecords: List<FullRecord>) {
        val completeRecords = viewModel.getBarChartRecords(databaseRecords)

        val formatter = SimpleDateFormat(SHORT_DATE_PATTERN, Locale.ENGLISH)
        val startDate = formatter.format(Date(completeRecords[FIRST].millis))
        val endDate = formatter.format(Date(completeRecords[LAST].millis))

        binding.startDate.text = startDate
        binding.endDate.text = endDate

        val goal = viewModel.getSettings().goal
        val barData = BarData()

        completeRecords.forEach { record ->
            barData.addDataSet(
                viewModel.createBarDataSet(
                    completeRecords.indexOf(record),
                    record.intake,
                    record.goal,
                    requireContext()
                )
            )
        }

        barData.barWidth = BAR_WIDTH

        binding.barChart.apply {
            data = barData
            setDrawBarShadow(true)
            setTouchEnabled(false)
            axisLeft.textColor = ContextCompat.getColor(requireContext(), R.color.white)
            axisRight.isEnabled = false
            xAxis.isEnabled = false
            axisLeft.setDrawAxisLine(false)
            description.isEnabled = false
            axisLeft.axisMaximum = (goal + GOAL_MAX_HEIGHT).toFloat()
            axisLeft.axisMinimum = GOAL_MIN_HEIGHT

            val chartLegend = legend
            chartLegend.xEntrySpace = SQUARE_SPACE
            chartLegend.xOffset = SQUARE_OFFSET
            chartLegend.formSize = SQUARE_SIZE

            val entries = chartLegend.entries
            entries.forEach {
                it.formColor = ContextCompat.getColor(requireContext(), R.color.gray_2)
            }
            entries[FIRST].formColor = ContextCompat.getColor(requireContext(), R.color.white)
            entries[LAST].formColor = ContextCompat.getColor(requireContext(), R.color.white)
            chartLegend.setCustom(entries)

            invalidate()
            refreshDrawableState()
        }
    }
}
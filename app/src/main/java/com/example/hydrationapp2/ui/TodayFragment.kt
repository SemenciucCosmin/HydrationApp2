package com.example.hydrationapp2.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hydrationapp2.HydrationApplication
import com.example.hydrationapp2.R
import com.example.hydrationapp2.data.UnitType
import com.example.hydrationapp2.databinding.FragmentTodayBinding

class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
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
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        binding.todayFragment = this@TodayFragment

        viewModel.addNewRecord()

        viewModel.getSettingsPreferences().observe(viewLifecycleOwner) {
            setViews()
            setGlassBackground()
        }

        viewModel.getCurrentRecord().observe(viewLifecycleOwner) {
            setViews()
            setGlassBackground()
        }

        return binding.root
    }

    fun navigateToSettings() {
        findNavController().navigate(MainFragmentDirections.actionMainFragmentToSettingsFragment())
    }

    fun addIntake(containerType: Int) {
        viewModel.addIntake(containerType)
    }

    private fun setGlassBackground() {
        val intake = viewModel.getIntake()
        val percentage = viewModel.getPercentage(intake)

        binding.apply {
            when {
                percentage >= 100 -> binding.glassBackground.setImageResource(R.drawable.glass_1)
                percentage >= 90 -> binding.glassBackground.setImageResource(R.drawable.glass_2)
                percentage >= 80 -> binding.glassBackground.setImageResource(R.drawable.glass_3)
                percentage >= 70 -> binding.glassBackground.setImageResource(R.drawable.glass_4)
                percentage >= 60 -> binding.glassBackground.setImageResource(R.drawable.glass_5)
                percentage >= 50 -> binding.glassBackground.setImageResource(R.drawable.glass_6)
                percentage >= 40 -> binding.glassBackground.setImageResource(R.drawable.glass_7)
                percentage >= 30 -> binding.glassBackground.setImageResource(R.drawable.glass_8)
                percentage >= 20 -> binding.glassBackground.setImageResource(R.drawable.glass_9)
                percentage >= 10 -> binding.glassBackground.setImageResource(R.drawable.glass_10)
                percentage >= 1 -> binding.glassBackground.setImageResource(R.drawable.glass_11)
            }
        }
    }

    private fun setViews() {
        val settingsPreferences = viewModel.getSettings()
        val intake = viewModel.getIntake()

        when (UnitType.getByUnitType(settingsPreferences.measurementUnit)) {
            UnitType.MILLILITERS -> {
                binding.apply {
                    goal.text = getString(R.string.goal_ml).format(settingsPreferences.goal)
                    currentIntake.text = getString(R.string.intake_container_ml).format(intake)
                    glassPercentage.text =
                        getString(R.string.glass_percentage).format(viewModel.getPercentage(intake))
                    container1Button.text =
                        getString(R.string.intake_container_ml).format(settingsPreferences.container1)
                    container2Button.text =
                        getString(R.string.intake_container_ml).format(settingsPreferences.container2)
                    container3Button.text =
                        getString(R.string.intake_container_ml).format(settingsPreferences.container3)
                }
            }
            UnitType.OUNCES -> {
                binding.apply {
                    goal.text = getString(R.string.goal_oz).format(settingsPreferences.goal)
                    currentIntake.text = getString(R.string.intake_container_oz).format(intake)
                    glassPercentage.text =
                        getString(R.string.glass_percentage).format(viewModel.getPercentage(intake))
                    container1Button.text =
                        getString(R.string.intake_container_oz).format(settingsPreferences.container1)
                    container2Button.text =
                        getString(R.string.intake_container_oz).format(settingsPreferences.container2)
                    container3Button.text =
                        getString(R.string.intake_container_oz).format(settingsPreferences.container3)
                }
            }
        }
    }
}
package com.example.hydrationapp2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hydrationapp2.HydrationApplication
import com.example.hydrationapp2.R
import com.example.hydrationapp2.data.UnitType
import com.example.hydrationapp2.databinding.FragmentUnitBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class UnitFragment : Fragment() {
    private var _binding: FragmentUnitBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HydrationViewModel by activityViewModels {
        HydrationViewModel.HydrationViewModelFactory(
            (activity?.application as HydrationApplication)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation?.visibility = View.INVISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUnitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.unitFragment = this@UnitFragment

        viewModel.getSettingsPreferences().observe(viewLifecycleOwner) {
            changeMeasurementUnit()
        }
    }

    fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun changeMeasurementUnit() {
        val settingsPreferences = viewModel.getSettings()
        when (UnitType.getByUnitType(settingsPreferences.measurementUnit)) {
            UnitType.MILLILITERS -> {
                binding.millilitersUnit.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
                binding.ouncesUnit.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
            }
            UnitType.OUNCES -> {
                binding.millilitersUnit.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.white
                    )
                )
                binding.ouncesUnit.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            }
        }
    }

    fun saveMeasurementUnit(measurementUnit: String) {
        viewModel.saveMeasurementUnit(
            measurementUnit,
            requireContext()
        )
    }
}
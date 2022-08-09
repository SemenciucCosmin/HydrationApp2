package com.example.hydrationapp2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.hydrationapp2.HydrationApplication
import com.example.hydrationapp2.R
import com.example.hydrationapp2.data.*
import com.example.hydrationapp2.databinding.FragmentSettingsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.NumberFormat
import java.util.*

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
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
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSettingsPreferences().observe(viewLifecycleOwner) {
            setMeasurementUnitContainerSizes(it)
        }

        binding.settingsFragment = this@SettingsFragment
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation?.visibility = View.INVISIBLE
    }

    override fun onStop() {
        super.onStop()
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation?.visibility = View.VISIBLE
    }

    fun navigateBack() {
        findNavController().navigateUp()
    }

    fun editUnit() {
        findNavController().navigate(
            SettingsFragmentDirections.actionSettingsFragmentToUnitFragment()
        )
    }

    private fun editContainer(containerType: Int) {
        findNavController().navigate(
            SettingsFragmentDirections.actionSettingsFragmentToQuantityFragment(
                containerType
            )
        )
    }

    private fun setMeasurementUnitContainerSizes(settingsPreferences: SettingsPreferences) {
        binding.apply {
            unitMeasurement.text = settingsPreferences.measurementUnit
            goalBox.root.setOnClickListener { editContainer(GOAL_TYPE) }
            goalBox.tabTitle.text = getString(R.string.daily_goal_title)
            goalBox.tabUnit.text = settingsPreferences.measurementUnit
            goalBox.tabSize.text = NumberFormat.getInstance(Locale.UK).format(settingsPreferences.goal)
            container1Box.root.setOnClickListener { editContainer(CONTAINER_1_TYPE) }
            container1Box.tabTitle.text = getString(R.string.container_1_title)
            container1Box.tabUnit.text = settingsPreferences.measurementUnit
            container1Box.tabSize.text =
                NumberFormat.getInstance(Locale.UK).format(settingsPreferences.container1)
            container2Box.root.setOnClickListener { editContainer(CONTAINER_2_TYPE) }
            container2Box.tabTitle.text = getString(R.string.container_2_title)
            container2Box.tabUnit.text = settingsPreferences.measurementUnit
            container2Box.tabSize.text =
                NumberFormat.getInstance(Locale.UK).format(settingsPreferences.container2)
            container3Box.root.setOnClickListener { editContainer(CONTAINER_3_TYPE) }
            container3Box.tabTitle.text = getString(R.string.container_3_title)
            container3Box.tabUnit.text = settingsPreferences.measurementUnit
            container3Box.tabSize.text =
                NumberFormat.getInstance(Locale.UK).format(settingsPreferences.container3)
        }
    }
}
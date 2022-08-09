package com.example.hydrationapp2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hydrationapp2.HydrationApplication
import com.example.hydrationapp2.data.ContainerType
import com.example.hydrationapp2.R
import com.example.hydrationapp2.data.UnitType
import com.example.hydrationapp2.databinding.FragmentQuantityBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.NumberFormat
import java.util.*

class QuantityFragment : Fragment() {
    private var _binding: FragmentQuantityBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HydrationViewModel by activityViewModels {
        HydrationViewModel.HydrationViewModelFactory(
            (activity?.application as HydrationApplication)
        )
    }
    private var containerType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bottomNavigation = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation?.visibility = View.INVISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuantityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.quantityFragment = this@QuantityFragment
        val args: QuantityFragmentArgs by navArgs()
        containerType = args.containerType
        binding.containerSize.setOnFocusChangeListener { _, hasFocus -> editSize(hasFocus) }

        viewModel.getSettingsPreferences().observe(viewLifecycleOwner) {
            setMeasurementUnit()
            setContentType()
        }
    }

    fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun setMeasurementUnit() {
        val settingsPreferences = viewModel.getSettings()
        when (UnitType.getByUnitType(settingsPreferences.measurementUnit)) {
            UnitType.MILLILITERS -> binding.measurementUnit.text =
                getString(R.string.ml_unit_choice)
            UnitType.OUNCES -> binding.measurementUnit.text = getString(R.string.oz_unit_choice)
        }
    }

    private fun setContentType() {
        when (ContainerType.getByContainerType(containerType)) {
            ContainerType.DAILY_GOAL -> setContentForGoal()
            ContainerType.CONTAINER_1 -> setContentContainer1()
            ContainerType.CONTAINER_2 -> setContentContainer2()
            ContainerType.CONTAINER_3 -> setContentContainer3()
        }
    }

    private fun setContentForGoal() {
        val settingsPreferences = viewModel.getSettings()
        binding.apply {
            topBarTitle.text = getString(R.string.daily_goal_title)
            containerSize.setText(NumberFormat.getInstance(Locale.UK).format(settingsPreferences.goal))
        }
    }

    private fun setContentContainer1() {
        val settingsPreferences = viewModel.getSettings()
        binding.apply {
            topBarTitle.text = getString(R.string.container_1_title)
            containerSize.setText(NumberFormat.getInstance(Locale.UK).format(settingsPreferences.container1))
        }
    }

    private fun setContentContainer2() {
        val settingsPreferences = viewModel.getSettings()
        binding.apply {
            topBarTitle.text = getString(R.string.container_2_title)
            containerSize.setText(NumberFormat.getInstance(Locale.UK).format(settingsPreferences.container2))
        }
    }

    private fun setContentContainer3() {
        val settingsPreferences = viewModel.getSettings()
        binding.apply {
            topBarTitle.text = getString(R.string.container_3_title)
            containerSize.setText(NumberFormat.getInstance(Locale.UK).format(settingsPreferences.container3))
        }
    }

    private fun editSize(isEditable: Boolean) {
        binding.backNavigation.visibility = if (!isEditable) View.VISIBLE else View.INVISIBLE
        binding.cancelButton.visibility = if (isEditable) View.VISIBLE else View.INVISIBLE
        binding.saveButton.visibility = if (isEditable) View.VISIBLE else View.INVISIBLE

        if (!isEditable) {
            binding.containerSize.isEnabled = isEditable
            binding.containerSize.isEnabled = !isEditable
        }
    }

    fun cancelEditedSize() {
        val settingsPreferences = viewModel.getSettings()
        when (ContainerType.getByContainerType(containerType)) {
            ContainerType.DAILY_GOAL -> binding.containerSize.setText(
                NumberFormat.getInstance(Locale.UK).format(settingsPreferences.goal)
            )
            ContainerType.CONTAINER_1 -> binding.containerSize.setText(
                NumberFormat.getInstance(Locale.UK).format(settingsPreferences.container1)
            )
            ContainerType.CONTAINER_2 -> binding.containerSize.setText(
                NumberFormat.getInstance(Locale.UK).format(settingsPreferences.container2)
            )
            ContainerType.CONTAINER_3 -> binding.containerSize.setText(
                NumberFormat.getInstance(Locale.UK).format(settingsPreferences.container3)
            )
        }
        editSize(false)
    }

    fun saveEditedSize() {
        when (ContainerType.getByContainerType(containerType)) {
            ContainerType.DAILY_GOAL -> viewModel.saveGoalSize(
                binding.containerSize.text.toString().replace(",", "").toInt(),
                requireContext()
            )
            ContainerType.CONTAINER_1 -> viewModel.saveContainer1Size(
                binding.containerSize.text.toString().replace(",", "").toInt(),
                requireContext()
            )
            ContainerType.CONTAINER_2 -> viewModel.saveContainer2Size(
                binding.containerSize.text.toString().replace(",", "").toInt(),
                requireContext()
            )
            ContainerType.CONTAINER_3 -> viewModel.saveContainer3Size(
                binding.containerSize.text.toString().replace(",", "").toInt(),
                requireContext()
            )
        }
        editSize(false)
    }
}
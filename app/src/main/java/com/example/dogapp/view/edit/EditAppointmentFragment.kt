package com.example.dogapp.view.edit

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.data.local.AppointmentEntity
import com.example.dogapp.databinding.FragmentEditAppointmentBinding
import com.example.dogapp.viewmodel.EditAppointmentViewModel

class EditAppointmentFragment : Fragment() {

    private var _binding: FragmentEditAppointmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditAppointmentViewModel by viewModels()

    private var currentAppointment: AppointmentEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.actvBreedEdit.threshold = 2
        setupValidations()
        setupButtons()
        observeViewModel()
        loadInitialAppointment()
    }

    private fun setupValidations() {
        binding.etPetNameEdit.doOnTextChanged { _, _, _, _ -> validateFields() }
        binding.actvBreedEdit.doOnTextChanged { _, _, _, _ -> validateFields() }
        binding.etOwnerNameEdit.doOnTextChanged { _, _, _, _ -> validateFields() }
        binding.etPhoneEdit.doOnTextChanged { _, _, _, _ -> validateFields() }
    }

    private fun validateFields() {
        binding.btnUpdateAppointment.isEnabled =
            viewModel.isLoading.value != true && areFieldsValid()
    }

    private fun areFieldsValid(): Boolean {
        val nameText = binding.etPetNameEdit.text?.toString().orEmpty().trim()
        val breedText = binding.actvBreedEdit.text?.toString().orEmpty().trim()
        val ownerText = binding.etOwnerNameEdit.text?.toString().orEmpty().trim()
        val phoneText = binding.etPhoneEdit.text?.toString().orEmpty().trim()

        val nameValid = nameText.length in 1..15
        val breedValid = breedText.isNotEmpty()
        val ownerValid = ownerText.length in 1..30
        val phoneValid = phoneText.length in 1..10

        return nameValid && breedValid && ownerValid && phoneValid
    }

    private fun setupButtons() {
        binding.btnReturnEdit.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnUpdateAppointment.setOnClickListener {
            currentAppointment?.let { original ->
                viewModel.updateAppointment(
                    original = original,
                    petName = binding.etPetNameEdit.text.toString(),
                    breed = binding.actvBreedEdit.text.toString(),
                    ownerName = binding.etOwnerNameEdit.text.toString(),
                    phone = binding.etPhoneEdit.text.toString()
                )
            }
        }
    }

    private fun observeViewModel() {
        viewModel.appointment.observe(viewLifecycleOwner) { cita ->
            if (cita != null) {
                populateFields(cita)
            }
        }

        viewModel.breeds.observe(viewLifecycleOwner) { breeds ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                breeds
            )
            binding.actvBreedEdit.setAdapter(adapter)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBarEdit.isVisible = loading
            binding.btnUpdateAppointment.isEnabled = !loading && areFieldsValid()
        }

        viewModel.appointmentUpdated.observe(viewLifecycleOwner) { updated ->
            if (updated) {
                findNavController().navigate(R.id.action_editAppointmentFragment_to_homeFragment)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrBlank()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                viewModel.onErrorHandled()
            }
        }
    }

    private fun loadInitialAppointment() {
        val appointmentArg = extractAppointmentFromArgs()
        val appointmentId = arguments?.getInt("appointmentId", -1) ?: -1

        when {
            appointmentArg != null -> {
                populateFields(appointmentArg)
                viewModel.loadAppointment(appointmentArg.id)
            }
            appointmentId > 0 -> {
                viewModel.loadAppointment(appointmentId)
            }
        }
    }

    private fun populateFields(cita: AppointmentEntity) {
        currentAppointment = cita
        binding.etPetNameEdit.setText(cita.petName)
        binding.actvBreedEdit.setText(cita.breed, false)
        binding.etOwnerNameEdit.setText(cita.ownerName)
        binding.etPhoneEdit.setText(cita.phone)
        validateFields()
    }

    @Suppress("DEPRECATION")
    private fun extractAppointmentFromArgs(): AppointmentEntity? {
        val args = arguments ?: return null
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            args.getSerializable("appointment", AppointmentEntity::class.java)
        } else {
            args.getSerializable("appointment") as? AppointmentEntity
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

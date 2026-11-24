package com.example.dogapp.view.edit

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.data.local.AppointmentEntity
import com.example.dogapp.databinding.FragmentEditAppointmentBinding
import com.example.dogapp.viewmodel.EditAppointmentViewModel

class EditAppointmentFragment : Fragment() {

    private var _binding: FragmentEditAppointmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditAppointmentViewModel by viewModels()

    private val breeds = listOf(
        "Labrador", "Bulldog", "Chihuahua", "Poodle",
        "Golden Retriever", "Beagle", "Pug", "Yorkshire"
    )

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

        setupBreedAutoComplete()
        setupValidations()
        setupButtons()
        observeViewModel()
        loadInitialAppointment()
    }

    private fun setupBreedAutoComplete() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            breeds
        )
        binding.actvBreedEdit.setAdapter(adapter)
        binding.actvBreedEdit.threshold = 2
    }

    private fun setupValidations() {
        binding.etPetNameEdit.doOnTextChanged { _, _, _, _ -> validateFields() }
        binding.actvBreedEdit.doOnTextChanged { _, _, _, _ -> validateFields() }
        binding.etOwnerNameEdit.doOnTextChanged { _, _, _, _ -> validateFields() }
        binding.etPhoneEdit.doOnTextChanged { _, _, _, _ -> validateFields() }
    }

    private fun validateFields() {
        val nameText = binding.etPetNameEdit.text.toString()
        val breedText = binding.actvBreedEdit.text.toString()
        val ownerText = binding.etOwnerNameEdit.text.toString()
        val phoneText = binding.etPhoneEdit.text.toString()

        val nameValid = nameText.length in 1..15
        val breedValid = breedText.isNotEmpty()
        val ownerValid = ownerText.length in 1..30
        val phoneValid = phoneText.length in 7..10

        binding.btnUpdateAppointment.isEnabled = nameValid && breedValid && ownerValid && phoneValid
    }

    private fun setupButtons() {
        binding.btnReturnEdit.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnUpdateAppointment.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeViewModel() {
        viewModel.appointment.observe(viewLifecycleOwner) { cita ->
            if (cita != null) {
                populateFields(cita)
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

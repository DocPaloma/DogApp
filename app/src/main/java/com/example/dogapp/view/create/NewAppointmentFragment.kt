package com.example.dogapp.view.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.databinding.FragmentAddCitaBinding
import com.example.dogapp.viewmodel.NewAppointmentViewModel

class NewAppointmentFragment : Fragment() {

    private var _binding: FragmentAddCitaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewAppointmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCitaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupValidations()
        setupListeners()
        observeViewModel()
        updateSaveButtonState()
    }

    private fun setupValidations() {
        binding.etNombreMascota.doOnTextChanged { _, _, _, _ -> updateSaveButtonState() }
        binding.etRace.doOnTextChanged { _, _, _, _ -> updateSaveButtonState() }
        binding.etNombrePropietario.doOnTextChanged { _, _, _, _ -> updateSaveButtonState() }
        binding.etPhone.doOnTextChanged { _, _, _, _ -> updateSaveButtonState() }
    }

    private fun areRequiredFieldsFilled(): Boolean {
        val petName = binding.etNombreMascota.text?.toString().orEmpty().trim()
        val breed = binding.etRace.text?.toString().orEmpty().trim()
        val ownerName = binding.etNombrePropietario.text?.toString().orEmpty().trim()
        val phone = binding.etPhone.text?.toString().orEmpty().trim()

        return petName.isNotEmpty() &&
            breed.isNotEmpty() &&
            ownerName.isNotEmpty() &&
            phone.isNotEmpty()
    }

    private fun updateSaveButtonState() {
        val isSaving = viewModel.isLoading.value == true
        binding.btnSaveCita.isEnabled = !isSaving && areRequiredFieldsFilled()
    }

    private fun setupListeners() {
        binding.btnReturn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSaveCita.setOnClickListener {
            val petName = binding.etNombreMascota.text?.toString().orEmpty().trim()
            val breed = binding.etRace.text?.toString().orEmpty().trim()
            val ownerName = binding.etNombrePropietario.text?.toString().orEmpty().trim()
            val phone = binding.etPhone.text?.toString().orEmpty().trim()
            val symptom = binding.spSymptoms.selectedItem?.toString().orEmpty().trim()

            if (symptom.isEmpty() || symptom.equals("Síntomas", ignoreCase = true)) {
                Toast.makeText(requireContext(), "Selecciona un síntoma", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveAppointment(
                petName = petName,
                breed = breed,
                ownerName = ownerName,
                phone = phone,
                symptom = symptom
            )
        }
    }

    private fun observeViewModel() {
        viewModel.breeds.observe(viewLifecycleOwner) { breeds ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, breeds)
            binding.etRace.setAdapter(adapter)
        }

        viewModel.isLoadingBreeds.observe(viewLifecycleOwner) { loading ->
            binding.progressBarBreeds.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBarSave.visibility = if (loading) View.VISIBLE else View.GONE
            updateSaveButtonState()
        }

        viewModel.appointmentSaved.observe(viewLifecycleOwner) { saved ->
            if (saved) {
                viewModel.onNavigationHandled()
                findNavController().popBackStack()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrBlank()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

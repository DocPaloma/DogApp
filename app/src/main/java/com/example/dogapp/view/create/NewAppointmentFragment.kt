package com.example.dogapp.view.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.databinding.FragmentAddCitaBinding

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

        setupListeners()
        observeViewModel()
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
            val symptom = binding.actSymptoms.text?.toString().orEmpty().trim()

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
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBarSave.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnSaveCita.isEnabled = !loading
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

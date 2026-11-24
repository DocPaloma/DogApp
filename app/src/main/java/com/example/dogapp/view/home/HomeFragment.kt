package com.example.dogapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dogapp.R
import com.example.dogapp.data.local.AppointmentEntity
import com.example.dogapp.databinding.FragmentHomeCitasBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeCitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val appointmentAdapter = AppointmentAdapter { appointment ->
        navigateToDetail(appointment)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeCitasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCitas.adapter = appointmentAdapter

        setupBackNavigation()
        setupListeners()
        observeViewModel()
    }

    private fun setupBackNavigation() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            }
        )
    }

    private fun setupListeners() {
        binding.fabAgregarCita.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_newAppointmentFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.appointments.observe(viewLifecycleOwner) { appointments ->
            appointmentAdapter.submitList(appointments)
        }
    }

    private fun navigateToDetail(appointment: AppointmentEntity) {
        val bundle = Bundle().apply {
            putSerializable("appointment", appointment)
        }
        findNavController().navigate(R.id.action_homeFragment_to_detailAppointmentFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

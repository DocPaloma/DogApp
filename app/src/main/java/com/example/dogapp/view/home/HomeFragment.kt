package com.example.dogapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dogapp.databinding.FragmentHomeCitasBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeCitasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val appointmentAdapter = AppointmentAdapter()

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

        viewModel.appointments.observe(viewLifecycleOwner) { appointments ->
            appointmentAdapter.submitList(appointments)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.example.dogapp.view.detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.dogapp.R
import com.example.dogapp.data.local.AppointmentEntity
import com.example.dogapp.databinding.FragmentDetailAppointmentBinding
import com.example.dogapp.viewmodel.DetailAppointmentViewModel

class DetailAppointmentFragment : Fragment() {

    private var _binding: FragmentDetailAppointmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailAppointmentViewModel by viewModels()

    private var appointment: AppointmentEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailAppointmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appointment = extractAppointmentFromArgs()
        appointment?.let { showAppointment(it) }

        controladores()
        observadorViewModel()
    }

    private fun showAppointment(cita: AppointmentEntity) {
        binding.tvTitleDetalle.text = cita.petName
        binding.tvTurnNumber.text = "#${cita.id}"
        binding.tvPetBreed.text = cita.breed
        binding.tvSymptoms.text = cita.symptom
        binding.tvOwnerName.text = cita.ownerName
        binding.tvOwnerPhone.text = cita.phone
        binding.ivPetPhoto.load(cita.imageUrl) {
            placeholder(R.drawable.ic_dog_head)
            error(R.drawable.ic_dog_head)
        }
    }

    private fun controladores() {
        binding.btnReturnDetail.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fabDeleteAppointment.setOnClickListener {
            appointment?.let { viewModel.deleteAppointment(it.id) }
        }

        binding.fabEditAppointment.setOnClickListener {
            val bundle = Bundle().apply {
                putSerializable("appointment", appointment)
            }
            findNavController().navigate(
                R.id.action_detailAppointmentFragment_to_editAppointmentFragment,
                bundle
            )
        }
    }

    private fun observadorViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.progressBarDetail.isVisible = loading
            binding.fabDeleteAppointment.isEnabled = !loading
            binding.fabEditAppointment.isEnabled = !loading
        }

        viewModel.appointmentDeleted.observe(viewLifecycleOwner) { deleted ->
            if (deleted) {
                findNavController().popBackStack()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrBlank()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                viewModel.onErrorHandled()
            }
        }
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

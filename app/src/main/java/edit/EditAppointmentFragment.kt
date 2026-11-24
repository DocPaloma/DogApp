package edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dogapp.databinding.FragmentEditAppointmentBinding

class EditAppointmentFragment : Fragment() {

    private var _binding: FragmentEditAppointmentBinding? = null
    private val binding get() = _binding!!

    private val breeds = listOf("Labrador", "Bulldog", "Chihuahua", "Poodle",
        "Golden Retriever", "Beagle", "Pug", "Yorkshire")

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
    }

    private fun setupBreedAutoComplete() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, breeds)
        binding.actvBreedEdit.setAdapter(adapter)
        binding.actvBreedEdit.threshold = 2
    }

    private fun setupValidations() {
        val validateFields = {
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

        binding.etPetNameEdit.doOnTextChanged { _, _, _, _ -> validateFields() }
        binding.actvBreedEdit.doOnTextChanged { _, _, _, _ -> validateFields() }
        binding.etOwnerNameEdit.doOnTextChanged { _, _, _, _ -> validateFields() }
        binding.etPhoneEdit.doOnTextChanged { _, _, _, _ -> validateFields() }
    }

    private fun setupButtons() {
        binding.btnReturnEdit.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnUpdateAppointment.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

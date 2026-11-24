package com.example.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dogapp.data.local.AppDatabase
import com.example.dogapp.data.local.AppointmentEntity
import com.example.dogapp.data.remote.RetrofitClient
import com.example.dogapp.data.repository.AppointmentRepository
import kotlinx.coroutines.launch

class NewAppointmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppointmentRepository by lazy {
        val dao = AppDatabase.getInstance(application).appointmentDao()
        AppointmentRepository(dao, RetrofitClient.dogApiService)
    }

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _appointmentSaved = MutableLiveData(false)
    val appointmentSaved: LiveData<Boolean> = _appointmentSaved

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _breeds = MutableLiveData<List<String>>()
    val breeds: LiveData<List<String>> = _breeds

    private val _isLoadingBreeds = MutableLiveData(false)
    val isLoadingBreeds: LiveData<Boolean> = _isLoadingBreeds

    init {
        loadBreeds()
    }

    fun isFormValid(
        petName: String,
        breed: String,
        ownerName: String,
        phone: String,
        symptom: String
    ): Boolean {
        val cleanPetName = petName.trim()
        val cleanBreed = breed.trim()
        val cleanOwnerName = ownerName.trim()
        val cleanPhone = phone.trim()
        val cleanSymptom = symptom.trim()

        if (cleanPetName.isEmpty() || cleanBreed.isEmpty() || cleanOwnerName.isEmpty() || cleanPhone.isEmpty()) {
            _errorMessage.value = "Completa todos los campos obligatorios"
            return false
        }

        if (cleanPhone.length < 7) {
            _errorMessage.value = "Ingresa un teléfono válido"
            return false
        }

        if (cleanSymptom.isEmpty() ||
            cleanSymptom.equals("Síntomas", ignoreCase = true) ||
            cleanSymptom.equals("Sintomas", ignoreCase = true)
        ) {
            _errorMessage.value = "Selecciona un síntoma"
            return false
        }

        return true
    }

    private fun loadBreeds() {
        _isLoadingBreeds.value = true
        viewModelScope.launch {
            try {
                _breeds.value = repository.getBreeds()
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo cargar la lista de razas"
            } finally {
                _isLoadingBreeds.value = false
            }
        }
    }



    fun saveAppointment(
        petName: String,
        breed: String,
        ownerName: String,
        phone: String,
        symptom: String
    ) {
        if (_isLoading.value == true) return
        if (!isFormValid(petName, breed, ownerName, phone, symptom)) return

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val imageUrl = repository.getBreedImageUrl(breed)
                val entity = AppointmentEntity(
                    petName = petName.trim(),
                    breed = breed.trim(),
                    ownerName = ownerName.trim(),
                    phone = phone.trim(),
                    symptom = symptom.trim(),
                    imageUrl = imageUrl
                )
                repository.insertAppointment(entity)
                _appointmentSaved.value = true
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Error al guardar la cita"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onNavigationHandled() {
        _appointmentSaved.value = false
    }
}
package com.example.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dogapp.data.local.AppDatabase
import com.example.dogapp.data.local.AppointmentEntity
import com.example.dogapp.data.remote.RetrofitClient
import com.example.dogapp.data.repository.AppointmentRepository
import kotlinx.coroutines.launch

class EditAppointmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppointmentRepository by lazy {
        val dao = AppDatabase.getInstance(application).appointmentDao()
        AppointmentRepository(dao, RetrofitClient.dogApiService)
    }

    private val _appointment = MutableLiveData<AppointmentEntity?>()
    val appointment: LiveData<AppointmentEntity?> = _appointment

    private val _breeds = MutableLiveData<List<String>>()
    val breeds: LiveData<List<String>> = _breeds

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _appointmentUpdated = MutableLiveData(false)
    val appointmentUpdated: LiveData<Boolean> = _appointmentUpdated

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadBreeds()
    }

    fun loadAppointment(appointmentId: Int) {
        if (appointmentId <= 0) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                _appointment.value = repository.findAppointmentById(appointmentId)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadBreeds() {
        viewModelScope.launch {
            try {
                _breeds.value = repository.getBreeds()
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo cargar la lista de razas"
            }
        }
    }

    fun updateAppointment(
        original: AppointmentEntity,
        petName: String,
        breed: String,
        ownerName: String,
        phone: String
    ) {
        if (_isLoading.value == true) return

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val cleanBreed = breed.trim()
                val imageUrl = if (cleanBreed.equals(original.breed, ignoreCase = true)) {
                    original.imageUrl
                } else {
                    repository.getBreedImageUrl(cleanBreed)
                }
                repository.updateAppointment(
                    original.copy(
                        petName = petName.trim(),
                        breed = cleanBreed,
                        ownerName = ownerName.trim(),
                        phone = phone.trim(),
                        imageUrl = imageUrl
                    )
                )
                _appointmentUpdated.value = true
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo actualizar la cita"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onErrorHandled() {
        _errorMessage.value = null
    }
}

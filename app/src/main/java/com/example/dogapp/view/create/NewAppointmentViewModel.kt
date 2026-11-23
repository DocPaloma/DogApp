package com.example.dogapp.view.create

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

    fun saveAppointment(
        petName: String,
        breed: String,
        ownerName: String,
        phone: String,
        symptom: String
    ) {
        if (_isLoading.value == true) return

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

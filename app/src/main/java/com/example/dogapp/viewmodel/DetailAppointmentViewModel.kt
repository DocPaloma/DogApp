package com.example.dogapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dogapp.data.local.AppDatabase
import com.example.dogapp.data.remote.RetrofitClient
import com.example.dogapp.data.repository.AppointmentRepository
import kotlinx.coroutines.launch

class DetailAppointmentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppointmentRepository by lazy {
        val dao = AppDatabase.getInstance(application).appointmentDao()
        AppointmentRepository(dao, RetrofitClient.dogApiService)
    }

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _appointmentDeleted = MutableLiveData(false)
    val appointmentDeleted: LiveData<Boolean> = _appointmentDeleted

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun deleteAppointment(appointmentId: Int) {
        if (_isLoading.value == true) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.deleteAppointmentById(appointmentId)
                _appointmentDeleted.value = true
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo eliminar la cita"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onErrorHandled() {
        _errorMessage.value = null
    }
}

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

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

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
}
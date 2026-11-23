package com.example.dogapp.view.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.dogapp.data.local.AppDatabase
import com.example.dogapp.data.local.AppointmentEntity
import com.example.dogapp.data.remote.RetrofitClient
import com.example.dogapp.data.repository.AppointmentRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppointmentRepository by lazy {
        val dao = AppDatabase.getInstance(application).appointmentDao()
        AppointmentRepository(dao, RetrofitClient.dogApiService)
    }

    val appointments: LiveData<List<AppointmentEntity>> = repository.getAllAppointments()
}

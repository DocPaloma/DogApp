package com.example.dogapp.data.repository

import androidx.lifecycle.LiveData
import com.example.dogapp.data.local.AppointmentDao
import com.example.dogapp.data.local.AppointmentEntity
import com.example.dogapp.data.remote.DogApiService

class AppointmentRepository(
    private val appointmentDao: AppointmentDao,
    private val dogApiService: DogApiService
) {

    fun getAllAppointments(): LiveData<List<AppointmentEntity>> {
        return appointmentDao.getAllAppointments()
    }

    fun getAppointmentById(id: Int): LiveData<AppointmentEntity?> {
        return appointmentDao.getAppointmentById(id)
    }

    suspend fun findAppointmentById(id: Int): AppointmentEntity? {
        return appointmentDao.findById(id)
    }

    suspend fun insertAppointment(appointment: AppointmentEntity): Long {
        return appointmentDao.insertAppointment(appointment)
    }

    suspend fun updateAppointment(appointment: AppointmentEntity) {
        appointmentDao.updateAppointment(appointment)
    }

    suspend fun deleteAppointmentById(id: Int) {
        appointmentDao.deleteAppointmentById(id)
    }

    suspend fun getBreeds(): List<String> {
        val response = dogApiService.getAllBreeds()
        return response.message.flatMap { (breed, subBreeds) ->
            if (subBreeds.isEmpty()) {
                listOf(breed)
            } else {
                listOf(breed) + subBreeds.map { sub -> "$breed $sub" }
            }
        }.sorted()
    }

    suspend fun getBreedImageUrl(breed: String): String {
        val parts = breed.trim().lowercase().split("\\s+".toRegex()).filter { it.isNotBlank() }
        return try {
            when {
                parts.size >= 2 -> dogApiService.getRandomImageBySubBreed(parts[0], parts[1]).message
                parts.size == 1 -> dogApiService.getRandomImageByBreed(parts[0]).message
                else -> dogApiService.getRandomDogImage().message
            }
        } catch (_: Exception) {
            dogApiService.getRandomDogImage().message
        }
    }
}

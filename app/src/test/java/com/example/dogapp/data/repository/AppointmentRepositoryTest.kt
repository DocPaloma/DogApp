package com.example.dogapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dogapp.data.local.AppointmentDao
import com.example.dogapp.data.local.AppointmentEntity
import com.example.dogapp.data.remote.DogApiService
import com.example.dogapp.data.remote.model.BreedsResponse
import com.example.dogapp.data.remote.model.DogImageResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class AppointmentRepositoryTest {

    private class FakeAppointmentDao : AppointmentDao {
        private val items = mutableListOf<AppointmentEntity>()
        private val liveData = MutableLiveData<List<AppointmentEntity>>(emptyList())

        override fun getAllAppointments(): LiveData<List<AppointmentEntity>> = liveData

        override fun getAppointmentById(id: Int): LiveData<AppointmentEntity?> {
            return MutableLiveData(items.find { it.id == id })
        }

        override suspend fun findById(id: Int): AppointmentEntity? = items.find { it.id == id }

        override suspend fun insertAppointment(appointment: AppointmentEntity): Long {
            val nextId = (items.maxOfOrNull { it.id } ?: 0) + 1
            val inserted = appointment.copy(id = nextId)
            items.add(inserted)
            liveData.postValue(items.toList())
            return nextId.toLong()
        }

        override suspend fun updateAppointment(appointment: AppointmentEntity) {
            val index = items.indexOfFirst { it.id == appointment.id }
            if (index >= 0) {
                items[index] = appointment
                liveData.postValue(items.toList())
            }
        }

        override suspend fun deleteAppointmentById(id: Int) {
            items.removeAll { it.id == id }
            liveData.postValue(items.toList())
        }
    }

    private class FakeDogApiService : DogApiService {
        override suspend fun getAllBreeds(): BreedsResponse {
            return BreedsResponse(
                message = mapOf(
                    "pitbull" to emptyList(),
                    "hound" to listOf("afghan", "basset")
                ),
                status = "success"
            )
        }

        override suspend fun getRandomImageByBreed(breed: String): DogImageResponse {
            if (breed == "unknown") throw RuntimeException("Not found")
            return DogImageResponse("https://images.dog.ceo/breeds/$breed/1.jpg", "success")
        }

        override suspend fun getRandomImageBySubBreed(breed: String, subBreed: String): DogImageResponse {
            return DogImageResponse("https://images.dog.ceo/breeds/$breed-$subBreed/1.jpg", "success")
        }

        override suspend fun getRandomDogImage(): DogImageResponse {
            return DogImageResponse("https://images.dog.ceo/breeds/fallback/1.jpg", "success")
        }
    }

    @Test
    fun `getBreeds flattens and sorts breeds and subBreeds`() = runTest {
        val repository = AppointmentRepository(FakeAppointmentDao(), FakeDogApiService())
        val breeds = repository.getBreeds()
        assertEquals(listOf("hound", "hound afghan", "hound basset", "pitbull"), breeds)
    }

    @Test
    fun `getBreedImageUrl resolves breed, subBreed and fallback`() = runTest {
        val repository = AppointmentRepository(FakeAppointmentDao(), FakeDogApiService())
        assertEquals("https://images.dog.ceo/breeds/pitbull/1.jpg", repository.getBreedImageUrl("Pitbull"))
        assertEquals("https://images.dog.ceo/breeds/hound-afghan/1.jpg", repository.getBreedImageUrl("hound afghan"))
        assertEquals("https://images.dog.ceo/breeds/fallback/1.jpg", repository.getBreedImageUrl("unknown"))
    }
}

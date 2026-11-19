package com.example.dogapp.data.remote

import com.example.dogapp.data.remote.model.BreedsResponse
import com.example.dogapp.data.remote.model.DogImageResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiService {

    @GET("breeds/list/all")
    suspend fun getAllBreeds(): BreedsResponse

    @GET("breed/{breed}/images/random")
    suspend fun getRandomImageByBreed(
        @Path("breed") breed: String
    ): DogImageResponse

    @GET("breed/{breed}/{subBreed}/images/random")
    suspend fun getRandomImageBySubBreed(
        @Path("breed") breed: String,
        @Path("subBreed") subBreed: String
    ): DogImageResponse

    @GET("breeds/image/random")
    suspend fun getRandomDogImage(): DogImageResponse
}

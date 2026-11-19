package com.example.dogapp.data.remote.model

data class BreedsResponse(
    val message: Map<String, List<String>>,
    val status: String
)

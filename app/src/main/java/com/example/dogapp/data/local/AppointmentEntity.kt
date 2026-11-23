package com.example.dogapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val petName: String,
    val breed: String,
    val ownerName: String,
    val phone: String,
    val symptom: String,
    val imageUrl: String
) : Serializable

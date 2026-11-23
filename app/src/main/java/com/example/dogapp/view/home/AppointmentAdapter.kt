package com.example.dogapp.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.dogapp.R
import com.example.dogapp.data.local.AppointmentEntity
import com.example.dogapp.databinding.ItemCitaBinding

class AppointmentAdapter(
    private val onItemClick: (AppointmentEntity) -> Unit = {}
) : ListAdapter<AppointmentEntity, AppointmentAdapter.AppointmentViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val binding = ItemCitaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppointmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AppointmentViewHolder(
        private val binding: ItemCitaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(appointment: AppointmentEntity) {
            binding.tvNombreMascota.text = appointment.petName
            binding.tvMotivoCita.text = appointment.symptom
            binding.tvNumeroCita.text = "#${appointment.id}"

            binding.ivFotoMascota.load(appointment.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_dog_head)
                error(R.drawable.ic_dog_head)
            }

            binding.cardCita.setOnClickListener {
                onItemClick(appointment)
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<AppointmentEntity>() {
        override fun areItemsTheSame(
            oldItem: AppointmentEntity,
            newItem: AppointmentEntity
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: AppointmentEntity,
            newItem: AppointmentEntity
        ): Boolean = oldItem == newItem
    }
}

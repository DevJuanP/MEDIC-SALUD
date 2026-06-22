package pe.edu.cibertec.medic_salud.view.citas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.medic_salud.data.remote.dto.DoctorDto
import pe.edu.cibertec.medic_salud.databinding.ItemDoctorBinding

class DoctorAdapter(
    private var doctors: List<DoctorDto>,
    private val onSelect: (DoctorDto) -> Unit
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    class DoctorViewHolder(val binding: ItemDoctorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val binding = ItemDoctorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]
        holder.binding.tvDoctorName.text = "${doctor.names} ${doctor.surnames}"
        holder.binding.tvSpecialty.text = "Especialidad: ${doctor.specialtyName}"
        holder.binding.btnSelect.setOnClickListener { onSelect(doctor) }
    }

    override fun getItemCount() = doctors.size

    fun updateList(newList: List<DoctorDto>) {
        doctors = newList
        notifyDataSetChanged()
    }
}

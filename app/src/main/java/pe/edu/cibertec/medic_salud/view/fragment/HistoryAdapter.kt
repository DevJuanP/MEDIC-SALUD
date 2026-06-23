package pe.edu.cibertec.medic_salud.view.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.medic_salud.data.remote.dto.AppointmentResponse
import pe.edu.cibertec.medic_salud.databinding.ItemHistoryAppointmentBinding

class HistoryAdapter(
    private var appointments: List<AppointmentResponse>,
    private val onViewDetail: (AppointmentResponse) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(val binding: ItemHistoryAppointmentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = appointments[position]
        holder.binding.tvHistorySpecialty.text = "Especialidad: ${item.specialtyName}"
        holder.binding.tvHistoryDate.text = "Fecha: ${item.date}"
        holder.binding.tvHistoryStatus.text = "Estado: ${item.statusName}"
        holder.binding.btnViewDetail.setOnClickListener { onViewDetail(item) }
    }

    override fun getItemCount() = appointments.size

    fun updateList(newList: List<AppointmentResponse>) {
        appointments = newList
        notifyDataSetChanged()
    }
}

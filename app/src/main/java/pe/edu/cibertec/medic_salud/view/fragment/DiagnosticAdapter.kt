package pe.edu.cibertec.medic_salud.view.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pe.edu.cibertec.medic_salud.data.remote.dto.DiagnosticItemDto
import pe.edu.cibertec.medic_salud.databinding.ItemDiagnosticBinding

class DiagnosticAdapter(
    private var diagnostics: List<DiagnosticItemDto>
) : RecyclerView.Adapter<DiagnosticAdapter.DiagnosticViewHolder>() {

    class DiagnosticViewHolder(val binding: ItemDiagnosticBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosticViewHolder {
        val binding = ItemDiagnosticBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiagnosticViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiagnosticViewHolder, position: Int) {
        val item = diagnostics[position]
        holder.binding.tvDiagStatus.text = item.statusName
        holder.binding.tvDiagDescription.text = item.description
        holder.binding.tvDiagNote.text = item.notes ?: "Sin notas"
    }

    override fun getItemCount() = diagnostics.size

    fun updateList(newList: List<DiagnosticItemDto>) {
        diagnostics = newList
        notifyDataSetChanged()
    }
}

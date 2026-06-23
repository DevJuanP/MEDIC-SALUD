package pe.edu.cibertec.medic_salud.data.remote.dto

data class HistoryResponse(
    val items: List<AppointmentResponse>,
    val total: Int,
    val page: Int,
    val pageSize: Int
)

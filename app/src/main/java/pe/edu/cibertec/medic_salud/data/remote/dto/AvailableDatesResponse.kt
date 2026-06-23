package pe.edu.cibertec.medic_salud.data.remote.dto

data class AvailableDatesResponse(
    val doctorCode: String,
    val availableDates: List<String>
)

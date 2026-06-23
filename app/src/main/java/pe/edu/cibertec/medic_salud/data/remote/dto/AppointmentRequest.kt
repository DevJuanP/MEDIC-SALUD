package pe.edu.cibertec.medic_salud.data.remote.dto

data class AppointmentRequest(
    val availabilityCode: String,
    val consultationTypeCode: String
)

package pe.edu.cibertec.medic_salud.data.remote.dto

data class AppointmentResponse(
    val appointmentCode: String,
    val patientCode: String,
    val patientNames: String,
    val patientSurnames: String,
    val doctorCode: String,
    val doctorNames: String,
    val doctorSurnames: String,
    val specialtyName: String,
    val consultationTypeName: String,
    val statusName: String,
    val statusCode: String,
    val scheduledAt: String,
    val date: String,
    val time: String,
    val createdAt: String
)

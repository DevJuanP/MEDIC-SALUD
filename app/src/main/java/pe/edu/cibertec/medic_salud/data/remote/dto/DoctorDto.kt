package pe.edu.cibertec.medic_salud.data.remote.dto

data class DoctorDto(
    val userCode: String,
    val names: String,
    val surnames: String,
    val licenseNumber: String,
    val specialtyName: String
)

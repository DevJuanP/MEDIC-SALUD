package pe.edu.cibertec.medic_salud.data.remote.dto

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: String,
    val patient: PatientDto
)

data class PatientDto(
    val patientCode: String,
    val names: String,
    val surnames: String,
    val email: String,
    val medicalRecordNumber: String
)

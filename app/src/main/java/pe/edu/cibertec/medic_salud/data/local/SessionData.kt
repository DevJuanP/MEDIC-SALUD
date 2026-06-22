package pe.edu.cibertec.medic_salud.data.local

data class SessionData(
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: String,
    val patientCode: String,
    val names: String,
    val surnames: String,
    val email: String,
    val medicalRecordNumber: String
)

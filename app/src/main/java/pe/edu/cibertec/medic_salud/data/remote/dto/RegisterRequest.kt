package pe.edu.cibertec.medic_salud.data.remote.dto

data class RegisterRequest(
    val names: String,
    val surnames: String,
    val birthDate: String,
    val sexCode: String,
    val phone: String,
    val alternativePhone: String?,
    val email: String,
    val address: String?,
    val emergencyContactName: String?,
    val emergencyContactPhone: String?,
    val documentTypeCode: String,
    val documentNumber: String,
    val password: String
)

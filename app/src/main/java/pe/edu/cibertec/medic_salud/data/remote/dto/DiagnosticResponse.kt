package pe.edu.cibertec.medic_salud.data.remote.dto

data class DiagnosticResponse(
    val items: List<DiagnosticItemDto>,
    val totalCount: Int,
    val page: Int,
    val pageSize: Int,
    val totalPages: Int
)

data class DiagnosticItemDto(
    val diagnosticCode: String,
    val appointmentCode: String,
    val patientCode: String,
    val statusCode: String,
    val statusName: String,
    val description: String,
    val diagnosedAt: String,
    val notes: String?,
    val createdAt: String,
    val prescription: PrescriptionDto?
)

data class PrescriptionDto(
    val prescriptionCode: String,
    val statusCode: String,
    val statusName: String,
    val validUntil: String,
    val detailCount: Int
)

package pe.edu.cibertec.medic_salud.data.remote.dto

data class AvailableSlotsResponse(
    val doctorCode: String,
    val date: String,
    val slots: List<TimeSlotDto>
)

data class TimeSlotDto(
    val availabilityCode: String,
    val time: String
)

package pe.edu.cibertec.medic_salud.data.remote.api

import pe.edu.cibertec.medic_salud.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface PatientAuthApi {

    @POST("api/patient-auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/patient-auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("api/sex")
    suspend fun getSexes(): Response<List<SexDto>>

    @GET("api/documenttype")
    suspend fun getDocumentTypes(): Response<List<DocumentTypeDto>>

    @POST("api/patient-auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>

    @GET("api/specialties")
    suspend fun getSpecialties(): Response<List<SpecialtyDto>>

    @GET("api/consultationtype")
    suspend fun getConsultationTypes(): Response<List<ConsultationTypeDto>>

    @GET("api/specialty/{code}/doctors")
    suspend fun getDoctorsBySpecialty(@Path("code") specialtyCode: String): Response<List<DoctorDto>>

    @GET("api/doctor/{code}/available-dates")
    suspend fun getAvailableDates(@Path("code") doctorCode: String): Response<AvailableDatesResponse>

    @GET("api/doctor/{code}/available-slots")
    suspend fun getAvailableSlots(
        @Path("code") doctorCode: String,
        @Query("date") date: String
    ): Response<AvailableSlotsResponse>

    @POST("api/appointment")
    suspend fun createAppointment(
        @Header("Authorization") token: String,
        @Body request: AppointmentRequest
    ): Response<AppointmentResponse>

    @PATCH("api/appointment/{code}/confirm-payment")
    suspend fun confirmPayment(
        @Header("Authorization") token: String,
        @Path("code") appointmentCode: String
    ): Response<Unit>
}

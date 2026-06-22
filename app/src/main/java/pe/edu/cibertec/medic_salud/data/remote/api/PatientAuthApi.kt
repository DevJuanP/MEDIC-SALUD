package pe.edu.cibertec.medic_salud.data.remote.api

import pe.edu.cibertec.medic_salud.data.remote.dto.ConsultationTypeDto
import pe.edu.cibertec.medic_salud.data.remote.dto.DoctorDto
import pe.edu.cibertec.medic_salud.data.remote.dto.DocumentTypeDto
import pe.edu.cibertec.medic_salud.data.remote.dto.LoginRequest
import pe.edu.cibertec.medic_salud.data.remote.dto.LoginResponse
import pe.edu.cibertec.medic_salud.data.remote.dto.RegisterRequest
import pe.edu.cibertec.medic_salud.data.remote.dto.SexDto
import pe.edu.cibertec.medic_salud.data.remote.dto.SpecialtyDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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
    suspend fun logout(@retrofit2.http.Header("Authorization") token: String): Response<Unit>

    @GET("api/specialties")
    suspend fun getSpecialties(): Response<List<SpecialtyDto>>

    @GET("api/consultationtype")
    suspend fun getConsultationTypes(): Response<List<ConsultationTypeDto>>

    @GET("api/specialty/{code}/doctors")
    suspend fun getDoctorsBySpecialty(@Path("code") specialtyCode: String): Response<List<DoctorDto>>
}

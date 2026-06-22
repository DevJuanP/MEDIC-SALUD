package pe.edu.cibertec.medic_salud.data.remote.api

import pe.edu.cibertec.medic_salud.data.remote.dto.DocumentTypeDto
import pe.edu.cibertec.medic_salud.data.remote.dto.LoginRequest
import pe.edu.cibertec.medic_salud.data.remote.dto.LoginResponse
import pe.edu.cibertec.medic_salud.data.remote.dto.RegisterRequest
import pe.edu.cibertec.medic_salud.data.remote.dto.SexDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PatientAuthApi {

    @POST("api/patient-auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/patient-auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("api/sex")
    suspend fun getSexes(): Response<List<SexDto>>

    @GET("api/documenttype")
    suspend fun getDocumentTypes(): Response<List<DocumentTypeDto>>
}

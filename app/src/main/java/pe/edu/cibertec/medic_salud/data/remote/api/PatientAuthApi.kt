package pe.edu.cibertec.medic_salud.data.remote.api

import pe.edu.cibertec.medic_salud.data.remote.dto.LoginRequest
import pe.edu.cibertec.medic_salud.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PatientAuthApi {

    @POST("api/patient-auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}

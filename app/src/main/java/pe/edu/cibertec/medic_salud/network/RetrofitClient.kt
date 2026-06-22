package pe.edu.cibertec.medic_salud.network

import pe.edu.cibertec.medic_salud.data.remote.api.PatientAuthApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.rxlink.xyz/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val patientAuthApi: PatientAuthApi by lazy {
        retrofit.create(PatientAuthApi::class.java)
    }
}

package pe.edu.cibertec.medic_salud.network

import okhttp3.OkHttpClient
import pe.edu.cibertec.medic_salud.data.remote.api.PatientAuthApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private const val BASE_URL = "https://api.rxlink.xyz/"

    private var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun buildRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /*private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }*/

    val patientAuthApi: PatientAuthApi by lazy {
        buildRetrofit().create(PatientAuthApi::class.java)
    }
}

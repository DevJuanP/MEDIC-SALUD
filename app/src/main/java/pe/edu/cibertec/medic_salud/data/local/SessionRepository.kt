package pe.edu.cibertec.medic_salud.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson
import com.google.gson.JsonObject
import pe.edu.cibertec.medic_salud.data.remote.dto.LoginRequest
import pe.edu.cibertec.medic_salud.data.remote.dto.LoginResponse
import pe.edu.cibertec.medic_salud.network.RetrofitClient

class SessionRepository(context: Context) {

    private val dbHelper = SessionDbHelper(context)
    private val api = RetrofitClient.patientAuthApi
    private val gson = Gson()

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()!!
                saveSession(body)
                Result.success(body)
            } else {
                Result.failure(Exception(parseErrorBody(response.errorBody()?.string(), response.code())))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Sin conexión. Verifica tu red e inténtalo de nuevo."))
        }
    }

    fun getSession(): SessionData? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            SessionDbHelper.TABLE_SESSION, null,
            null, null, null, null, null
        )
        return cursor.use {
            if (it.moveToFirst()) {
                SessionData(
                    accessToken = it.getString(it.getColumnIndexOrThrow(SessionDbHelper.COL_ACCESS_TOKEN)),
                    refreshToken = it.getString(it.getColumnIndexOrThrow(SessionDbHelper.COL_REFRESH_TOKEN)),
                    expiresAt = it.getString(it.getColumnIndexOrThrow(SessionDbHelper.COL_EXPIRES_AT)),
                    patientCode = it.getString(it.getColumnIndexOrThrow(SessionDbHelper.COL_PATIENT_CODE)),
                    names = it.getString(it.getColumnIndexOrThrow(SessionDbHelper.COL_NAMES)),
                    surnames = it.getString(it.getColumnIndexOrThrow(SessionDbHelper.COL_SURNAMES)),
                    email = it.getString(it.getColumnIndexOrThrow(SessionDbHelper.COL_EMAIL)),
                    medicalRecordNumber = it.getString(it.getColumnIndexOrThrow(SessionDbHelper.COL_MEDICAL_RECORD_NUMBER))
                )
            } else null
        }
    }

    fun clearSession() {
        dbHelper.writableDatabase.delete(SessionDbHelper.TABLE_SESSION, null, null)
    }

    private fun saveSession(response: LoginResponse) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(SessionDbHelper.COL_ID, 1)
            put(SessionDbHelper.COL_ACCESS_TOKEN, response.accessToken)
            put(SessionDbHelper.COL_REFRESH_TOKEN, response.refreshToken)
            put(SessionDbHelper.COL_EXPIRES_AT, response.expiresAt)
            put(SessionDbHelper.COL_PATIENT_CODE, response.patient.patientCode)
            put(SessionDbHelper.COL_NAMES, response.patient.names)
            put(SessionDbHelper.COL_SURNAMES, response.patient.surnames)
            put(SessionDbHelper.COL_EMAIL, response.patient.email)
            put(SessionDbHelper.COL_MEDICAL_RECORD_NUMBER, response.patient.medicalRecordNumber)
        }
        db.insertWithOnConflict(
            SessionDbHelper.TABLE_SESSION, null, values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    private fun parseErrorBody(body: String?, httpCode: Int): String {
        if (body.isNullOrBlank()) return "Error $httpCode"
        return try {
            val json = gson.fromJson(body, JsonObject::class.java)
            when {
                json.has("message") -> json["message"].asString
                json.has("errors") -> {
                    val errors = json["errors"].asJsonObject
                    errors.keySet()
                        .flatMap { key -> errors[key].asJsonArray.map { it.asString } }
                        .firstOrNull() ?: "Error de validación"
                }
                json.has("title") -> json["title"].asString
                else -> "Error $httpCode"
            }
        } catch (e: Exception) {
            "Error $httpCode"
        }
    }
}

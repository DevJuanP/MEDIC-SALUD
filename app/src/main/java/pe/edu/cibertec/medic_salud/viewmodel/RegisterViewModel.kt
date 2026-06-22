package pe.edu.cibertec.medic_salud.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import pe.edu.cibertec.medic_salud.data.local.SessionRepository
import pe.edu.cibertec.medic_salud.data.remote.api.PatientAuthApi
import pe.edu.cibertec.medic_salud.data.remote.dto.DocumentTypeDto
import pe.edu.cibertec.medic_salud.data.remote.dto.RegisterRequest
import pe.edu.cibertec.medic_salud.data.remote.dto.SexDto

class RegisterViewModel(
    private val api: PatientAuthApi,
    private val repository: SessionRepository
) : ViewModel() {

    private val gson = Gson()

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }

    private val _registerState = MutableLiveData<RegisterState>(RegisterState.Idle)
    val registerState: LiveData<RegisterState> = _registerState

    private val _sexes = MutableLiveData<List<SexDto>>()
    val sexes: LiveData<List<SexDto>> = _sexes

    private val _documentTypes = MutableLiveData<List<DocumentTypeDto>>()
    val documentTypes: LiveData<List<DocumentTypeDto>> = _documentTypes

    fun loadInitialData() {
        viewModelScope.launch {
            try {
                val sexResponse = api.getSexes()
                if (sexResponse.isSuccessful) {
                    _sexes.value = sexResponse.body()
                }

                val docResponse = api.getDocumentTypes()
                if (docResponse.isSuccessful) {
                    _documentTypes.value = docResponse.body()
                }
            } catch (e: Exception) {
                // Silently fail or log
            }
        }
    }

    fun register(request: RegisterRequest) {
        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            try {
                val response = api.register(request)
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    repository.saveSession(body)
                    _registerState.value = RegisterState.Success
                } else {
                    val errorMsg = parseErrorBody(response.errorBody()?.string(), response.code())
                    _registerState.value = RegisterState.Error(errorMsg)
                }
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error("Fallo de conexión: ${e.message}")
            }
        }
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
                        .joinToString("\n")
                }
                json.has("title") -> json["title"].asString
                else -> "Error $httpCode"
            }
        } catch (e: Exception) {
            "Error $httpCode"
        }
    }
}

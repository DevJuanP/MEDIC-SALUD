package pe.edu.cibertec.medic_salud.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.edu.cibertec.medic_salud.data.local.SessionRepository
import pe.edu.cibertec.medic_salud.data.remote.dto.LoginResponse

class LoginViewModel(private val repository: SessionRepository) : ViewModel() {

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        data class Success(val response: LoginResponse) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    val loginState = MutableLiveData<LoginState>(LoginState.Idle)

    fun login(email: String, password: String) {
        if (loginState.value is LoginState.Loading) return
        loginState.value = LoginState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.login(email, password)
            withContext(Dispatchers.Main) {
                loginState.value = result.fold(
                    onSuccess = { LoginState.Success(it) },
                    onFailure = { LoginState.Error(it.message ?: "Error desconocido") }
                )
            }
        }
    }
}

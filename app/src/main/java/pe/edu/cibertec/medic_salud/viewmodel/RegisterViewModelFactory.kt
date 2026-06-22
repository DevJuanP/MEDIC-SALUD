package pe.edu.cibertec.medic_salud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pe.edu.cibertec.medic_salud.data.local.SessionRepository
import pe.edu.cibertec.medic_salud.network.RetrofitClient

class RegisterViewModelFactory(private val repository: SessionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(RetrofitClient.patientAuthApi, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

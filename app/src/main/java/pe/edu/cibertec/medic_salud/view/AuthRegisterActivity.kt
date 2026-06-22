package pe.edu.cibertec.medic_salud.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import pe.edu.cibertec.medic_salud.data.local.SessionRepository
import pe.edu.cibertec.medic_salud.data.remote.dto.DocumentTypeDto
import pe.edu.cibertec.medic_salud.data.remote.dto.RegisterRequest
import pe.edu.cibertec.medic_salud.data.remote.dto.SexDto
import pe.edu.cibertec.medic_salud.databinding.ActivityAuthRegisterBinding
import pe.edu.cibertec.medic_salud.viewmodel.RegisterViewModel
import pe.edu.cibertec.medic_salud.viewmodel.RegisterViewModelFactory
import java.util.Calendar

class AuthRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    private var sexList: List<SexDto> = emptyList()
    private var docList: List<DocumentTypeDto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = SessionRepository(this)
        viewModel = ViewModelProvider(this, RegisterViewModelFactory(repository))[RegisterViewModel::class.java]

        observeViewModel()
        viewModel.loadInitialData()

        binding.etBirthDate.setOnClickListener { showDatePicker() }
        binding.btnRegister.setOnClickListener { attemptRegister() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            binding.etBirthDate.setText(formattedDate)
        }, year, month, day)
        datePicker.show()
    }

    private fun observeViewModel() {
        viewModel.sexes.observe(this) { list ->
            sexList = list
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list.map { it.name })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spSex.adapter = adapter
        }

        viewModel.documentTypes.observe(this) { list ->
            docList = list
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list.map { it.name })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spDocumentType.adapter = adapter
        }

        viewModel.registerState.observe(this) { state ->
            when (state) {
                is RegisterViewModel.RegisterState.Loading -> setLoading(true)
                is RegisterViewModel.RegisterState.Success -> {
                    setLoading(false)
                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity() // Cierra el flujo de login/registro
                }
                is RegisterViewModel.RegisterState.Error -> {
                    setLoading(false)
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                else -> setLoading(false)
            }
        }
    }

    private fun attemptRegister() {
        val names = binding.etNames.text.toString().trim()
        val surnames = binding.etSurnames.text.toString().trim()
        val birthDate = binding.etBirthDate.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val docNumber = binding.etDocumentNumber.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (names.isEmpty() || surnames.isEmpty() || birthDate.isEmpty() || phone.isEmpty() || 
            email.isEmpty() || docNumber.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val sexCode = if (sexList.isNotEmpty()) sexList[binding.spSex.selectedItemPosition].code else ""
        val docTypeCode = if (docList.isNotEmpty()) docList[binding.spDocumentType.selectedItemPosition].code else ""

        val request = RegisterRequest(
            names = names,
            surnames = surnames,
            birthDate = birthDate,
            sexCode = sexCode,
            phone = phone,
            alternativePhone = binding.etAltPhone.text.toString().trim().takeIf { it.isNotEmpty() },
            email = email,
            address = binding.etAddress.text.toString().trim().takeIf { it.isNotEmpty() },
            emergencyContactName = binding.etEmergencyContact.text.toString().trim().takeIf { it.isNotEmpty() },
            emergencyContactPhone = binding.etEmergencyPhone.text.toString().trim().takeIf { it.isNotEmpty() },
            documentTypeCode = docTypeCode,
            documentNumber = docNumber,
            password = password
        )

        viewModel.register(request)
    }

    private fun setLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !show
    }
}

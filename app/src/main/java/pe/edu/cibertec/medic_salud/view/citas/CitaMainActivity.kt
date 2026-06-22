package pe.edu.cibertec.medic_salud.view.citas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pe.edu.cibertec.medic_salud.R
import pe.edu.cibertec.medic_salud.data.remote.dto.ConsultationTypeDto
import pe.edu.cibertec.medic_salud.data.remote.dto.SpecialtyDto
import pe.edu.cibertec.medic_salud.databinding.ActivityCitaMainBinding
import pe.edu.cibertec.medic_salud.network.RetrofitClient

class CitaMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCitaMainBinding
    private var specialties: List<SpecialtyDto> = emptyList()
    private var consultationTypes: List<ConsultationTypeDto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCitaMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadData()

        binding.btnNext.setOnClickListener {
            if (specialties.isNotEmpty() && consultationTypes.isNotEmpty()) {
                val selectedSpecialty = specialties[binding.spSpecialty.selectedItemPosition]
                val selectedType = consultationTypes[binding.spConsultationType.selectedItemPosition]

                val intent = Intent(this, CitaDoctorActivity::class.java).apply {
                    putExtra("specialtyCode", selectedSpecialty.specialtyCode)
                    putExtra("specialtyName", selectedSpecialty.name)
                    putExtra("consultationTypeCode", selectedType.code)
                    putExtra("consultationTypeName", selectedType.name)
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Por favor, espere a que carguen los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadData() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnNext.isEnabled = false

        lifecycleScope.launch {
            try {
                val specialtiesResponse = RetrofitClient.patientAuthApi.getSpecialties()
                val typesResponse = RetrofitClient.patientAuthApi.getConsultationTypes()

                if (specialtiesResponse.isSuccessful && typesResponse.isSuccessful) {
                    specialties = specialtiesResponse.body() ?: emptyList()
                    consultationTypes = typesResponse.body() ?: emptyList()

                    setupSpinners()
                } else {
                    Toast.makeText(this@CitaMainActivity, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CitaMainActivity, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnNext.isEnabled = true
            }
        }
    }

    private fun setupSpinners() {
        val specialtyAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, specialties.map { it.name })
        specialtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spSpecialty.adapter = specialtyAdapter

        val typeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, consultationTypes.map { it.name })
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spConsultationType.adapter = typeAdapter
    }
}

package pe.edu.cibertec.medic_salud.view.citas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import pe.edu.cibertec.medic_salud.R
import pe.edu.cibertec.medic_salud.databinding.ActivityCitaDoctorBinding
import pe.edu.cibertec.medic_salud.network.RetrofitClient

class CitaDoctorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCitaDoctorBinding
    private lateinit var doctorAdapter: DoctorAdapter

    private var specialtyCode: String? = null
    private var specialtyName: String? = null
    private var consultationTypeCode: String? = null
    private var consultationTypeName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCitaDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recuperar datos del intent anterior
        specialtyCode = intent.getStringExtra("specialtyCode")
        specialtyName = intent.getStringExtra("specialtyName")
        consultationTypeCode = intent.getStringExtra("consultationTypeCode")
        consultationTypeName = intent.getStringExtra("consultationTypeName")

        setupRecyclerView()
        loadDoctors()
    }

    private fun setupRecyclerView() {
        doctorAdapter = DoctorAdapter(emptyList()) { doctor ->
            val intent = Intent(this, CitaTimeActivity::class.java).apply {
                putExtra("specialtyCode", specialtyCode)
                putExtra("specialtyName", specialtyName)
                putExtra("consultationTypeCode", consultationTypeCode)
                putExtra("consultationTypeName", consultationTypeName)
                putExtra("doctorCode", doctor.userCode)
                putExtra("doctorName", "${doctor.names} ${doctor.surnames}")
            }
            startActivity(intent)
        }
        binding.rvDoctors.layoutManager = LinearLayoutManager(this)
        binding.rvDoctors.adapter = doctorAdapter
    }

    private fun loadDoctors() {
        val code = specialtyCode ?: return
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.patientAuthApi.getDoctorsBySpecialty(code)
                if (response.isSuccessful) {
                    val doctors = response.body() ?: emptyList()
                    doctorAdapter.updateList(doctors)
                    if (doctors.isEmpty()) {
                        Toast.makeText(this@CitaDoctorActivity, "No hay doctores disponibles", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CitaDoctorActivity, "Error al cargar doctores", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CitaDoctorActivity, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}

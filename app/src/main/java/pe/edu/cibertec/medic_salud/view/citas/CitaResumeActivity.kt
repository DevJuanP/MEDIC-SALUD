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
import kotlinx.coroutines.launch
import pe.edu.cibertec.medic_salud.data.local.SessionRepository
import pe.edu.cibertec.medic_salud.data.remote.dto.AppointmentRequest
import pe.edu.cibertec.medic_salud.databinding.ActivityCitaResumeBinding
import pe.edu.cibertec.medic_salud.network.RetrofitClient

class CitaResumeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCitaResumeBinding

    private var specialtyCode: String? = null
    private var specialtyName: String? = null
    private var consultationTypeCode: String? = null
    private var consultationTypeName: String? = null
    private var doctorCode: String? = null
    private var doctorName: String? = null
    private var date: String? = null
    private var time: String? = null
    private var availabilityCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCitaResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recuperar todos los datos del flujo
        specialtyCode = intent.getStringExtra("specialtyCode")
        specialtyName = intent.getStringExtra("specialtyName")
        consultationTypeCode = intent.getStringExtra("consultationTypeCode")
        consultationTypeName = intent.getStringExtra("consultationTypeName")
        doctorCode = intent.getStringExtra("doctorCode")
        doctorName = intent.getStringExtra("doctorName")
        date = intent.getStringExtra("date")
        time = intent.getStringExtra("time")
        availabilityCode = intent.getStringExtra("availabilityCode")

        setupView()

        binding.btnConfirm.setOnClickListener {
            confirmAppointment()
        }
    }

    private fun setupView() {
        binding.tvDoctorName.text = doctorName
        binding.tvSpecialty.text = "Especialidad: $specialtyName"
        binding.tvResSpecialty.text = specialtyName
        binding.tvResType.text = consultationTypeName
        binding.tvResDate.text = date
        binding.tvResTime.text = time
    }

    private fun confirmAppointment() {
        val availCode = availabilityCode ?: return
        val typeCode = consultationTypeCode ?: return

        val repository = SessionRepository(this)
        val session = repository.getSession() ?: return

        binding.progressBar.visibility = View.VISIBLE
        binding.btnConfirm.isEnabled = false

        lifecycleScope.launch {
            try {
                val token = "Bearer ${session.accessToken}"
                val request = AppointmentRequest(availCode, typeCode)
                val response = RetrofitClient.patientAuthApi.createAppointment(token, request)

                if (response.isSuccessful) {
                    val appointment = response.body()
                    val intent = Intent(this@CitaResumeActivity, CitaFinalActivity::class.java).apply {
                        putExtra("appointmentCode", appointment?.appointmentCode)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@CitaResumeActivity, "Error al reservar: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CitaResumeActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
                binding.btnConfirm.isEnabled = true
            }
        }
    }
}

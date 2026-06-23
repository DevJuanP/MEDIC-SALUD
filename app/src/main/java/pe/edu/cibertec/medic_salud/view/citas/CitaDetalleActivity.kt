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
import pe.edu.cibertec.medic_salud.databinding.ActivityCitaDetalleBinding
import pe.edu.cibertec.medic_salud.network.RetrofitClient
import pe.edu.cibertec.medic_salud.view.HomeActivity

class CitaDetalleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCitaDetalleBinding
    private var appointmentCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCitaDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        appointmentCode = intent.getStringExtra("appointmentCode")

        loadAppointmentDetail()

        binding.btnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun loadAppointmentDetail() {
        val code = appointmentCode ?: return
        val repository = SessionRepository(this)
        val session = repository.getSession() ?: return

        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val token = "Bearer ${session.accessToken}"
                val response = RetrofitClient.patientAuthApi.getAppointmentDetail(token, code)

                if (response.isSuccessful) {
                    val appointment = response.body()
                    appointment?.let {
                        binding.tvDetailPatient.text = "${it.patientNames} ${it.patientSurnames}"
                        binding.tvDetailDoctor.text = "${it.doctorNames} ${it.doctorSurnames}"
                        binding.tvDetailSpecialty.text = it.specialtyName
                        binding.tvDetailType.text = it.consultationTypeName
                        binding.tvDetailStatus.text = it.statusName
                        binding.tvDetailDate.text = it.date
                        binding.tvDetailTime.text = it.time
                    }
                } else {
                    Toast.makeText(this@CitaDetalleActivity, "Error al cargar detalle", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CitaDetalleActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}

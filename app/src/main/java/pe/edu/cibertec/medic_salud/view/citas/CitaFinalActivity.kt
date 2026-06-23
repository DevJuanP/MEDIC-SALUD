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
import pe.edu.cibertec.medic_salud.databinding.ActivityCitaFinalBinding
import pe.edu.cibertec.medic_salud.network.RetrofitClient
import pe.edu.cibertec.medic_salud.view.HomeActivity

class CitaFinalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCitaFinalBinding
    private var appointmentCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCitaFinalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        appointmentCode = intent.getStringExtra("appointmentCode")

        binding.btnPayNow.setOnClickListener {
            confirmPayment()
        }

        binding.btnPayLater.setOnClickListener {
            Toast.makeText(this, "Su cita está pendiente de pago. ¡Le esperamos!", Toast.LENGTH_LONG).show()
            goToHome()
        }
    }

    private fun confirmPayment() {
        val code = appointmentCode ?: return
        val repository = SessionRepository(this)
        val session = repository.getSession() ?: return

        binding.progressBar.visibility = View.VISIBLE
        binding.btnPayNow.isEnabled = false
        binding.btnPayLater.isEnabled = false

        lifecycleScope.launch {
            try {
                val token = "Bearer ${session.accessToken}"
                val response = RetrofitClient.patientAuthApi.confirmPayment(token, code)

                if (response.isSuccessful) {
                    Toast.makeText(this@CitaFinalActivity, "¡Se pagó la cita con éxito! L@ esperamos.", Toast.LENGTH_LONG).show()
                    goToHome()
                } else {
                    Toast.makeText(this@CitaFinalActivity, "Error al confirmar pago", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    binding.btnPayNow.isEnabled = true
                    binding.btnPayLater.isEnabled = true
                }
            } catch (e: Exception) {
                Toast.makeText(this@CitaFinalActivity, "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                binding.btnPayNow.isEnabled = true
                binding.btnPayLater.isEnabled = true
            }
        }
    }

    private fun goToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

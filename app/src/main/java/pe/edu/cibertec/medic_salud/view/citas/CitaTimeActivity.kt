package pe.edu.cibertec.medic_salud.view.citas

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pe.edu.cibertec.medic_salud.data.remote.dto.TimeSlotDto
import pe.edu.cibertec.medic_salud.databinding.ActivityCitaTimeBinding
import pe.edu.cibertec.medic_salud.network.RetrofitClient

class CitaTimeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCitaTimeBinding

    private var specialtyCode: String? = null
    private var specialtyName: String? = null
    private var consultationTypeCode: String? = null
    private var consultationTypeName: String? = null
    private var doctorCode: String? = null
    private var doctorName: String? = null

    private var availableDates: List<String> = emptyList()
    private var availableSlots: List<TimeSlotDto> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCitaTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recuperar datos
        specialtyCode = intent.getStringExtra("specialtyCode")
        specialtyName = intent.getStringExtra("specialtyName")
        consultationTypeCode = intent.getStringExtra("consultationTypeCode")
        consultationTypeName = intent.getStringExtra("consultationTypeName")
        doctorCode = intent.getStringExtra("doctorCode")
        doctorName = intent.getStringExtra("doctorName")

        // Mostrar info del doctor
        binding.tvDoctorName.text = doctorName
        binding.tvSpecialty.text = "Especialidad: $specialtyName"

        loadAvailableDates()

        binding.btnNext.setOnClickListener {
            navigateToResume()
        }
    }

    private fun loadAvailableDates() {
        val code = doctorCode ?: return
        binding.progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.patientAuthApi.getAvailableDates(code)
                if (response.isSuccessful) {
                    availableDates = response.body()?.availableDates ?: emptyList()
                    setupDateSpinner()
                } else {
                    Toast.makeText(this@CitaTimeActivity, "Error al cargar fechas", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CitaTimeActivity, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setupDateSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, availableDates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spDate.adapter = adapter

        binding.spDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDate = availableDates[position]
                loadAvailableSlots(selectedDate)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadAvailableSlots(date: String) {
        val code = doctorCode ?: return
        binding.progressBar.visibility = View.VISIBLE
        binding.spTime.isEnabled = false
        binding.btnNext.isEnabled = false

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.patientAuthApi.getAvailableSlots(code, date)
                if (response.isSuccessful) {
                    availableSlots = response.body()?.slots ?: emptyList()
                    setupTimeSpinner()
                } else {
                    Toast.makeText(this@CitaTimeActivity, "Error al cargar horarios", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CitaTimeActivity, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun setupTimeSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, availableSlots.map { it.time })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spTime.adapter = adapter
        binding.spTime.isEnabled = true
        binding.btnNext.isEnabled = availableSlots.isNotEmpty()
    }

    private fun navigateToResume() {
        val selectedDate = availableDates[binding.spDate.selectedItemPosition]
        val selectedSlot = availableSlots[binding.spTime.selectedItemPosition]

        val intent = Intent(this, CitaResumeActivity::class.java).apply {
            putExtra("specialtyCode", specialtyCode)
            putExtra("specialtyName", specialtyName)
            putExtra("consultationTypeCode", consultationTypeCode)
            putExtra("consultationTypeName", consultationTypeName)
            putExtra("doctorCode", doctorCode)
            putExtra("doctorName", doctorName)
            putExtra("date", selectedDate)
            putExtra("time", selectedSlot.time)
            putExtra("availabilityCode", selectedSlot.availabilityCode)
        }
        startActivity(intent)
    }
}

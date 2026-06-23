package pe.edu.cibertec.medic_salud.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import pe.edu.cibertec.medic_salud.R
import pe.edu.cibertec.medic_salud.data.local.SessionRepository
import pe.edu.cibertec.medic_salud.databinding.FragmentHistoryBinding
import pe.edu.cibertec.medic_salud.network.RetrofitClient
import pe.edu.cibertec.medic_salud.view.citas.CitaDetalleActivity

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = SessionRepository(requireContext())
        val session = repository.getSession()

        session?.let {
            binding.tvWelcome.text = "¡Hola ${it.names}!"
        }

        setupRecyclerView()
        loadAppointments(session?.accessToken)

        // Sincronización con el avatar (igual que en HomeFragment)
        binding.ivUserAvatar.setOnClickListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav.selectedItemId = R.id.profileFragment
        }
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(emptyList()) { appointment ->
            val intent = Intent(requireContext(), CitaDetalleActivity::class.java).apply {
                putExtra("appointmentCode", appointment.appointmentCode)
            }
            startActivity(intent)
        }
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = historyAdapter
    }

    private fun loadAppointments(token: String?) {
        if (token == null) return

        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val bearerToken = "Bearer $token"
                val response = RetrofitClient.patientAuthApi.getAppointmentHistory(bearerToken)
                
                if (response.isSuccessful) {
                    val history = response.body()
                    historyAdapter.updateList(history?.items ?: emptyList())
                    if (history?.items.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), "No tienes citas registradas", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al cargar historial", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

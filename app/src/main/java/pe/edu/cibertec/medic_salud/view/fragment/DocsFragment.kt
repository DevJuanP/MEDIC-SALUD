package pe.edu.cibertec.medic_salud.view.fragment

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
import pe.edu.cibertec.medic_salud.databinding.FragmentDocsBinding
import pe.edu.cibertec.medic_salud.network.RetrofitClient

class DocsFragment : Fragment() {

    private var _binding: FragmentDocsBinding? = null
    private val binding get() = _binding!!
    private lateinit var diagnosticAdapter: DiagnosticAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDocsBinding.inflate(inflater, container, false)
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
        loadDiagnostics(session?.accessToken)

        // Sincronización con el avatar
        binding.ivUserAvatar.setOnClickListener {
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav.selectedItemId = R.id.profileFragment
        }
    }

    private fun setupRecyclerView() {
        diagnosticAdapter = DiagnosticAdapter(emptyList())
        binding.rvDiagnostics.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDiagnostics.adapter = diagnosticAdapter
    }

    private fun loadDiagnostics(token: String?) {
        if (token == null) return

        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val bearerToken = "Bearer $token"
                val response = RetrofitClient.patientAuthApi.getDiagnostics(bearerToken)
                
                if (response.isSuccessful) {
                    val diagnosticResponse = response.body()
                    diagnosticAdapter.updateList(diagnosticResponse?.items ?: emptyList())
                    if (diagnosticResponse?.items.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), "No tienes diagnósticos registrados", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error al cargar diagnósticos", Toast.LENGTH_SHORT).show()
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

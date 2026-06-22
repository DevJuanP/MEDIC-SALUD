package pe.edu.cibertec.medic_salud.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pe.edu.cibertec.medic_salud.data.local.SessionRepository
import pe.edu.cibertec.medic_salud.databinding.FragmentProfileBinding
import pe.edu.cibertec.medic_salud.network.RetrofitClient
import pe.edu.cibertec.medic_salud.view.AuthLoginActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = SessionRepository(requireContext())
        val session = repository.getSession()

        session?.let {
            binding.tvProfileName.text = it.names
            binding.tvProfileSurnames.text = it.surnames
            binding.tvProfileEmail.text = it.email
            binding.tvProfileMedicalRecord.text = it.medicalRecordNumber
        }

        binding.btnLogout.setOnClickListener {
            performLogout(repository)
        }
    }

    private fun performLogout(repository: SessionRepository) {
        val session = repository.getSession()
        if (session == null) {
            navigateToLogin()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnLogout.isEnabled = false

        lifecycleScope.launch {
            try {
                val token = "Bearer ${session.accessToken}"
                val response = RetrofitClient.patientAuthApi.logout(token)
                
                if (response.isSuccessful) {
                    repository.clearSession()
                    navigateToLogin()
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.btnLogout.isEnabled = true
                    Toast.makeText(requireContext(), "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                binding.progressBar.visibility = View.GONE
                binding.btnLogout.isEnabled = true
                Toast.makeText(requireContext(), "Error de red: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), AuthLoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

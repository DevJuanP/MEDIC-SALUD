package pe.edu.cibertec.medic_salud.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import pe.edu.cibertec.medic_salud.R
import pe.edu.cibertec.medic_salud.data.local.SessionRepository
import pe.edu.cibertec.medic_salud.databinding.FragmentHomeBinding
import pe.edu.cibertec.medic_salud.view.citas.CitaMainActivity

class HomeFragment : Fragment() {
// ... (rest of imports and class start)

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Cargar datos de la sesión
        val repository = SessionRepository(requireContext())
        val session = repository.getSession()

        session?.let {
            binding.tvWelcome.text = "¡Hola ${it.names}!"
        }

        // Configurar clics
        binding.ivUserAvatar.setOnClickListener {
            // Sincronizamos con el BottomNavigationView usando su ID correcto: bottom_nav
            val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)
            bottomNav.selectedItemId = R.id.profileFragment
        }

        binding.btnSchedule.setOnClickListener {
            val intent = Intent(requireContext(), CitaMainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

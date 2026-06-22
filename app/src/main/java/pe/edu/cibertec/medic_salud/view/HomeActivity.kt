package pe.edu.cibertec.medic_salud.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import pe.edu.cibertec.medic_salud.R
import pe.edu.cibertec.medic_salud.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binidng: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binidng = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binidng.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        as NavHostFragment
        val navController = navHostFragment.navController
        binidng.bottomNav.setupWithNavController(navController)
    }
}
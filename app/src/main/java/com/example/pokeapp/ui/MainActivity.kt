package com.example.pokeapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pokeapp.R
import com.example.pokeapp.databinding.ActivityMainBinding
import android.view.animation.AccelerateDecelerateInterpolator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setOnExitAnimationListener { provider ->
            val splashView = provider.view

            splashView.animate()
                .translationY(-splashView.height.toFloat())
                .setDuration(350L)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withEndAction { provider.remove() }
                .start()
        }

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mantém o conteúdo fora das barras do sistema (status, navegação e recorte de tela).
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val barras = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.updatePadding(barras.left, barras.top, barras.right, barras.bottom)
            WindowInsetsCompat.CONSUMED
        }

        val navHost = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNav.setupWithNavController(navController)

        // A barra inferior só aparece nas telas principais; os detalhes ocupam a tela toda.
        navController.addOnDestinationChangedListener { _, destino, _ ->
            val visivel = destino.id != R.id.detalhesPokemonFragment
            binding.bottomNav.isVisible = visivel
            binding.divisorNavegacao.isVisible = visivel
        }
    }
}

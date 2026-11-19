package com.example.pokeapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.pokeapp.R
import com.example.pokeapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
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

        binding.btnSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val opcoes = NavOptions.Builder()
                .setPopUpTo(R.id.navigation_mng, false)
                .setLaunchSingleTop(true)
                .build()
            navController.navigate(R.id.loginFragment, null, opcoes)
        }

        navController.addOnDestinationChangedListener { _, destino, _ ->
            val telaPrincipal = destino.id == R.id.buscaFragment ||
                    destino.id == R.id.listaPokemonFragment ||
                    destino.id == R.id.favoritosFragment
            val telaProtegida = telaPrincipal || destino.id == R.id.detalhesPokemonFragment

            binding.bottomNav.isVisible = telaPrincipal
            binding.divisorNavegacao.isVisible = telaPrincipal
            binding.headerSessao.isVisible = telaPrincipal

            if (telaProtegida && FirebaseAuth.getInstance().currentUser == null) {
                val opcoes = NavOptions.Builder()
                    .setPopUpTo(R.id.navigation_mng, false)
                    .setLaunchSingleTop(true)
                    .build()
                navController.navigate(R.id.loginFragment, null, opcoes)
            }
        }
    }
}

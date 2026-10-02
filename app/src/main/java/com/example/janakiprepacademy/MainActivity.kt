package com.example.janakiprepacademy

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.janakiprepacademy.ui.navigation.JanakiNavGraph
import com.example.janakiprepacademy.ui.theme.CreamWhite
import com.example.janakiprepacademy.ui.theme.JanakiPrepAcademyTheme

/**
 * Main entry point for Janaki PrepAcademy.
 *
 * Security: FLAG_SECURE blocks screenshots, screen-recording,
 * and Chromecast casting of premium test content — essential
 * anti-piracy DRM for exam question banks.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lock to Portrait for consistent test-taking CBT experience
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // ━━━ Anti-Piracy DRM: Block screenshots & screen recording ━━━
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )

        enableEdgeToEdge()

        // Initialize persistent authentication and user session
        com.example.janakiprepacademy.data.AuthManager.init(this)
        com.example.janakiprepacademy.data.SampleDataProvider.init(this)

        setContent {
            JanakiPrepAcademyTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = CreamWhite
                ) {
                    JanakiNavGraph(
                        navController = navController,
                        isLoggedIn = com.example.janakiprepacademy.data.AuthManager.isLoggedIn,
                        isOnboarded = com.example.janakiprepacademy.data.AuthManager.hasCompletedOnboarding
                    )
                }
            }
        }
    }
}
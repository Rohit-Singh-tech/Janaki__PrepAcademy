package com.example.janakiprepacademy.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.janakiprepacademy.data.AuthManager
import com.example.janakiprepacademy.data.model.ExamTrack
import com.example.janakiprepacademy.ui.admin.AdminPanelScreen
import com.example.janakiprepacademy.ui.auth.LoginScreen
import com.example.janakiprepacademy.ui.dashboard.DashboardScreen
import com.example.janakiprepacademy.ui.exam.ExamListScreen
import com.example.janakiprepacademy.ui.exam.ExamPlayerScreen
import com.example.janakiprepacademy.ui.leaderboard.LeaderboardScreen
import com.example.janakiprepacademy.ui.onboarding.OnboardingScreen
import com.example.janakiprepacademy.ui.profile.ProfileScreen
import com.example.janakiprepacademy.ui.scorecard.ScorecardScreen
import com.example.janakiprepacademy.ui.syllabus.SyllabusScreen

/**
 * Main navigation graph for Janaki PrepAcademy.
 * Controls the entire app flow from login → onboarding → dashboard → exams → admin.
 */
@Composable
fun JanakiNavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean = false,
    isOnboarded: Boolean = false
) {
    val startDestination = when {
        !isLoggedIn -> Screen.Login.route
        AuthManager.currentUser?.isAdmin == true -> Screen.AdminPanel.route
        !isOnboarded -> Screen.Onboarding.route
        else -> Screen.Dashboard.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideInHorizontally(tween(300)) { it } + fadeIn(tween(300)) },
        exitTransition = { slideOutHorizontally(tween(300)) { -it } + fadeOut(tween(300)) },
        popEnterTransition = { slideInHorizontally(tween(300)) { -it } + fadeIn(tween(300)) },
        popExitTransition = { slideOutHorizontally(tween(300)) { it } + fadeOut(tween(300)) }
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    val targetRoute = if (AuthManager.hasCompletedOnboarding) {
                        Screen.Dashboard.route
                    } else {
                        Screen.Onboarding.route
                    }
                    navController.navigate(targetRoute) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onAdminLogin = {
                    navController.navigate(Screen.AdminPanel.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onOnboardingComplete = { track, district ->
                    AuthManager.updateUserPreferences(track, district)
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onExamTrackClick = { track ->
                    navController.navigate(Screen.ExamList.createRoute(track.name))
                },
                onLeaderboardClick = {
                    navController.navigate(Screen.Leaderboard.route)
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onAdminClick = {
                    navController.navigate(Screen.AdminPanel.route)
                },
                onSyllabusClick = { trackName ->
                    navController.navigate(Screen.Syllabus.createRoute(trackName))
                }
            )
        }

        composable(
            route = Screen.ExamList.route,
            arguments = listOf(navArgument("trackName") { type = NavType.StringType })
        ) { backStackEntry ->
            val trackName = backStackEntry.arguments?.getString("trackName") ?: ExamTrack.BIHAR_STET.name
            val track = ExamTrack.valueOf(trackName)
            ExamListScreen(
                examTrack = track,
                onExamClick = { examId ->
                    navController.navigate(Screen.ExamPlayer.createRoute(examId))
                },
                onBackClick = { navController.popBackStack() },
                onSyllabusClick = { t ->
                    navController.navigate(Screen.Syllabus.createRoute(t.name))
                }
            )
        }

        composable(
            route = Screen.ExamPlayer.route,
            arguments = listOf(navArgument("examId") { type = NavType.StringType })
        ) { backStackEntry ->
            val examId = backStackEntry.arguments?.getString("examId") ?: ""
            ExamPlayerScreen(
                examId = examId,
                onSubmitExam = { attemptId ->
                    navController.navigate(Screen.Scorecard.createRoute(attemptId)) {
                        popUpTo(Screen.ExamPlayer.createRoute(examId)) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Scorecard.route,
            arguments = listOf(navArgument("attemptId") { type = NavType.StringType })
        ) { backStackEntry ->
            val attemptId = backStackEntry.arguments?.getString("attemptId") ?: ""
            ScorecardScreen(
                attemptId = attemptId,
                onBackToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Dashboard.route) { inclusive = true }
                    }
                },
                onViewLeaderboard = {
                    navController.navigate(Screen.Leaderboard.route)
                }
            )
        }

        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onBackClick = { navController.popBackStack() },
                onLogout = {
                    AuthManager.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onAdminClick = {
                    navController.navigate(Screen.AdminPanel.route)
                }
            )
        }

        composable(Screen.AdminPanel.route) {
            AdminPanelScreen(
                onNavigateToExam = { examId ->
                    navController.navigate(Screen.ExamPlayer.createRoute(examId))
                },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route)
                },
                onLogout = {
                    AuthManager.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Syllabus.route,
            arguments = listOf(navArgument("trackName") { type = NavType.StringType })
        ) { backStackEntry ->
            val trackName = backStackEntry.arguments?.getString("trackName") ?: "ALL"
            SyllabusScreen(
                initialTrackName = trackName,
                onBackClick = { navController.popBackStack() },
                onStartTestClick = { track ->
                    navController.navigate(Screen.ExamList.createRoute(track.name))
                }
            )
        }
    }
}

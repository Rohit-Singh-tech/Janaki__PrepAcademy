package com.example.janakiprepacademy.ui.navigation

/**
 * Navigation route definitions for Janaki PrepAcademy.
 */
sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Onboarding : Screen("onboarding")
    data object Dashboard : Screen("dashboard")
    data object ExamList : Screen("exam_list/{trackName}") {
        fun createRoute(trackName: String) = "exam_list/$trackName"
    }
    data object ExamPlayer : Screen("exam_player/{examId}") {
        fun createRoute(examId: String) = "exam_player/$examId"
    }
    data object Scorecard : Screen("scorecard/{attemptId}") {
        fun createRoute(attemptId: String) = "scorecard/$attemptId"
    }
    data object Leaderboard : Screen("leaderboard")
    data object Profile : Screen("profile")
    data object AdminPanel : Screen("admin_panel")
    data object Syllabus : Screen("syllabus/{trackName}") {
        fun createRoute(trackName: String) = "syllabus/$trackName"
    }
}

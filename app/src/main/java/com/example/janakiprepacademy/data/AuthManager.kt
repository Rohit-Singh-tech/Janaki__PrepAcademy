package com.example.janakiprepacademy.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.janakiprepacademy.data.model.ExamTrack
import kotlin.random.Random

data class UserAccount(
    val name: String,
    val email: String,
    val password: String,
    val isVerified: Boolean = true,
    var selectedTrack: ExamTrack = ExamTrack.BIHAR_STET,
    var district: String = "Sitamarhi",
    val isAdmin: Boolean = false
)

sealed class AuthResult {
    data class Success(val user: UserAccount) : AuthResult()
    data object Admin : AuthResult()
    data class Error(val message: String) : AuthResult()
}

/**
 * Manages user accounts, session state, OTP email verification,
 * and admin authentication for Janaki PrepAcademy.
 */
object AuthManager {
    // Registered accounts in memory (with default accounts + new registrations)
    private val accounts = mutableStateListOf<UserAccount>(
        UserAccount(
            name = "Aarav Thakur",
            email = "aarav@gmail.com",
            password = "password123",
            isVerified = true,
            selectedTrack = ExamTrack.BIHAR_STET,
            district = "Sitamarhi"
        ),
        UserAccount(
            name = "Priya Kumari",
            email = "priya@gmail.com",
            password = "password123",
            isVerified = true,
            selectedTrack = ExamTrack.BPSC_TEACHER,
            district = "Sitamarhi"
        )
    )

    // Current active session
    var currentUser by mutableStateOf<UserAccount?>(null)
        private set

    // Pending OTP store: email -> otp
    private val pendingOtps = mutableMapOf<String, String>()

    /**
     * Send 6-digit OTP to the user's Gmail.
     * Generates a realistic code and returns it so the UI can notify the user.
     */
    fun sendEmailOtp(email: String): String {
        val otp = String.format("%06d", Random.nextInt(100000, 999999))
        pendingOtps[email.lowercase().trim()] = otp
        return otp
    }

    /**
     * Verify whether the entered OTP matches the one sent to the Gmail address.
     */
    fun verifyEmailOtp(email: String, enteredOtp: String): Boolean {
        val storedOtp = pendingOtps[email.lowercase().trim()]
        if (storedOtp != null && storedOtp == enteredOtp.trim()) {
            pendingOtps.remove(email.lowercase().trim())
            return true
        }
        return false
    }

    /**
     * Register a newly verified user.
     */
    fun registerVerifiedUser(name: String, email: String, password: String): UserAccount {
        val cleanEmail = email.lowercase().trim()
        val existing = accounts.find { it.email.lowercase() == cleanEmail }
        if (existing != null) {
            accounts.remove(existing)
        }
        val newUser = UserAccount(
            name = name.trim(),
            email = cleanEmail,
            password = password,
            isVerified = true,
            selectedTrack = ExamTrack.BIHAR_STET,
            district = "Sitamarhi"
        )
        accounts.add(newUser)
        currentUser = newUser
        return newUser
    }

    /**
     * Log in user using Name or Email and Password.
     * Checks for Admin credentials: username=rohit, password=Rohit1234@#
     */
    fun login(identifier: String, password: String): AuthResult {
        val cleanId = identifier.trim()

        // 1. Check Admin Credentials
        if (cleanId.equals("rohit", ignoreCase = true) && password == "Rohit1234@#") {
            val adminUser = UserAccount(
                name = "Rohit (Admin)",
                email = "admin@janakiprep.com",
                password = password,
                isAdmin = true,
                selectedTrack = ExamTrack.BIHAR_STET,
                district = "Sitamarhi"
            )
            currentUser = adminUser
            return AuthResult.Admin
        }

        // 2. Check Standard User by Email or Name
        val user = accounts.find {
            (it.email.equals(cleanId, ignoreCase = true) || it.name.equals(cleanId, ignoreCase = true)) &&
                    it.password == password
        }

        return if (user != null) {
            currentUser = user
            AuthResult.Success(user)
        } else {
            AuthResult.Error("Invalid username/email or password. Please check and try again.")
        }
    }

    /**
     * Google / Gmail One-Tap Sign In
     */
    fun loginWithGoogle(email: String = "student@gmail.com", name: String = "Janaki Scholar"): UserAccount {
        val cleanEmail = email.lowercase().trim()
        var user = accounts.find { it.email.lowercase() == cleanEmail }
        if (user == null) {
            user = UserAccount(
                name = name,
                email = cleanEmail,
                password = "",
                isVerified = true,
                selectedTrack = ExamTrack.BIHAR_STET,
                district = "Sitamarhi"
            )
            accounts.add(user)
        }
        currentUser = user
        return user
    }

    fun updateUserPreferences(track: ExamTrack, district: String) {
        currentUser?.let { user ->
            user.selectedTrack = track
            user.district = district
        }
    }

    fun logout() {
        currentUser = null
    }
}

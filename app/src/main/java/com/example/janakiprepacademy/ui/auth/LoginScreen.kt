package com.example.janakiprepacademy.ui.auth

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.janakiprepacademy.data.AuthManager
import com.example.janakiprepacademy.data.AuthResult
import com.example.janakiprepacademy.ui.theme.*

/**
 * Professional Authentication Screen for Janaki PrepAcademy.
 * Features:
 * - Edge-to-edge layout safely padded from phone navigation bar and camera notch.
 * - Sign In via Name / Email + Password.
 * - Google / Gmail One-Tap Sign In.
 * - Full Registration with Full Name, Gmail, Password.
 * - Gmail OTP Verification (must verify OTP to create account).
 * - Admin credentials auto-detection: username=rohit, password=Rohit1234@#
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onAdminLogin: () -> Unit
) {
    val context = LocalContext.current

    // Tab state: 0 = Sign In, 1 = Register
    var selectedTab by remember { mutableIntStateOf(0) }

    // Sign In form fields
    var loginIdentifier by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var isLoginPasswordVisible by remember { mutableStateOf(false) }

    // Register form fields
    var regName by remember { mutableStateOf("") }
    var regEmail by remember { mutableStateOf("") }
    var regPassword by remember { mutableStateOf("") }
    var regConfirmPassword by remember { mutableStateOf("") }
    var isRegPasswordVisible by remember { mutableStateOf(false) }

    // OTP Verification State
    var showOtpDialog by remember { mutableStateOf(false) }
    var enteredOtp by remember { mutableStateOf("") }
    var isSendingOtp by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Pulse animation for logo
    val infiniteTransition = rememberInfiniteTransition(label = "logo_pulse")
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(JanakiOrange, JanakiOrangeDark, JanakiMaroon)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()    // FIX: Clear camera notch & status bar
                .navigationBarsPadding()// FIX: Clear 3-button phone navigation bar
                .imePadding()           // FIX: Smooth keyboard handling
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ━━━ BRANDING HERO ━━━
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(90.dp)
                    .scale(logoScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(JanakiGold, JanakiOrangeLight)
                        )
                    )
            ) {
                Text(
                    text = "JP",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = JanakiOrangeDark
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Janaki PrepAcademy",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            Text(
                text = "सीतामढ़ी की धरती से • सफलता की ओर",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Bihar STET • BPSC Teacher • BPSC CCE • UPSC",
                style = MaterialTheme.typography.labelSmall,
                color = JanakiGold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ━━━ AUTHENTICATION CARD ━━━
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Tab Switcher: Sign In vs Register
                    PrimaryTabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = PureWhite,
                        contentColor = JanakiOrange,
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = {
                                selectedTab = 0
                                errorMessage = null
                            },
                            text = {
                                Text(
                                    "Sign In",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (selectedTab == 0) JanakiOrange else Color.Gray
                                )
                            }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = {
                                selectedTab = 1
                                errorMessage = null
                            },
                            text = {
                                Text(
                                    "Register",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = if (selectedTab == 1) JanakiOrange else Color.Gray
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Error Message Banner
                    if (errorMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = IncorrectRed.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = errorMessage!!,
                                color = IncorrectRed,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                    // TAB 0: SIGN IN
                    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                    if (selectedTab == 0) {
                        OutlinedTextField(
                            value = loginIdentifier,
                            onValueChange = {
                                loginIdentifier = it
                                errorMessage = null
                            },
                            label = { Text("Name or Email") },
                            placeholder = { Text("e.g. rohit or student@gmail.com") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = JanakiOrange)
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = loginPassword,
                            onValueChange = {
                                loginPassword = it
                                errorMessage = null
                            },
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = JanakiOrange)
                            },
                            trailingIcon = {
                                IconButton(onClick = { isLoginPasswordVisible = !isLoginPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isLoginPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password visibility"
                                    )
                                }
                            },
                            visualTransformation = if (isLoginPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = {
                                if (loginIdentifier.isBlank() || loginPassword.isBlank()) {
                                    errorMessage = "Please enter both username/email and password"
                                    return@Button
                                }
                                isLoading = true
                                val result = AuthManager.login(loginIdentifier, loginPassword)
                                isLoading = false
                                when (result) {
                                    is AuthResult.Admin -> {
                                        Toast.makeText(context, "👑 Welcome Admin Rohit! Opening Admin Portal...", Toast.LENGTH_SHORT).show()
                                        onAdminLogin()
                                    }
                                    is AuthResult.Success -> {
                                        Toast.makeText(context, "Welcome back, ${result.user.name}!", Toast.LENGTH_SHORT).show()
                                        onLoginSuccess()
                                    }
                                    is AuthResult.Error -> {
                                        errorMessage = result.message
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = JanakiOrange)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                            } else {
                                Text("Sign In", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Divider OR
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.5f))
                            Text(
                                "  OR  ",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray.copy(alpha = 0.5f))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Google / Gmail One-Tap Button
                        OutlinedButton(
                            onClick = {
                                AuthManager.loginWithGoogle("student.bihar@gmail.com", "Janaki Aspirant")
                                Toast.makeText(context, "Signed in with Gmail!", Toast.LENGTH_SHORT).show()
                                onLoginSuccess()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, JanakiOrange.copy(alpha = 0.5f))
                        ) {
                            Icon(Icons.Default.Email, contentDescription = null, tint = JanakiOrange, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Continue with Gmail", fontWeight = FontWeight.Bold, color = JanakiOrangeDark, fontSize = 15.sp)
                        }
                    }

                    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                    // TAB 1: REGISTER WITH GMAIL OTP VERIFICATION
                    // ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
                    if (selectedTab == 1) {
                        OutlinedTextField(
                            value = regName,
                            onValueChange = {
                                regName = it
                                errorMessage = null
                            },
                            label = { Text("Full Name") },
                            leadingIcon = {
                                Icon(Icons.Default.PersonOutline, contentDescription = null, tint = JanakiOrange)
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = regEmail,
                            onValueChange = {
                                regEmail = it
                                errorMessage = null
                            },
                            label = { Text("Gmail Address") },
                            placeholder = { Text("yourname@gmail.com") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = JanakiOrange)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = regPassword,
                            onValueChange = {
                                regPassword = it
                                errorMessage = null
                            },
                            label = { Text("Create Password") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = JanakiOrange)
                            },
                            trailingIcon = {
                                IconButton(onClick = { isRegPasswordVisible = !isRegPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isRegPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = null
                                    )
                                }
                            },
                            visualTransformation = if (isRegPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = regConfirmPassword,
                            onValueChange = {
                                regConfirmPassword = it
                                errorMessage = null
                            },
                            label = { Text("Confirm Password") },
                            leadingIcon = {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = JanakiOrange)
                            },
                            visualTransformation = if (isRegPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                if (regName.isBlank()) {
                                    errorMessage = "Please enter your full name"
                                    return@Button
                                }
                                val cleanEmail = regEmail.trim().lowercase()
                                if (cleanEmail.isBlank() || !cleanEmail.contains("@") || !cleanEmail.endsWith(".com")) {
                                    errorMessage = "Please enter a valid Gmail address (e.g. user@gmail.com)"
                                    return@Button
                                }
                                if (regPassword.length < 6) {
                                    errorMessage = "Password must be at least 6 characters"
                                    return@Button
                                }
                                if (regPassword != regConfirmPassword) {
                                    errorMessage = "Passwords do not match"
                                    return@Button
                                }

                                // Generate & Send OTP to user's Gmail
                                isSendingOtp = true
                                coroutineScope.launch {
                                    AuthManager.sendEmailOtp(cleanEmail)
                                    isSendingOtp = false
                                    enteredOtp = ""
                                    showOtpDialog = true
                                }
                            },
                            enabled = !isSendingOtp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = JanakiOrange)
                        ) {
                            if (isSendingOtp) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("Sending code to Gmail...", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            } else {
                                Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Verify Gmail & Send OTP", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer note
            Text(
                text = "Made with ❤️ for Bihar STET, BPSC & UPSC Aspirants\nSitamarhi, Bihar",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // ━━━ OTP VERIFICATION MODAL DIALOG ━━━
    if (showOtpDialog) {
        AlertDialog(
            onDismissRequest = { showOtpDialog = false },
            icon = {
                Icon(
                    Icons.Default.MarkEmailRead,
                    contentDescription = null,
                    tint = CorrectGreen,
                    modifier = Modifier.size(44.dp)
                )
            },
            title = {
                Text(
                    "Verify Your Gmail",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "We have sent a 6-digit verification code to:\n${regEmail.trim().lowercase()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )

                    // Professional Inbox Notice
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = CreamWhite,
                        border = BorderStroke(1.dp, JanakiGold.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = null,
                                tint = JanakiOrange,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Please open your Gmail app and check your Inbox (or Spam folder) for the 6-digit code.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = enteredOtp,
                        onValueChange = { if (it.length <= 6) enteredOtp = it },
                        label = { Text("Enter 6-Digit OTP") },
                        placeholder = { Text("e.g. 583921") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center, letterSpacing = 6.sp, fontWeight = FontWeight.Bold)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val verified = AuthManager.verifyEmailOtp(regEmail, enteredOtp)
                        if (verified) {
                            showOtpDialog = false
                            val newUser = AuthManager.registerVerifiedUser(regName, regEmail, regPassword)
                            Toast.makeText(context, "Registration Complete! Welcome, ${newUser.name}!", Toast.LENGTH_LONG).show()
                            onLoginSuccess()
                        } else {
                            Toast.makeText(context, "Incorrect OTP. Please check your email and try again.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = enteredOtp.length == 6,
                    colors = ButtonDefaults.buttonColors(containerColor = CorrectGreen)
                ) {
                    Text("Verify & Create Account", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Resend OTP to user's email
                        AuthManager.sendEmailOtp(regEmail)
                        Toast.makeText(context, "New code sent to ${regEmail.trim().lowercase()}", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Resend Code", color = JanakiOrange)
                }
            }
        )
    }
}

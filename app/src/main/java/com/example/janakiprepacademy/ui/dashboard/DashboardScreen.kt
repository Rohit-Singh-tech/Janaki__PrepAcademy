package com.example.janakiprepacademy.ui.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.janakiprepacademy.R
import com.example.janakiprepacademy.data.AuthManager
import com.example.janakiprepacademy.data.model.ExamTrack
import com.example.janakiprepacademy.ui.theme.*
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos

/**
 * Main Dashboard — The home screen hub for Janaki PrepAcademy.
 * Displays exam categories, quick stats, and navigation shortcuts.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onExamTrackClick: (ExamTrack) -> Unit,
    onLeaderboardClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAdminClick: () -> Unit = {},
    onSyllabusClick: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "App Logo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .border(1.dp, JanakiGold, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                "Janaki PrepAcademy",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                color = JanakiOrange
                            )
                            Text(
                                "जय जानकी • सफलता निश्चित",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                },
                actions = {
                    if (AuthManager.currentUser?.isAdmin == true) {
                        FilledTonalButton(
                            onClick = onAdminClick,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = JanakiMaroon,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Icon(Icons.Filled.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Admin", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    IconButton(onClick = onLeaderboardClick) {
                        Icon(
                            Icons.Filled.Leaderboard,
                            contentDescription = "Leaderboard",
                            tint = JanakiOrange
                        )
                    }
                    IconButton(onClick = onProfileClick) {
                        Icon(
                            Icons.Filled.AccountCircle,
                            contentDescription = "Profile",
                            tint = JanakiOrange
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PureWhite
                )
            )
        },
        containerColor = CreamWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ━━━ Admin Quick Access Card (Visible only to Admin) ━━━
            if (AuthManager.currentUser?.isAdmin == true) {
                Card(
                    onClick = onAdminClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(JanakiOrange)
                        ) {
                            Icon(Icons.Filled.AdminPanelSettings, contentDescription = null, tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("👑 Admin Portal", fontWeight = FontWeight.Bold, color = JanakiGold, fontSize = 15.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = JanakiMaroon,
                                    contentColor = Color.White
                                ) {
                                    Text("PORTAL", fontSize = 9.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                                }
                            }
                            Text("Upload question PDFs, manage mock tests & answer keys", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.85f))
                        }
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = JanakiGold, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // ━━━ Quick Stats Banner ━━━
            QuickStatsBanner()

            Spacer(modifier = Modifier.height(20.dp))

            // ━━━ Exam Categories Grid ━━━
            Text(
                text = "Choose Your Exam",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ExamTrack.entries.forEach { track ->
                ExamCategoryCard(
                    track = track,
                    onClick = { onExamTrackClick(track) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ━━━ Quick Actions Row ━━━
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A2E),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    icon = Icons.Filled.Leaderboard,
                    label = "Leaderboard",
                    subtitle = "All Bihar Rank",
                    color = JanakiGold,
                    modifier = Modifier.weight(1f),
                    onClick = onLeaderboardClick
                )
                QuickActionCard(
                    icon = Icons.Filled.History,
                    label = "My Results",
                    subtitle = "Past Scores",
                    color = CorrectGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { /* Navigate to results history */ }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    icon = Icons.Filled.CloudDownload,
                    label = "Offline Tests",
                    subtitle = "No Internet",
                    color = CbtMarkedReview,
                    modifier = Modifier.weight(1f),
                    onClick = { /* Navigate to offline downloads */ }
                )
                QuickActionCard(
                    icon = Icons.Filled.MenuBook,
                    label = "Syllabus",
                    subtitle = "Exam Pattern",
                    color = JanakiMaroon,
                    modifier = Modifier.weight(1f),
                    onClick = { onSyllabusClick("ALL") }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ━━━ Feature Highlights ━━━
            FeatureHighlightBanner()

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuickStatsBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(JanakiOrange, JanakiMaroon)
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(value = "78+", label = "Mock Tests", icon = Icons.Filled.Quiz)
                StatItem(value = "15K+", label = "Questions", icon = Icons.Filled.HelpOutline)
                StatItem(value = "4", label = "Exams", icon = Icons.Filled.School)
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = null,
            tint = JanakiGold,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.85f)
        )
    }
}

@Composable
private fun ExamCategoryCard(track: ExamTrack, onClick: () -> Unit) {
    val cardColors = when (track) {
        ExamTrack.BIHAR_STET -> listOf(Color(0xFF1565C0), Color(0xFF0D47A1))
        ExamTrack.BPSC_TEACHER -> listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))
        ExamTrack.BPSC_CCE -> listOf(JanakiOrange, JanakiOrangeDark)
        ExamTrack.UPSC_CSE -> listOf(JanakiMaroon, JanakiMaroonDark)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(cardColors))
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Emoji icon in circle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Text(text = track.iconEmoji, fontSize = 28.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = track.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = track.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Exam config badges (FlowRow wraps cleanly onto next line without vertical letter stacking)
                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ExamBadge("${track.totalQuestions} Qs")
                        ExamBadge("${track.durationMinutes} Min")
                        if (track.hasNegativeMarking) {
                            ExamBadge("−ve Marking")
                        } else {
                            ExamBadge("No −ve")
                        }
                        ExamBadge("${track.optionsCount} Options")
                    }
                }

                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = "Open",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun ExamBadge(text: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color.White.copy(alpha = 0.22f)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            softWrap = false,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
private fun QuickActionCard(
    icon: ImageVector,
    label: String,
    subtitle: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f))
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun FeatureHighlightBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "✨ Why Janaki PrepAcademy?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = JanakiGold
            )
            Spacer(modifier = Modifier.height(12.dp))

            FeatureItem("🎯", "Authentic CBT Interface — Practice exactly like the real exam center")
            FeatureItem("📊", "Instant Analytics — All India Rank, Percentile & Time Analysis")
            FeatureItem("📥", "Offline Mode — Download tests, practice anywhere in Bihar")
            FeatureItem("🤖", "AI Mains Checker — Upload handwritten answers for AI evaluation")
            FeatureItem("🏆", "District Leaderboard — Compete with students from Sitamarhi & beyond")
            FeatureItem("🆓", "100% Free — No subscriptions, no hidden charges")
        }
    }
}

@Composable
private fun FeatureItem(emoji: String, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = emoji, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}

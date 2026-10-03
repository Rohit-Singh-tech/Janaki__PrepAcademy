package com.example.janakiprepacademy.ui.scorecard

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.janakiprepacademy.data.ExamAttemptRepository
import com.example.janakiprepacademy.data.model.QuestionReviewItem
import com.example.janakiprepacademy.ui.theme.*

enum class ReviewFilter(val label: String) {
    ALL("All"),
    INCORRECT("Incorrect"),
    SKIPPED("Skipped"),
    CORRECT("Correct")
}

/**
 * Scorecard / Analytics Screen — Displays real-time validated post-exam performance report.
 * Shows exact Score, AIR, Percentile, Accuracy, Time Analysis, Section Breakdown, and Question Solutions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScorecardScreen(
    attemptId: String,
    onBackToDashboard: () -> Unit,
    onViewLeaderboard: () -> Unit
) {
    val context = LocalContext.current
    val result = remember(attemptId) { ExamAttemptRepository.getAttempt(attemptId, context) }

    val score = result?.finalScore ?: 0.0
    val maxScore = result?.maxScore ?: 150.0
    val accuracy = result?.accuracyPercentage ?: 0.0
    val allIndiaRank = result?.allIndiaRank ?: 1
    val percentile = result?.percentile ?: 50.0
    val correct = result?.correctCount ?: 0
    val incorrect = result?.incorrectCount ?: 0
    val skipped = result?.skippedCount ?: 0
    val seconds = result?.timeTakenSeconds ?: 0L
    val minutes = seconds / 60
    val remSecs = seconds % 60
    val timeTaken = if (minutes > 0) "$minutes min $remSecs sec" else "$remSecs sec"
    val reviews = result?.questionReviews ?: emptyList()

    var selectedFilter by remember { mutableStateOf(ReviewFilter.ALL) }

    val filteredReviews = remember(selectedFilter, reviews) {
        when (selectedFilter) {
            ReviewFilter.ALL -> reviews
            ReviewFilter.INCORRECT -> reviews.filter { !it.isCorrect && !it.isSkipped }
            ReviewFilter.SKIPPED -> reviews.filter { it.isSkipped }
            ReviewFilter.CORRECT -> reviews.filter { it.isCorrect }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exam Result", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PureWhite)
            )
        },
        containerColor = CreamWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ━━━ Hero Score Card ━━━
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(JanakiOrange, JanakiOrangeDark)
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🏆", fontSize = 44.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = result?.examTitle ?: "Real CBT Examination",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Your Score",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                        val formattedScore = if (score % 1.0 == 0.0) "${score.toInt()} / ${maxScore.toInt()}" else "%.2f / ${maxScore.toInt()}".format(score)
                        Text(
                            text = formattedScore,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // AIR and Percentile row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ScoreMetric("🏅 AIR", "#$allIndiaRank")
                            ScoreMetric("📊 Percentile", "%.1f%%".format(percentile))
                            ScoreMetric("🎯 Accuracy", "%.1f%%".format(accuracy))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ━━━ Breakdown Cards ━━━
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BreakdownCard(
                    icon = Icons.Filled.CheckCircle,
                    value = "$correct",
                    label = "Correct",
                    color = CorrectGreen,
                    modifier = Modifier.weight(1f)
                )
                BreakdownCard(
                    icon = Icons.Filled.Cancel,
                    value = "$incorrect",
                    label = "Incorrect",
                    color = IncorrectRed,
                    modifier = Modifier.weight(1f)
                )
                BreakdownCard(
                    icon = Icons.Filled.RemoveCircle,
                    value = "$skipped",
                    label = "Skipped",
                    color = SkippedGray,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ━━━ Time Analysis ━━━
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite)
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
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(JanakiGold.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            Icons.Filled.Timer,
                            contentDescription = null,
                            tint = JanakiGoldDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Time Taken",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            timeTaken,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ━━━ Section Breakdown (if available) ━━━
            val sections = result?.sectionWiseBreakdown ?: emptyList()
            if (sections.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Section Breakdown",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        sections.forEach { sec ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    sec.sectionName,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray,
                                    modifier = Modifier.weight(1f)
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("✓ ${sec.correct}", color = CorrectGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("✗ ${sec.incorrect}", color = IncorrectRed, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Text("– ${sec.skipped}", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 0.5.dp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ━━━ Performance Message ━━━
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (accuracy >= 70) CorrectGreen.copy(alpha = 0.1f)
                    else WarningAmber.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (accuracy >= 70) "🌟 Great Performance!" else "💪 Keep Practicing!",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (accuracy >= 70) CorrectGreen else WarningAmber
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (accuracy >= 70)
                            "You're on track to clear the exam! Review solutions below to master every topic."
                        else
                            "Review the questions you got wrong below to learn from mistakes and improve your score.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ━━━ Question Solutions & Detailed Review ━━━
            if (reviews.isNotEmpty()) {
                Spacer(modifier = Modifier.height(18.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Question Solutions & Review",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Filter chips row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ReviewFilter.entries.forEach { filter ->
                            val isSelected = selectedFilter == filter
                            val count = when (filter) {
                                ReviewFilter.ALL -> reviews.size
                                ReviewFilter.INCORRECT -> incorrect
                                ReviewFilter.SKIPPED -> skipped
                                ReviewFilter.CORRECT -> correct
                            }
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) JanakiOrange else PureWhite,
                                border = if (isSelected) null else BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                                modifier = Modifier.clickable { selectedFilter = filter }
                            ) {
                                Text(
                                    "${filter.label} ($count)",
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else Color.DarkGray,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Reviews list
                    filteredReviews.take(50).forEach { item ->
                        QuestionReviewCard(item)
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    if (filteredReviews.size > 50) {
                        Text(
                            "Showing 50 of ${filteredReviews.size} questions",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ━━━ Action Buttons ━━━
            Button(
                onClick = onViewLeaderboard,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JanakiOrange)
            ) {
                Icon(Icons.Filled.Leaderboard, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("View Leaderboard", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onBackToDashboard,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Filled.Home, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Back to Dashboard", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun QuestionReviewCard(item: QuestionReviewItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Q${item.questionNumber}.",
                        fontWeight = FontWeight.Bold,
                        color = JanakiOrange,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        item.sectionName,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when {
                        item.isCorrect -> CorrectGreen.copy(alpha = 0.15f)
                        item.isSkipped -> SkippedGray.copy(alpha = 0.2f)
                        else -> IncorrectRed.copy(alpha = 0.15f)
                    }
                ) {
                    Text(
                        text = when {
                            item.isCorrect -> "✓ Correct"
                            item.isSkipped -> "– Skipped"
                            else -> "✗ Incorrect"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            item.isCorrect -> CorrectGreen
                            item.isSkipped -> Color.DarkGray
                            else -> IncorrectRed
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.questionText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1A2E),
                lineHeight = 20.sp
            )

            if (item.questionTextHindi.isNotBlank() && item.questionTextHindi != item.questionText) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.questionTextHindi,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray.copy(alpha = 0.8f),
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Options list
            item.options.forEach { opt ->
                val isCorrectOpt = opt.id.equals(item.correctOption, ignoreCase = true)
                val isUserPick = opt.id.equals(item.userSelectedOption, ignoreCase = true)

                val bgColor = when {
                    isCorrectOpt -> CorrectGreen.copy(alpha = 0.12f)
                    isUserPick && !item.isCorrect -> IncorrectRed.copy(alpha = 0.12f)
                    else -> Color.Transparent
                }
                val borderColor = when {
                    isCorrectOpt -> CorrectGreen
                    isUserPick && !item.isCorrect -> IncorrectRed
                    else -> Color.LightGray.copy(alpha = 0.4f)
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = bgColor,
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${opt.id}.",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = when {
                                isCorrectOpt -> CorrectGreen
                                isUserPick && !item.isCorrect -> IncorrectRed
                                else -> Color.DarkGray
                            }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            opt.text,
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.weight(1f)
                        )
                        if (isCorrectOpt) {
                            Text("✓ Correct Answer", fontSize = 10.sp, color = CorrectGreen, fontWeight = FontWeight.Bold)
                        } else if (isUserPick) {
                            Text("Your Answer", fontSize = 10.sp, color = IncorrectRed, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (item.explanation.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LightOrangeTint.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            "💡 Explanation:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = JanakiOrangeDark
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            item.explanation,
                            fontSize = 11.sp,
                            color = Color.DarkGray,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreMetric(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.75f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = JanakiGold
        )
    }
}

@Composable
private fun BreakdownCard(
    icon: ImageVector,
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = color
            )
            Text(label, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}

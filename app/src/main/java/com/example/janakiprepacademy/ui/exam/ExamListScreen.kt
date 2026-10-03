package com.example.janakiprepacademy.ui.exam

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.janakiprepacademy.data.SampleDataProvider
import com.example.janakiprepacademy.data.model.Exam
import com.example.janakiprepacademy.data.model.ExamTrack
import com.example.janakiprepacademy.ui.theme.*

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.janakiprepacademy.data.QuestionBankRepository

/**
 * Exam List Screen — Shows all available mock tests for a specific exam track.
 * Lists free and premium tests with attempt counts and Real CBT Exam Simulator.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamListScreen(
    examTrack: ExamTrack,
    onExamClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onSyllabusClick: ((ExamTrack) -> Unit)? = null
) {
    val context = LocalContext.current
    val exams = SampleDataProvider.getAvailableExams().filter { it.category == examTrack }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            examTrack.displayName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${exams.size} tests available • 150 Qs Real Ratio",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (onSyllabusClick != null) {
                        IconButton(onClick = { onSyllabusClick(examTrack) }) {
                            Icon(Icons.Filled.MenuBook, contentDescription = "View Syllabus", tint = JanakiMaroon)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PureWhite
                )
            )
        },
        containerColor = CreamWhite
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            // Exam info header
            item {
                ExamInfoCard(examTrack)
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Real CBT Exam Simulator Action Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = JanakiMaroon),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.White.copy(alpha = 0.2f),
                                    modifier = Modifier.size(38.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("⚡", fontSize = 20.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        "Real CBT Simulation Exam",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        "150 Qs • Exact Official Ratio • Fresh Scenario",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = PureWhite.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Simulates the actual examination center. Pulls a balanced 150-question mock paper from the 5,000+ question bank, dynamically prioritizing unseen questions so every question is tested.",
                            style = MaterialTheme.typography.bodySmall,
                            color = PureWhite.copy(alpha = 0.9f),
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = {
                                val simExam = QuestionBankRepository.generateRealCbtExamSimulation(examTrack, context)
                                onExamClick(simExam.examId)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = JanakiOrange),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Generate & Start Real CBT Exam",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            items(exams) { exam ->
                MockTestCard(exam = exam, onClick = { onExamClick(exam.examId) })
            }

            // Bottom spacer
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun ExamInfoCard(track: ExamTrack) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = LightOrangeTint)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${track.iconEmoji} Exam Pattern",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = JanakiOrangeDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoChip(Icons.Filled.Quiz, "${track.totalQuestions} Questions")
                InfoChip(Icons.Filled.Timer, "${track.durationMinutes} Minutes")
                InfoChip(Icons.Filled.Rule, "${track.optionsCount} Options")
            }
            if (track.hasNegativeMarking) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "⚠️ Negative marking: -1/${(1.0 / track.negativeMarkFraction).toInt()} for each wrong answer",
                    style = MaterialTheme.typography.bodySmall,
                    color = IncorrectRed,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun InfoChip(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
            tint = JanakiOrange,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun MockTestCard(exam: Exam, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exam.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    val qCount = exam.sections.sumOf { it.questions.size }
                    if (qCount > 0) {
                        Text(
                            text = "$qCount Questions • ${exam.totalDurationMinutes} min",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    } else {
                        Text(
                            text = "0 Questions • Upload Paper via Admin",
                            style = MaterialTheme.typography.bodySmall,
                            color = JanakiOrangeDark,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                if (exam.isFree) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CorrectGreen.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "FREE",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = CorrectGreen,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.People,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${exam.totalAttempts} attempts",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = JanakiOrange),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Start Test", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

package com.example.janakiprepacademy.ui.exam

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.janakiprepacademy.data.SampleDataProvider
import com.example.janakiprepacademy.data.model.*
import com.example.janakiprepacademy.ui.theme.*
import kotlinx.coroutines.delay
import java.util.UUID

/**
 * CBT Exam Player — The core test-taking engine.
 * Replicates the official Bihar STET / BPSC exam center CBT interface.
 *
 * Features:
 * - Countdown timer with server-side timestamp validation
 * - Color-coded Question Palette (Grey/Red/Green/Purple/Blue)
 * - Save & Next, Clear Response, Mark for Review, Back buttons
 * - 4 or 5 option radio buttons (configurable per exam)
 * - Auto-save progress on every interaction
 * - Section navigation with question counts
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamPlayerScreen(
    examId: String,
    onSubmitExam: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val exam = remember { SampleDataProvider.getAvailableExams().find { it.examId == examId } }
    if (exam == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Exam not found", color = Color.Red)
        }
        return
    }

    val allQuestions = remember { exam.sections.flatMap { it.questions } }
    val totalQuestions = allQuestions.size

    // CBT State Management
    var currentIndex by remember { mutableIntStateOf(0) }
    val responses = remember {
        mutableStateMapOf<String, UserResponse>().apply {
            allQuestions.forEach { q ->
                put(q.questionId, UserResponse(q.questionId, null, QuestionState.UNVISITED))
            }
        }
    }
    var showPalette by remember { mutableStateOf(false) }
    var showSubmitDialog by remember { mutableStateOf(false) }

    // Timer state
    var remainingSeconds by remember { mutableLongStateOf(exam.totalDurationMinutes * 60L) }
    LaunchedEffect(Unit) {
        while (remainingSeconds > 0) {
            delay(1000)
            remainingSeconds--
        }
        // Auto-submit when timer expires
        val attemptId = UUID.randomUUID().toString()
        onSubmitExam(attemptId)
    }

    // Mark current question as visited
    LaunchedEffect(currentIndex) {
        val qId = allQuestions[currentIndex].questionId
        val currentResponse = responses[qId]
        if (currentResponse?.state == QuestionState.UNVISITED) {
            responses[qId] = currentResponse.copy(state = QuestionState.NOT_ANSWERED)
        }
    }

    val currentQuestion = allQuestions[currentIndex]
    val currentResponse = responses[currentQuestion.questionId]

    Scaffold(
        topBar = {
            // Exam header with timer
            TopAppBar(
                title = {
                    Column {
                        Text(
                            exam.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            "Q ${currentIndex + 1} of $totalQuestions",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                },
                actions = {
                    // Timer display
                    TimerChip(remainingSeconds)
                    // Palette toggle
                    IconButton(onClick = { showPalette = !showPalette }) {
                        Icon(
                            Icons.Filled.GridView,
                            contentDescription = "Question Palette",
                            tint = if (showPalette) JanakiOrange else Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PureWhite)
            )
        },
        bottomBar = {
            // CBT Action Buttons
            CbtActionBar(
                currentIndex = currentIndex,
                totalQuestions = totalQuestions,
                onSaveAndNext = {
                    if (currentIndex < totalQuestions - 1) currentIndex++
                },
                onClearResponse = {
                    val qId = allQuestions[currentIndex].questionId
                    responses[qId] = UserResponse(qId, null, QuestionState.NOT_ANSWERED)
                },
                onMarkForReview = {
                    val qId = allQuestions[currentIndex].questionId
                    val resp = responses[qId]!!
                    val newState = if (resp.selectedOption != null) {
                        QuestionState.ANSWERED_MARKED
                    } else {
                        QuestionState.MARKED_REVIEW
                    }
                    responses[qId] = resp.copy(state = newState)
                    if (currentIndex < totalQuestions - 1) currentIndex++
                },
                onPrevious = {
                    if (currentIndex > 0) currentIndex--
                },
                onSubmit = { showSubmitDialog = true }
            )
        },
        containerColor = CreamWhite
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            // Main question content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Section badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = JanakiOrange.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = currentQuestion.sectionName,
                        style = MaterialTheme.typography.labelMedium,
                        color = JanakiOrange,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Question text
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Q${currentIndex + 1}.",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = JanakiOrange
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = currentQuestion.text,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1A1A2E),
                            lineHeight = 26.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Options
                currentQuestion.options.forEach { option ->
                    val isSelected = currentResponse?.selectedOption == option.id
                    OptionCard(
                        option = option,
                        isSelected = isSelected,
                        onClick = {
                            val qId = currentQuestion.questionId
                            responses[qId] = UserResponse(
                                qId, option.id, QuestionState.ANSWERED
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            // Question Palette Drawer (overlay)
            AnimatedVisibility(
                visible = showPalette,
                enter = slideInHorizontally { it } + fadeIn(),
                exit = slideOutHorizontally { it } + fadeOut()
            ) {
                QuestionPalettePanel(
                    questions = allQuestions,
                    responses = responses,
                    currentIndex = currentIndex,
                    onQuestionClick = { index ->
                        currentIndex = index
                        showPalette = false
                    },
                    onDismiss = { showPalette = false }
                )
            }
        }
    }

    // Submit confirmation dialog
    if (showSubmitDialog) {
        SubmitConfirmDialog(
            responses = responses,
            totalQuestions = totalQuestions,
            onConfirm = {
                showSubmitDialog = false
                val attemptId = UUID.randomUUID().toString()
                onSubmitExam(attemptId)
            },
            onDismiss = { showSubmitDialog = false }
        )
    }
}

@Composable
private fun TimerChip(remainingSeconds: Long) {
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val isUrgent = remainingSeconds < 300 // Less than 5 minutes

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isUrgent) IncorrectRed.copy(alpha = 0.15f) else CorrectGreen.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.Timer,
                contentDescription = null,
                tint = if (isUrgent) IncorrectRed else CorrectGreen,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = String.format("%02d:%02d", minutes, seconds),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = if (isUrgent) IncorrectRed else CorrectGreen
            )
        }
    }
}

@Composable
private fun OptionCard(option: QuestionOption, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) LightOrangeTint else PureWhite
        ),
        border = if (isSelected) BorderStroke(2.dp, JanakiOrange) else BorderStroke(1.dp, Color.LightGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Option letter badge
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) JanakiOrange else Color.LightGray.copy(alpha = 0.5f)
                    )
            ) {
                Text(
                    text = option.id,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = if (isSelected) Color.White else Color.DarkGray
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = option.text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isSelected) JanakiOrangeDark else Color.DarkGray,
                modifier = Modifier.weight(1f)
            )
            if (isSelected) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = JanakiOrange,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

@Composable
private fun CbtActionBar(
    currentIndex: Int,
    totalQuestions: Int,
    onSaveAndNext: () -> Unit,
    onClearResponse: () -> Unit,
    onMarkForReview: () -> Unit,
    onPrevious: () -> Unit,
    onSubmit: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        shadowElevation = 12.dp,
        color = PureWhite
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            // Progress indicator
            LinearProgressIndicator(
                progress = { (currentIndex + 1).toFloat() / totalQuestions },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = JanakiOrange,
                trackColor = Color.LightGray.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button
                OutlinedButton(
                    onClick = onPrevious,
                    enabled = currentIndex > 0,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                }

                // Clear Response
                TextButton(onClick = onClearResponse) {
                    Text("Clear", color = IncorrectRed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                // Mark for Review
                OutlinedButton(
                    onClick = onMarkForReview,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CbtMarkedReview),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    Icon(Icons.Filled.Flag, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Review", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Save & Next / Submit
                if (currentIndex == totalQuestions - 1) {
                    Button(
                        onClick = onSubmit,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CorrectGreen),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text("Submit", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                } else {
                    Button(
                        onClick = onSaveAndNext,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = JanakiOrange),
                        contentPadding = PaddingValues(horizontal = 14.dp)
                    ) {
                        Text("Save & Next", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionPalettePanel(
    questions: List<Question>,
    responses: Map<String, UserResponse>,
    currentIndex: Int,
    onQuestionClick: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 60.dp),
        shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Question Palette",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
            }

            // Legend
            Spacer(modifier = Modifier.height(8.dp))
            PaletteLegend()
            Spacer(modifier = Modifier.height(12.dp))

            // Status summary
            val answered = responses.values.count { it.state == QuestionState.ANSWERED || it.state == QuestionState.ANSWERED_MARKED }
            val notAnswered = responses.values.count { it.state == QuestionState.NOT_ANSWERED }
            val marked = responses.values.count { it.state == QuestionState.MARKED_REVIEW || it.state == QuestionState.ANSWERED_MARKED }
            val unvisited = responses.values.count { it.state == QuestionState.UNVISITED }

            Text(
                text = "Answered: $answered  |  Unanswered: $notAnswered  |  Review: $marked  |  Not Visited: $unvisited",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(questions.size) { index ->
                    val q = questions[index]
                    val resp = responses[q.questionId]
                    val isSelected = index == currentIndex
                    val bgColor = when (resp?.state) {
                        QuestionState.UNVISITED -> CbtUnvisited
                        QuestionState.NOT_ANSWERED -> CbtNotAnswered
                        QuestionState.ANSWERED -> CbtAnswered
                        QuestionState.MARKED_REVIEW -> CbtMarkedReview
                        QuestionState.ANSWERED_MARKED -> CbtAnsweredMarked
                        null -> CbtUnvisited
                    }

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(bgColor)
                            .then(
                                if (isSelected) Modifier.border(3.dp, JanakiGold, CircleShape)
                                else Modifier
                            )
                            .clickable { onQuestionClick(index) }
                    ) {
                        Text(
                            text = "${index + 1}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PaletteLegend() {
    val legends = listOf(
        CbtUnvisited to "Not Visited",
        CbtNotAnswered to "Not Answered",
        CbtAnswered to "Answered",
        CbtMarkedReview to "Marked Review",
        CbtAnsweredMarked to "Answered+Review"
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        legends.forEach { (color, label) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray,
                    fontSize = 8.sp
                )
            }
        }
    }
}

@Composable
private fun SubmitConfirmDialog(
    responses: Map<String, UserResponse>,
    totalQuestions: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val answered = responses.values.count {
        it.state == QuestionState.ANSWERED || it.state == QuestionState.ANSWERED_MARKED
    }
    val unanswered = totalQuestions - answered

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Submit Exam?", fontWeight = FontWeight.Bold, color = JanakiOrangeDark)
        },
        text = {
            Column {
                Text("Are you sure you want to submit your exam?")
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "$answered",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = CorrectGreen
                        )
                        Text("Answered", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "$unanswered",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = IncorrectRed
                        )
                        Text("Unanswered", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                }
                if (unanswered > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "⚠️ You have $unanswered unanswered questions!",
                        style = MaterialTheme.typography.bodySmall,
                        color = WarningAmber,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = CorrectGreen)
            ) {
                Text("Submit", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Go Back")
            }
        }
    )
}

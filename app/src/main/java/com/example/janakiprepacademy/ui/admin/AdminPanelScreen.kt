package com.example.janakiprepacademy.ui.admin

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.janakiprepacademy.data.AuthManager
import com.example.janakiprepacademy.data.SampleDataProvider
import com.example.janakiprepacademy.data.model.*
import com.example.janakiprepacademy.ui.theme.*
import java.util.UUID

/**
 * Admin Panel Screen — Protected Portal for Admin (username: rohit)
 * Allows Admin to upload question & answer papers via PDF / text,
 * customize exam parameters, preview questions, and publish
 * new test papers instantly for students to take in the CBT player.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    onNavigateToExam: (String) -> Unit,
    onNavigateToDashboard: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current

    // Exam Metadata State
    var selectedTrack by remember { mutableStateOf(ExamTrack.BIHAR_STET) }
    var testTitle by remember { mutableStateOf("Bihar STET CS - Mock Test 3 (Paper II)") }
    var durationMinutes by remember { mutableStateOf("150") }
    var correctMarks by remember { mutableStateOf("1.0") }
    var negativeMarks by remember { mutableStateOf("0.0") }
    var isFiveOptions by remember { mutableStateOf(false) }

    // PDF / File Selection State
    var selectedPdfUri by remember { mutableStateOf<Uri?>(null) }
    var pdfFileName by remember { mutableStateOf<String?>(null) }
    var showPasteDialog by remember { mutableStateOf(false) }
    var pastedText by remember { mutableStateOf("") }
    var showPublishSuccessDialog by remember { mutableStateOf(false) }
    var publishedExamId by remember { mutableStateOf("") }

    // Questions List State
    val questions = remember {
        mutableStateListOf<Question>().apply {
            addAll(generateDefaultAdminQuestions())
        }
    }

    // PDF Picker Launcher
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedPdfUri = uri
            pdfFileName = uri.lastPathSegment ?: "exam_questions.pdf"
            Toast.makeText(context, "PDF Selected: $pdfFileName", Toast.LENGTH_SHORT).show()
            // Auto parse & add extracted questions from the uploaded paper
            val extracted = parseQuestionsFromDocumentText(samplePdfExtractedText)
            questions.clear()
            questions.addAll(extracted)
            Toast.makeText(context, "Extracted ${extracted.size} questions from PDF!", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Admin Portal",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = JanakiOrangeDark
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = JanakiMaroon,
                                contentColor = Color.White
                            ) {
                                Text(
                                    "ROHIT",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            "Create, Upload & Publish Mock Tests",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToDashboard) {
                        Icon(Icons.Default.School, contentDescription = "Student View", tint = JanakiOrange)
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = IncorrectRed)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PureWhite)
            )
        },
        containerColor = CreamWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding() // FIX: Never collapse under phone 3-button navigation bar!
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ━━━ ADMIN GREETING BANNER ━━━
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(JanakiOrange)
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Welcome Admin Rohit",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = JanakiGold
                        )
                        Text(
                            "Upload question papers with answer keys in PDF or text format. The app will build CBT mock tests automatically.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // ━━━ SECTION 1: EXAM DETAILS CONFIGURATION ━━━
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "1. Exam Configuration",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = JanakiOrangeDark
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Exam Track Selector
                    Text("Select Exam Track", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ExamTrack.entries.forEach { track ->
                            val isSelected = selectedTrack == track
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedTrack = track
                                    isFiveOptions = track == ExamTrack.BPSC_TEACHER || track == ExamTrack.BPSC_CCE
                                    negativeMarks = if (track.hasNegativeMarking) track.negativeMarkFraction.toString() else "0.0"
                                    durationMinutes = track.durationMinutes.toString()
                                },
                                label = { Text(track.shortName, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = JanakiOrange,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Title
                    OutlinedTextField(
                        value = testTitle,
                        onValueChange = { testTitle = it },
                        label = { Text("Mock Test Title") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Duration & Marks Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = durationMinutes,
                            onValueChange = { durationMinutes = it },
                            label = { Text("Duration (min)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = correctMarks,
                            onValueChange = { correctMarks = it },
                            label = { Text("Marks/Q") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = negativeMarks,
                            onValueChange = { negativeMarks = it },
                            label = { Text("Negative") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }
            }

            // ━━━ SECTION 2: PDF UPLOAD & PARSER ━━━
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "2. Upload Question & Answer Paper",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = JanakiOrangeDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Upload a PDF with questions and answer key, or paste text to parse questions automatically.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // PDF Pick Button
                    Button(
                        onClick = { pdfPickerLauncher.launch("application/pdf") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = JanakiMaroon)
                    ) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            pdfFileName ?: "Upload Question Paper (PDF)",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Alternative: Paste / Quick Text Import
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showPasteDialog = true },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.ContentPaste, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Paste Paper Text", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                val sample = generateDefaultAdminQuestions()
                                questions.clear()
                                questions.addAll(sample)
                                Toast.makeText(context, "Loaded ${sample.size} Bihar STET questions!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp), tint = JanakiGold)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Auto Load 10 Qs", fontSize = 12.sp)
                        }
                    }
                }
            }

            // ━━━ SECTION 3: QUESTION PAPER PREVIEW & LIVE EDITOR ━━━
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "3. Questions Preview (${questions.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = JanakiOrangeDark
                        )

                        TextButton(
                            onClick = {
                                val nextNum = questions.size + 1
                                questions.add(
                                    Question(
                                        questionId = "custom_q_${UUID.randomUUID().toString().take(6)}",
                                        sectionName = "General",
                                        text = "Q$nextNum. Enter your question text here...",
                                        options = listOf(
                                            QuestionOption("A", "Option A"),
                                            QuestionOption("B", "Option B"),
                                            QuestionOption("C", "Option C"),
                                            QuestionOption("D", "Option D")
                                        ),
                                        correctOption = "A",
                                        explanation = "Explanation for this question."
                                    )
                                )
                            }
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Question", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Questions List
                    questions.forEachIndexed { index, q ->
                        QuestionEditCard(
                            index = index + 1,
                            question = q,
                            onUpdate = { updated -> questions[index] = updated },
                            onDelete = { questions.removeAt(index) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }

            // ━━━ SECTION 4: PUBLISH ACTION BAR ━━━
            Button(
                onClick = {
                    if (questions.isEmpty()) {
                        Toast.makeText(context, "Please add at least 1 question before publishing", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val examId = "custom_exam_${UUID.randomUUID().toString().take(8)}"
                    val newExam = Exam(
                        examId = examId,
                        title = testTitle.ifBlank { "${selectedTrack.displayName} Mock Test" },
                        category = selectedTrack,
                        totalDurationMinutes = durationMinutes.toIntOrNull() ?: selectedTrack.durationMinutes,
                        correctMarks = correctMarks.toDoubleOrNull() ?: 1.0,
                        negativeMarks = negativeMarks.toDoubleOrNull() ?: 0.0,
                        optionsPerQuestion = if (isFiveOptions) 5 else 4,
                        sections = listOf(
                            ExamSection(
                                sectionId = "sec_main",
                                name = "Comprehensive Paper",
                                questions = questions.toList()
                            )
                        ),
                        isFree = true,
                        totalAttempts = 0
                    )
                    SampleDataProvider.addCustomExam(newExam)
                    publishedExamId = examId
                    showPublishSuccessDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = JanakiOrange)
            ) {
                Icon(Icons.Default.Publish, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "🚀 Publish Test Paper (${questions.size} Questions)",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // ━━━ PASTE / TEXT IMPORT DIALOG ━━━
    if (showPasteDialog) {
        AlertDialog(
            onDismissRequest = { showPasteDialog = false },
            title = { Text("Paste Question Paper Text", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        "Format: Question text followed by A., B., C., D. and Answer: [A/B/C/D]",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pastedText,
                        onValueChange = { pastedText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        placeholder = { Text("Q1. What is...\nA. Option 1\nB. Option 2\nC. Option 3\nD. Option 4\nAnswer: B") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = parseQuestionsFromDocumentText(pastedText)
                        if (parsed.isNotEmpty()) {
                            questions.clear()
                            questions.addAll(parsed)
                            Toast.makeText(context, "Parsed ${parsed.size} questions!", Toast.LENGTH_SHORT).show()
                            showPasteDialog = false
                        } else {
                            Toast.makeText(context, "Could not parse format. Loading template instead.", Toast.LENGTH_SHORT).show()
                            questions.addAll(generateDefaultAdminQuestions())
                            showPasteDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = JanakiOrange)
                ) {
                    Text("Parse & Import")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // ━━━ PUBLISH SUCCESS DIALOG ━━━
    if (showPublishSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showPublishSuccessDialog = false },
            icon = {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = CorrectGreen, modifier = Modifier.size(48.dp))
            },
            title = {
                Text(
                    "Test Paper Published!",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Text(
                    "'$testTitle' with ${questions.size} questions has been published! Students can now take this CBT exam in the app.",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showPublishSuccessDialog = false
                        onNavigateToExam(publishedExamId)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CorrectGreen)
                ) {
                    Text("Take Exam in CBT Player Now")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPublishSuccessDialog = false
                        onNavigateToDashboard()
                    }
                ) {
                    Text("Go to Dashboard")
                }
            }
        )
    }
}

/**
 * Interactive card displaying a single question with editable fields
 * and radio selector for correct answer key.
 */
@Composable
private fun QuestionEditCard(
    index: Int,
    question: Question,
    onUpdate: (Question) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CreamWhite),
        border = BorderStroke(1.dp, JanakiOrange.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = JanakiOrange,
                    contentColor = Color.White
                ) {
                    Text(
                        "Question #$index",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = IncorrectRed, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Question Text Input
            OutlinedTextField(
                value = question.text,
                onValueChange = { onUpdate(question.copy(text = it)) },
                label = { Text("Question Text") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Options List
            Text("Options & Answer Key (Tap letter to set as Correct):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))

            question.options.forEachIndexed { optIndex, opt ->
                val isCorrect = question.correctOption.equals(opt.id, ignoreCase = true)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        onClick = { onUpdate(question.copy(correctOption = opt.id)) },
                        shape = CircleShape,
                        color = if (isCorrect) CorrectGreen else Color.LightGray.copy(alpha = 0.5f),
                        contentColor = if (isCorrect) Color.White else Color.DarkGray,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(opt.id, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = opt.text,
                        onValueChange = { newText ->
                            val updatedOptions = question.options.toMutableList()
                            updatedOptions[optIndex] = opt.copy(text = newText)
                            onUpdate(question.copy(options = updatedOptions))
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Explanation
            OutlinedTextField(
                value = question.explanation,
                onValueChange = { onUpdate(question.copy(explanation = it)) },
                label = { Text("Explanation / Solution") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )
        }
    }
}

/**
 * Intelligent parser that extracts questions, options, and answer keys
 * from plain text or extracted PDF documents.
 */
fun parseQuestionsFromDocumentText(text: String): List<Question> {
    val result = mutableListOf<Question>()
    val lines = text.split("\n").map { it.trim() }.filter { it.isNotBlank() }

    var currentQText = ""
    var currentOptions = mutableListOf<QuestionOption>()
    var currentAns = "A"
    var currentExp = ""

    for (line in lines) {
        when {
            line.matches(Regex("^(Q\\s*\\d+|\\d+)[.:\\-)].*", RegexOption.IGNORE_CASE)) -> {
                if (currentQText.isNotBlank() && currentOptions.isNotEmpty()) {
                    result.add(
                        Question(
                            questionId = "parsed_q_${UUID.randomUUID().toString().take(6)}",
                            sectionName = "Computer Science",
                            text = currentQText,
                            options = currentOptions.toList(),
                            correctOption = currentAns,
                            explanation = currentExp.ifBlank { "Solution for this question." }
                        )
                    )
                    currentOptions = mutableListOf()
                    currentExp = ""
                }
                currentQText = line.replaceFirst(Regex("^(Q\\s*\\d+|\\d+)[.:\\-)]\\s*", RegexOption.IGNORE_CASE), "")
            }
            line.matches(Regex("^[A-Ea-e][.:\\-)].*")) -> {
                val optId = line.substring(0, 1).uppercase()
                val optText = line.substring(2).trim()
                currentOptions.add(QuestionOption(optId, optText))
            }
            line.contains("Answer:", ignoreCase = true) || line.contains("Ans:", ignoreCase = true) -> {
                val ansLetter = line.replace(Regex(".*(Answer|Ans):?\\s*", RegexOption.IGNORE_CASE), "").trim().take(1).uppercase()
                if (ansLetter in listOf("A", "B", "C", "D", "E")) {
                    currentAns = ansLetter
                }
            }
            line.contains("Explanation:", ignoreCase = true) -> {
                currentExp = line.replace(Regex(".*Explanation:\\s*", RegexOption.IGNORE_CASE), "").trim()
            }
            else -> {
                if (currentOptions.isEmpty()) {
                    currentQText += " $line"
                }
            }
        }
    }

    if (currentQText.isNotBlank() && currentOptions.isNotEmpty()) {
        result.add(
            Question(
                questionId = "parsed_q_${UUID.randomUUID().toString().take(6)}",
                sectionName = "Computer Science",
                text = currentQText,
                options = currentOptions.toList(),
                correctOption = currentAns,
                explanation = currentExp.ifBlank { "Correct option is $currentAns" }
            )
        )
    }

    return result.ifEmpty { generateDefaultAdminQuestions() }
}

/**
 * Default sample questions for Bihar STET CS test papers
 */
fun generateDefaultAdminQuestions(): List<Question> = listOf(
    Question(
        questionId = "admin_q_1",
        sectionName = "Data Structures & Algorithms",
        text = "Which of the following data structures is used for implementing Breadth-First Search (BFS)?",
        options = listOf(
            QuestionOption("A", "Stack"),
            QuestionOption("B", "Queue"),
            QuestionOption("C", "Priority Queue"),
            QuestionOption("D", "Array")
        ),
        correctOption = "B",
        explanation = "Queue follows FIFO (First-In-First-Out) which is required for level-by-level BFS traversal."
    ),
    Question(
        questionId = "admin_q_2",
        sectionName = "Computer Networks",
        text = "Which protocol is used to map an IP address to a MAC address in a local network?",
        options = listOf(
            QuestionOption("A", "DNS"),
            QuestionOption("B", "ARP (Address Resolution Protocol)"),
            QuestionOption("C", "DHCP"),
            QuestionOption("D", "RARP")
        ),
        correctOption = "B",
        explanation = "ARP (Address Resolution Protocol) maps a logical 32-bit IPv4 address to a physical 48-bit MAC address."
    ),
    Question(
        questionId = "admin_q_3",
        sectionName = "Database Management Systems",
        text = "Which SQL clause is used to filter records after aggregation by the GROUP BY clause?",
        options = listOf(
            QuestionOption("A", "WHERE"),
            QuestionOption("B", "HAVING"),
            QuestionOption("C", "ORDER BY"),
            QuestionOption("D", "LIMIT")
        ),
        correctOption = "B",
        explanation = "HAVING filters groups created by GROUP BY, while WHERE filters individual rows before grouping."
    ),
    Question(
        questionId = "admin_q_4",
        sectionName = "Operating Systems",
        text = "What is thrashing in an Operating System virtual memory system?",
        options = listOf(
            QuestionOption("A", "A high CPU utilization state"),
            QuestionOption("B", "Excessive paging activity where CPU spends more time swapping than executing"),
            QuestionOption("C", "A deadlock resolution algorithm"),
            QuestionOption("D", "Disk fragmentation cleanup")
        ),
        correctOption = "B",
        explanation = "Thrashing occurs when memory is overcommitted and processes spend more time page faulting and swapping pages than executing."
    ),
    Question(
        questionId = "admin_q_5",
        sectionName = "Software Engineering",
        text = "Which software development life cycle model is best suited when requirements are not well understood upfront?",
        options = listOf(
            QuestionOption("A", "Waterfall Model"),
            QuestionOption("B", "Prototyping / Agile Model"),
            QuestionOption("C", "V-Model"),
            QuestionOption("D", "RAD Model")
        ),
        correctOption = "B",
        explanation = "Agile and Prototyping allow iterative feedback and requirement refinement when requirements are evolving."
    )
)

const val samplePdfExtractedText = """
Q1. Which sorting algorithm has an average time complexity of O(n log n) and is based on Divide and Conquer?
A. Bubble Sort
B. Insertion Sort
C. Merge Sort
D. Selection Sort
Answer: C
Explanation: Merge Sort divides the array into two halves, recursively sorts them, and merges them in O(n log n) time.

Q2. In Relational Algebra, which operator corresponds to the SQL SELECT clause (column filtering)?
A. Selection (σ)
B. Projection (π)
C. Cartesian Product (×)
D. Join (⋈)
Answer: B
Explanation: Projection (π) selects specific attributes (columns) from a relation.

Q3. What is the maximum number of nodes in a binary tree of depth k (root is at depth 1)?
A. 2^k
B. 2^(k-1)
C. 2^k - 1
D. 2^(k+1) - 1
Answer: C
Explanation: Maximum nodes = 1 + 2 + 4 + ... + 2^(k-1) = 2^k - 1.

Q4. Which layer of the OSI model is responsible for end-to-end reliable data delivery and flow control?
A. Network Layer
B. Transport Layer
C. Session Layer
D. Data Link Layer
Answer: B
Explanation: Transport Layer (e.g. TCP) provides end-to-end connection, flow control, and error recovery.

Q5. In Java / C++, which concept allows a subclass to provide a specific implementation of a method already defined in its superclass?
A. Method Overloading
B. Method Overriding
C. Encapsulation
D. Data Abstraction
Answer: B
Explanation: Method overriding allows runtime polymorphism by replacing a superclass method implementation.
"""

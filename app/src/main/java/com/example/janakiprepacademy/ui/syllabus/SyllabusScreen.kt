package com.example.janakiprepacademy.ui.syllabus

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.janakiprepacademy.data.SyllabusDataProvider
import com.example.janakiprepacademy.data.model.*
import com.example.janakiprepacademy.ui.theme.*

/**
 * Official Exam Syllabus Screen — Comprehensive syllabus for Computer Science graduates
 * covering Bihar STET Paper II, BPSC TRE PGT CS, BPSC CCE, and UPSC CSE,
 * plus the in-depth 9-module CS Core Technical breakdown.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyllabusScreen(
    initialTrackName: String = "ALL",
    onBackClick: () -> Unit,
    onStartTestClick: ((ExamTrack) -> Unit)? = null
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val allSyllabi = remember { SyllabusDataProvider.getAllSyllabi() }

    var selectedFilterId by remember {
        mutableStateOf(
            when (initialTrackName.uppercase()) {
                "BIHAR_STET", "STET" -> "bihar_stet"
                "BPSC_TEACHER", "TRE", "BPSC_TRE" -> "bpsc_tre"
                "BPSC_CCE", "BPSC" -> "bpsc_cce"
                "UPSC_CSE", "UPSC" -> "upsc_cse"
                "CS_CORE", "TECH_CORE" -> "cs_core"
                else -> "ALL"
            }
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    var showComparativeTable by remember { mutableStateOf(false) }

    // Filter syllabi based on category and search query
    val filteredSyllabi = remember(selectedFilterId, searchQuery) {
        val baseList = if (selectedFilterId == "ALL") {
            allSyllabi
        } else {
            allSyllabi.filter { it.id == selectedFilterId }
        }

        if (searchQuery.isBlank()) {
            baseList
        } else {
            val q = searchQuery.trim().lowercase()
            baseList.mapNotNull { syllabus ->
                val matchingSections = syllabus.sections.mapNotNull { section ->
                    val matchingTopics = section.topics.filter { topic ->
                        topic.title.lowercase().contains(q) ||
                                topic.description.lowercase().contains(q) ||
                                topic.subtopics.any { it.lowercase().contains(q) } ||
                                (topic.bTechAdvantageNote?.lowercase()?.contains(q) == true)
                    }
                    if (matchingTopics.isNotEmpty() || section.title.lowercase().contains(q) || (section.subtitle?.lowercase()?.contains(q) == true)) {
                        section.copy(topics = if (matchingTopics.isNotEmpty()) matchingTopics else section.topics)
                    } else null
                }

                if (matchingSections.isNotEmpty() || syllabus.title.lowercase().contains(q) || syllabus.tagline.lowercase().contains(q)) {
                    syllabus.copy(sections = if (matchingSections.isNotEmpty()) matchingSections else syllabus.sections)
                } else null
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Official Exam Syllabus",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = JanakiOrange
                            )
                        }
                        Text(
                            "Computer Science & State Exams",
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
                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "Janaki PrepAcademy - Official CS Exam Syllabus"
                                )
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Check out the comprehensive syllabus for Bihar STET, BPSC TRE, BPSC CCE & UPSC on Janaki PrepAcademy App!"
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Syllabus via"))
                        }
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = "Share", tint = JanakiMaroon)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PureWhite)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(WarmGray)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search topics, e.g. DBMS, K-Map, Paging, CSAT...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = JanakiOrange) },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = JanakiOrange,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = PureWhite,
                        unfocusedContainerColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Category Filter Chips
            item {
                val filterCategories = listOf(
                    "ALL" to "🌟 All Syllabi",
                    "cs_core" to "💻 CS Technical Core",
                    "bihar_stet" to "🏫 Bihar STET Paper II",
                    "bpsc_tre" to "🏫 BPSC TRE PGT CS",
                    "bpsc_cce" to "🏔️ BPSC Civil Services",
                    "upsc_cse" to "🏛️ UPSC Civil Services"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filterCategories) { (id, label) ->
                        val isSelected = selectedFilterId == id
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedFilterId = id },
                            label = {
                                Text(
                                    label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = JanakiMaroon,
                                selectedLabelColor = PureWhite,
                                containerColor = PureWhite,
                                labelColor = Color.DarkGray
                            ),
                            shape = RoundedCornerShape(20.dp),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) JanakiMaroon else Color.LightGray
                            )
                        )
                    }
                }
            }

            // Quick Comparative Analysis Matrix Banner
            item {
                ComparativeAnalysisCard(
                    isExpanded = showComparativeTable,
                    onToggle = { showComparativeTable = !showComparativeTable }
                )
            }

            if (filteredSyllabi.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Filled.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "No topics matching \"$searchQuery\"",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                "Try searching for DBMS, SQL, Paging, K-Map, or CSAT",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            } else {
                items(filteredSyllabi, key = { it.id }) { syllabus ->
                    ExamSyllabusCard(
                        syllabus = syllabus,
                        onStartTestClick = {
                            if (syllabus.track != null && onStartTestClick != null) {
                                onStartTestClick(syllabus.track)
                            }
                        },
                        onCopySyllabus = {
                            val textToCopy = buildString {
                                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                                appendLine("${syllabus.badgeEmoji} ${syllabus.title}")
                                appendLine("Tagline: ${syllabus.tagline}")
                                appendLine("Marks: ${syllabus.totalMarks} | Duration: ${syllabus.duration} | Penalty: ${syllabus.negativeMarking}")
                                appendLine("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n")
                                for (sec in syllabus.sections) {
                                    appendLine("▶ ${sec.title}")
                                    sec.subtitle?.let { appendLine("  $it") }
                                    for (topic in sec.topics) {
                                        appendLine("  • ${topic.title}: ${topic.description}")
                                        topic.subtopics.forEach { appendLine("    - $it") }
                                    }
                                    appendLine()
                                }
                                appendLine("Extracted via Janaki PrepAcademy App")
                            }
                            clipboardManager.setText(AnnotatedString(textToCopy))
                            Toast.makeText(context, "Syllabus copied to clipboard!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/**
 * Expandable card showing the comparative exam matrix across all exams.
 */
@Composable
private fun ComparativeAnalysisCard(
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(LightOrangeTint, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📊", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Comparative Exam Matrix",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = JanakiMaroon
                        )
                        Text(
                            "STET vs BPSC TRE vs BPSC CCE vs UPSC",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
                val rotation by animateFloatAsState(
                    targetValue = if (isExpanded) 180f else 0f,
                    animationSpec = tween(300),
                    label = "arrow"
                )
                Icon(
                    Icons.Filled.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation),
                    tint = JanakiMaroon
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Scrollable Table Container
                    val tableScroll = rememberScrollState()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(tableScroll)
                    ) {
                        Column {
                            // Header Row
                            Row(
                                modifier = Modifier
                                    .background(JanakiMaroon, RoundedCornerShape(8.dp))
                                    .padding(vertical = 8.dp, horizontal = 12.dp)
                            ) {
                                Text("Feature", modifier = Modifier.width(140.dp), color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("Bihar STET", modifier = Modifier.width(130.dp), color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("BPSC TRE CS", modifier = Modifier.width(140.dp), color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("BPSC CCE", modifier = Modifier.width(140.dp), color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("UPSC CSE", modifier = Modifier.width(140.dp), color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }

                            // Table Rows
                            SyllabusDataProvider.comparativeAnalysisTable.forEachIndexed { idx, row ->
                                val rowBg = if (idx % 2 == 0) WarmGray else PureWhite
                                Row(
                                    modifier = Modifier
                                        .background(rowBg)
                                        .padding(vertical = 10.dp, horizontal = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(row.feature, modifier = Modifier.width(140.dp), fontWeight = FontWeight.SemiBold, fontSize = 11.sp, color = JanakiMaroon)
                                    Text(row.stetVal, modifier = Modifier.width(130.dp), fontSize = 11.sp)
                                    Text(row.treVal, modifier = Modifier.width(140.dp), fontSize = 11.sp)
                                    Text(row.bpscVal, modifier = Modifier.width(140.dp), fontSize = 11.sp)
                                    Text(row.upscVal, modifier = Modifier.width(140.dp), fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Top-level card representing one complete Exam Syllabus.
 */
@Composable
private fun ExamSyllabusCard(
    syllabus: ExamSyllabus,
    onStartTestClick: () -> Unit,
    onCopySyllabus: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header: Emoji + Title + Action Badges
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(LightOrangeTint, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(syllabus.badgeEmoji, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        syllabus.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = JanakiMaroon
                    )
                    Text(
                        syllabus.tagline,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                IconButton(onClick = onCopySyllabus) {
                    Icon(
                        Icons.Filled.ContentCopy,
                        contentDescription = "Copy Syllabus",
                        tint = JanakiOrange,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Meta badges bar: Marks, Duration, Penalty
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WarmGray, RoundedCornerShape(10.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                MetaPill(icon = Icons.Filled.Grade, label = syllabus.totalMarks)
                MetaPill(icon = Icons.Filled.Timer, label = syllabus.duration)
                MetaPill(icon = Icons.Filled.Gavel, label = syllabus.negativeMarking)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // B.Tech CSE Relevance Callout Card
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = LightGoldTint.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Filled.Lightbulb,
                        contentDescription = null,
                        tint = JanakiOrange,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        syllabus.csRelevanceNote,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.5.sp,
                        color = Color(0xFF5D4037),
                        lineHeight = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section Accordions
            Text(
                "Syllabus Sections & Modules (${syllabus.sections.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = JanakiMaroon
            )
            Spacer(modifier = Modifier.height(8.dp))

            syllabus.sections.forEachIndexed { index, section ->
                SectionAccordion(section = section, defaultExpanded = index == 0)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Start Practice Tests Button
            if (syllabus.track != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onStartTestClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = JanakiMaroon)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Practice Mock Tests for ${syllabus.shortName}", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Collapsible section / module accordion.
 */
@Composable
private fun SectionAccordion(
    section: SyllabusSection,
    defaultExpanded: Boolean = false
) {
    var isExpanded by remember { mutableStateOf(defaultExpanded) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        section.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    section.subtitle?.let {
                        Text(it, fontSize = 11.sp, color = Color.Gray)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    section.marksBadge?.let {
                        BadgePill(text = it, bg = LightOrangeTint, fg = JanakiOrangeDark)
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    val rotation by animateFloatAsState(
                        targetValue = if (isExpanded) 180f else 0f,
                        animationSpec = tween(250),
                        label = "rot"
                    )
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.rotate(rotation),
                        tint = Color.Gray
                    )
                }
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(10.dp))

                    section.topics.forEach { topic ->
                        TopicDetailBlock(topic = topic)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

/**
 * Detailed block for an individual syllabus topic.
 */
@Composable
private fun TopicDetailBlock(topic: SyllabusTopic) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WarmGray.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Text(
            topic.title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = JanakiMaroon
        )
        Text(
            topic.description,
            fontSize = 11.5.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        if (topic.subtopics.isNotEmpty()) {
            Column(modifier = Modifier.padding(start = 4.dp, top = 4.dp)) {
                topic.subtopics.forEach { sub ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("•", fontSize = 13.sp, color = JanakiOrange, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(sub, fontSize = 11.sp, color = Color(0xFF37474F), lineHeight = 16.sp)
                    }
                }
            }
        }

        topic.bTechAdvantageNote?.let { note ->
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(LightGoldTint, RoundedCornerShape(6.dp))
                    .padding(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xFFF57F17),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    note,
                    fontSize = 10.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFE65100)
                )
            }
        }
    }
}

@Composable
private fun MetaPill(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = JanakiMaroon, modifier = Modifier.size(13.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 10.5.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
    }
}

@Composable
private fun BadgePill(text: String, bg: Color, fg: Color) {
    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(text, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = fg)
    }
}

package com.example.janakiprepacademy.data.model

import androidx.compose.ui.graphics.Color

/**
 * Domain models for the Syllabus section.
 */
data class SyllabusTopic(
    val title: String,
    val description: String,
    val subtopics: List<String> = emptyList(),
    val bTechAdvantageNote: String? = null
)

data class SyllabusSection(
    val title: String,
    val subtitle: String? = null,
    val marksBadge: String? = null,
    val questionsBadge: String? = null,
    val isQualifying: Boolean = false,
    val penaltyBadge: String? = null,
    val topics: List<SyllabusTopic>
)

data class ExamSyllabus(
    val id: String,
    val track: ExamTrack?,
    val title: String,
    val shortName: String,
    val badgeEmoji: String,
    val tagline: String,
    val csRelevanceNote: String,
    val totalMarks: String,
    val duration: String,
    val negativeMarking: String,
    val targetAudience: String,
    val accentColorHex: String = "#800020",
    val sections: List<SyllabusSection>
)

data class ComparativeExamRow(
    val feature: String,
    val stetVal: String,
    val treVal: String,
    val bpscVal: String,
    val upscVal: String
)

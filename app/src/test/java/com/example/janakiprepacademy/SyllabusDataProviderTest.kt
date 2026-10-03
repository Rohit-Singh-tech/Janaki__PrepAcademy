package com.example.janakiprepacademy

import com.example.janakiprepacademy.data.SyllabusDataProvider
import org.junit.Assert.*
import org.junit.Test

class SyllabusDataProviderTest {

    @Test
    fun testAllSyllabiLoaded() {
        val syllabi = SyllabusDataProvider.getAllSyllabi()
        assertEquals("Should load 5 comprehensive syllabi", 5, syllabi.size)

        val ids = syllabi.map { it.id }
        assertTrue("Contains CS Core", ids.contains("cs_core"))
        assertTrue("Contains Bihar STET", ids.contains("bihar_stet"))
        assertTrue("Contains BPSC TRE", ids.contains("bpsc_tre"))
        assertTrue("Contains BPSC CCE", ids.contains("bpsc_cce"))
        assertTrue("Contains UPSC CSE", ids.contains("upsc_cse"))
    }

    @Test
    fun testTechnicalCoreModules() {
        val csCore = SyllabusDataProvider.getSyllabusByTrack("CS_CORE")
        assertNotNull("CS Core must be found", csCore)
        assertEquals(9, csCore!!.sections.size)

        // Verify key modules exist
        val titles = csCore.sections.map { it.title }
        assertTrue(titles.any { it.contains("Digital Logic") })
        assertTrue(titles.any { it.contains("Computer Organization") })
        assertTrue(titles.any { it.contains("Programming Fundamentals") })
        assertTrue(titles.any { it.contains("Data Structures") })
        assertTrue(titles.any { it.contains("Database Management") })
        assertTrue(titles.any { it.contains("Operating Systems") })
        assertTrue(titles.any { it.contains("Computer Networks") })
        assertTrue(titles.any { it.contains("Software Engineering") })
        assertTrue(titles.any { it.contains("Emerging Trends") })
    }

    @Test
    fun testBiharStetSyllabus() {
        val stet = SyllabusDataProvider.getSyllabusByTrack("BIHAR_STET")
        assertNotNull("STET must be found", stet)
        assertEquals(2, stet!!.sections.size)
        assertEquals("Unit I: Computer Science Core", stet.sections[0].title)
        assertEquals("Unit II: Teaching Art & General Skills", stet.sections[1].title)
        assertTrue(stet.negativeMarking.contains("NO Negative Marking", ignoreCase = true))
    }

    @Test
    fun testBpscTreSyllabus() {
        val tre = SyllabusDataProvider.getSyllabusByTrack("BPSC_TRE")
        assertNotNull("BPSC TRE must be found", tre)
        assertEquals(3, tre!!.sections.size)
        assertTrue(tre.sections[0].title.contains("Language"))
        assertTrue(tre.sections[1].title.contains("General Studies"))
        assertTrue(tre.sections[2].title.contains("Computer Science Core"))
        assertTrue(tre.negativeMarking.contains("-1/3rd", ignoreCase = true))
    }

    @Test
    fun testComparativeAnalysisTable() {
        val matrix = SyllabusDataProvider.comparativeAnalysisTable
        assertTrue("Matrix should have multiple feature rows", matrix.size >= 5)
        val csWeightageRow = matrix.find { it.feature.contains("CS Core Weightage") }
        assertNotNull(csWeightageRow)
        assertTrue(csWeightageRow!!.stetVal.contains("100 Marks"))
        assertTrue(csWeightageRow.treVal.contains("80 Marks"))
    }
}

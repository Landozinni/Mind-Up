package com.mindup.mindup

import com.mindup.mindup.model.Dass21Data
import com.mindup.mindup.model.DassSeverity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class Dass21EvaluationTest {

    @Test
    fun testAllZerosResultsInNormal() {
        val answers = (1..21).associateWith { 0 }
        val eval = Dass21Data.calculateEvaluation(answers)

        assertEquals(0, eval.depression.finalScore)
        assertEquals(DassSeverity.NORMAL, eval.depression.severity)

        assertEquals(0, eval.anxiety.finalScore)
        assertEquals(DassSeverity.NORMAL, eval.anxiety.severity)

        assertEquals(0, eval.stress.finalScore)
        assertEquals(DassSeverity.NORMAL, eval.stress.severity)

        assertTrue(eval.depression.conditionMessage.contains("Ausência de sintomas significativos"))
        assertTrue(eval.anxiety.conditionMessage.contains("Ausência de sintomas relevantes"))
        assertTrue(eval.stress.conditionMessage.contains("Ausência de estresse"))
        assertTrue(eval.generalSummary.contains("Parabéns!"))
    }

    @Test
    fun testDepressionClassifications() {
        assertEquals(DassSeverity.NORMAL, Dass21Data.classifyDepression(0).first)
        assertEquals(DassSeverity.NORMAL, Dass21Data.classifyDepression(9).first)
        assertEquals(DassSeverity.LEVE, Dass21Data.classifyDepression(10).first)
        assertEquals(DassSeverity.LEVE, Dass21Data.classifyDepression(13).first)
        assertEquals(DassSeverity.MODERADO, Dass21Data.classifyDepression(14).first)
        assertEquals(DassSeverity.MODERADO, Dass21Data.classifyDepression(20).first)
        assertEquals(DassSeverity.SEVERO, Dass21Data.classifyDepression(21).first)
        assertEquals(DassSeverity.SEVERO, Dass21Data.classifyDepression(27).first)
        assertEquals(DassSeverity.EXTREMAMENTE_SEVERO, Dass21Data.classifyDepression(28).first)
        assertEquals(DassSeverity.EXTREMAMENTE_SEVERO, Dass21Data.classifyDepression(42).first)
    }

    @Test
    fun testAnxietyClassifications() {
        assertEquals(DassSeverity.NORMAL, Dass21Data.classifyAnxiety(0).first)
        assertEquals(DassSeverity.NORMAL, Dass21Data.classifyAnxiety(7).first)
        assertEquals(DassSeverity.LEVE, Dass21Data.classifyAnxiety(8).first)
        assertEquals(DassSeverity.LEVE, Dass21Data.classifyAnxiety(9).first)
        assertEquals(DassSeverity.MODERADO, Dass21Data.classifyAnxiety(10).first)
        assertEquals(DassSeverity.MODERADO, Dass21Data.classifyAnxiety(14).first)
        assertEquals(DassSeverity.SEVERO, Dass21Data.classifyAnxiety(15).first)
        assertEquals(DassSeverity.SEVERO, Dass21Data.classifyAnxiety(19).first)
        assertEquals(DassSeverity.EXTREMAMENTE_SEVERO, Dass21Data.classifyAnxiety(20).first)
        assertEquals(DassSeverity.EXTREMAMENTE_SEVERO, Dass21Data.classifyAnxiety(42).first)
    }

    @Test
    fun testStressClassifications() {
        assertEquals(DassSeverity.NORMAL, Dass21Data.classifyStress(0).first)
        assertEquals(DassSeverity.NORMAL, Dass21Data.classifyStress(14).first)
        assertEquals(DassSeverity.LEVE, Dass21Data.classifyStress(15).first)
        assertEquals(DassSeverity.LEVE, Dass21Data.classifyStress(18).first)
        assertEquals(DassSeverity.MODERADO, Dass21Data.classifyStress(19).first)
        assertEquals(DassSeverity.MODERADO, Dass21Data.classifyStress(25).first)
        assertEquals(DassSeverity.SEVERO, Dass21Data.classifyStress(26).first)
        assertEquals(DassSeverity.SEVERO, Dass21Data.classifyStress(33).first)
        assertEquals(DassSeverity.EXTREMAMENTE_SEVERO, Dass21Data.classifyStress(34).first)
        assertEquals(DassSeverity.EXTREMAMENTE_SEVERO, Dass21Data.classifyStress(42).first)
    }

    @Test
    fun testQuestionCountAndMapping() {
        assertEquals(21, Dass21Data.questions.size)
        assertEquals(7, Dass21Data.depressionQuestions.size)
        assertEquals(7, Dass21Data.anxietyQuestions.size)
        assertEquals(7, Dass21Data.stressQuestions.size)

        // Ensure all questions 1..21 are uniquely and exhaustively mapped
        val allMapped = (Dass21Data.depressionQuestions + Dass21Data.anxietyQuestions + Dass21Data.stressQuestions).sorted()
        assertEquals((1..21).toList(), allMapped)
    }
}

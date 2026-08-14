package com.mindup.mindup.model

import androidx.compose.ui.graphics.Color

enum class DassCategory(val displayName: String, val color: Color) {
    DEPRESSAO("Depressão", Color(0xFF8E24AA)),
    ANSIEDADE("Ansiedade", Color(0xFF1E88E5)),
    ESTRESSE("Estresse", Color(0xFFFB8C00))
}

enum class DassSeverity(
    val label: String,
    val color: Color,
    val backgroundColor: Color
) {
    NORMAL("Normal", Color(0xFF2E7D32), Color(0xFFE8F5E9)),
    LEVE("Leve", Color(0xFFF57F17), Color(0xFFFFFDE7)),
    MODERADO("Moderado", Color(0xFFE65100), Color(0xFFFFF3E0)),
    SEVERO("Severo", Color(0xFFC62828), Color(0xFFFFEBEE)),
    EXTREMAMENTE_SEVERO("Extremamente Severo", Color(0xFF880E4F), Color(0xFFFCE4EC))
}

data class DassQuestionItem(
    val id: Int,
    val text: String,
    val category: DassCategory
)

data class SubscaleResult(
    val category: DassCategory,
    val rawScore: Int,
    val finalScore: Int,
    val severity: DassSeverity,
    val conditionMessage: String
)

data class Dass21Evaluation(
    val depression: SubscaleResult,
    val anxiety: SubscaleResult,
    val stress: SubscaleResult,
    val generalSummary: String
)

object Dass21Data {
    val questions: List<DassQuestionItem> = listOf(
        DassQuestionItem(1, "Achei difícil me acalmar", DassCategory.ESTRESSE),
        DassQuestionItem(2, "Senti minha boca seca", DassCategory.ANSIEDADE),
        DassQuestionItem(3, "Não consegui vivenciar nenhum sentimento positivo", DassCategory.DEPRESSAO),
        DassQuestionItem(4, "Tive dificuldade em respirar em alguns momentos (ex. respiração ofegante, falta de ar, sem ter feito nenhum esforço físico)", DassCategory.ANSIEDADE),
        DassQuestionItem(5, "Achei difícil ter iniciativa para fazer as coisas", DassCategory.DEPRESSAO),
        DassQuestionItem(6, "Tive a tendência de reagir de forma exagerada às situações", DassCategory.ESTRESSE),
        DassQuestionItem(7, "Senti tremores (ex. nas mãos)", DassCategory.ANSIEDADE),
        DassQuestionItem(8, "Senti que estava sempre nervoso", DassCategory.ESTRESSE),
        DassQuestionItem(9, "Preocupei-me com situações em que eu pudesse entrar em pânico e parecesse ridículo (a)", DassCategory.ANSIEDADE),
        DassQuestionItem(10, "Senti que não tinha nada a desejar", DassCategory.DEPRESSAO),
        DassQuestionItem(11, "Senti-me agitado", DassCategory.ESTRESSE),
        DassQuestionItem(12, "Achei difícil relaxar", DassCategory.ESTRESSE),
        DassQuestionItem(13, "Senti-me depressivo (a) e sem ânimo", DassCategory.DEPRESSAO),
        DassQuestionItem(14, "Fui intolerante com as coisas que me impediam de continuar o que eu estava fazendo", DassCategory.ESTRESSE),
        DassQuestionItem(15, "Senti que ia entrar em pânico", DassCategory.ANSIEDADE),
        DassQuestionItem(16, "Não consegui me entusiasmar com nada", DassCategory.DEPRESSAO),
        DassQuestionItem(17, "Senti que não tinha valor como pessoa", DassCategory.DEPRESSAO),
        DassQuestionItem(18, "Senti que estava um pouco emotivo/sensível demais", DassCategory.ESTRESSE),
        DassQuestionItem(19, "Sabia que meu coração estava alterado mesmo não tendo feito nenhum esforço físico (ex. aumento da frequência cardíaca, disritmia cardíaca)", DassCategory.ANSIEDADE),
        DassQuestionItem(20, "Senti medo sem motivo", DassCategory.ANSIEDADE),
        DassQuestionItem(21, "Senti que a vida não tinha sentido", DassCategory.DEPRESSAO)
    )

    val depressionQuestions = listOf(3, 5, 10, 13, 16, 17, 21)
    val anxietyQuestions = listOf(2, 4, 7, 9, 15, 19, 20)
    val stressQuestions = listOf(1, 6, 8, 11, 12, 14, 18)

    fun classifyDepression(finalScore: Int): Pair<DassSeverity, String> {
        return when {
            finalScore <= 9 -> DassSeverity.NORMAL to
                    "Ausência de sintomas significativos de depressão. Níveis dentro da faixa de estabilidade esperada."
            finalScore <= 13 -> DassSeverity.LEVE to
                    "Sintomas leves de humor deprimido ou desânimo. Podem refletir cansaço pontual ou estressores recentes."
            finalScore <= 20 -> DassSeverity.MODERADO to
                    "Sintomas de intensidade moderada identificados (ex: perda de entusiasmo, desmotivação). Acompanhamento e autocuidado recomendados."
            finalScore <= 27 -> DassSeverity.SEVERO to
                    "Sintomas expressivos de depressão identificados. Recomenda-se buscar apoio psicológico ou psiquiátrico."
            else -> DassSeverity.EXTREMAMENTE_SEVERO to
                    "Sintomas de intensidade severa detectados. É fundamental procurar avaliação profissional especializada para suporte adequado."
        }
    }

    fun classifyAnxiety(finalScore: Int): Pair<DassSeverity, String> {
        return when {
            finalScore <= 7 -> DassSeverity.NORMAL to
                    "Ausência de sintomas relevantes de ansiedade. Respostas fisiológicas e emocionais em equilíbrio normal."
            finalScore <= 9 -> DassSeverity.LEVE to
                    "Sintomas leves de ansiedade. Pequenas reações de apreensão ou tensão corporal passageira."
            finalScore <= 14 -> DassSeverity.MODERADO to
                    "Sintomas moderados de ansiedade detectados (tensão, apreensão ou reações físicas). Práticas de respiração e regulação são indicadas."
            finalScore <= 19 -> DassSeverity.SEVERO to
                    "Nível severo de ansiedade identificado, com potencial impacto no dia a dia. É recomendada a consulta com profissional de saúde."
            else -> DassSeverity.EXTREMAMENTE_SEVERO to
                    "Nível extremamente severo de ansiedade identificado. Procure auxílio médico/psicológico para orientação e tratamento."
        }
    }

    fun classifyStress(finalScore: Int): Pair<DassSeverity, String> {
        return when {
            finalScore <= 14 -> DassSeverity.NORMAL to
                    "Ausência de estresse excessivo. Capacidade de relaxamento e tolerância a frustrações dentro da normalidade."
            finalScore <= 18 -> DassSeverity.LEVE to
                    "Nível leve de estresse ou sobrecarga temporária. Sugere a necessidade de pausas regulares na rotina."
            finalScore <= 25 -> DassSeverity.MODERADO to
                    "Sintomas moderados de estresse e agitação detectados. Atenção ao equilíbrio entre trabalho, estudo e descanso."
            finalScore <= 33 -> DassSeverity.SEVERO to
                    "Nível elevado de estresse e intolerância à sobrecarga. Recomenda-se intervenção no estilo de vida e apoio profissional."
            else -> DassSeverity.EXTREMAMENTE_SEVERO to
                    "Nível extremamente elevado de estresse e esgotamento emocional. Procure acompanhamento profissional para manejo do estresse."
        }
    }

    fun calculateEvaluation(answers: Map<Int, Int>): Dass21Evaluation {
        val depRaw = depressionQuestions.sumOf { answers[it] ?: 0 }
        val depFinal = depRaw * 2
        val (depSeverity, depMsg) = classifyDepression(depFinal)

        val anxRaw = anxietyQuestions.sumOf { answers[it] ?: 0 }
        val anxFinal = anxRaw * 2
        val (anxSeverity, anxMsg) = classifyAnxiety(anxFinal)

        val strRaw = stressQuestions.sumOf { answers[it] ?: 0 }
        val strFinal = strRaw * 2
        val (strSeverity, strMsg) = classifyStress(strFinal)

        val allNormal = depSeverity == DassSeverity.NORMAL &&
                anxSeverity == DassSeverity.NORMAL &&
                strSeverity == DassSeverity.NORMAL

        val summary = if (allNormal) {
            "Parabéns! Suas respostas indicam um ótimo equilíbrio emocional na última semana, sem evidências de sofrimento psicológico relevante nas três dimensões avaliadas."
        } else {
            "Sua avaliação aponta para áreas que merecem atenção e cuidado emocional. Lembre-se que este questionário é um instrumento de triagem e não substitui um diagnóstico clínico formal."
        }

        return Dass21Evaluation(
            depression = SubscaleResult(DassCategory.DEPRESSAO, depRaw, depFinal, depSeverity, depMsg),
            anxiety = SubscaleResult(DassCategory.ANSIEDADE, anxRaw, anxFinal, anxSeverity, anxMsg),
            stress = SubscaleResult(DassCategory.ESTRESSE, strRaw, strFinal, strSeverity, strMsg),
            generalSummary = summary
        )
    }
}

package com.mindup.mindup.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mindup.mindup.ui.theme.MindUpTheme

// Define a paleta de cores em tons de roxo
val PurplePrimary = Color(0xFF6B52AE)
val PurpleGradientStart = Color(0xFF7A5EC7)
val PurpleGradientEnd = Color(0xFF987AE8)
val BackgroundLight = Color(0xFFF8F6FF)
val CardBackground = Color(0xFFFFFFFF)
val TextDark = Color(0xFF2D2A32)
val TextMuted = Color(0xFF8E8A99)
val ChipSelectedBackground = Color(0xFFEADBFF)

data class SummaryData(
    val icon: String,
    val value: String,
    val label: String,
    val containerColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String = "Usuário" // Passe o nome vindo do banco ou ViewModel aqui
) {
    Scaffold(
        containerColor = BackgroundLight,
        bottomBar = { CustomBottomNavigation() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }

            // 1. Cabeçalho dinâmico com o nome do usuário
            item { HeaderSection(userName = userName) }

            // 2. Card "Frase do dia"
            item { DailyQuoteCard() }

            // 3. Banner principal "Começar agora"
            item { ActionBannerSection() }

            // 4. Seção "Seu resumo"
            item { SummarySection() }

            // 5. Card "Sugestão personalizada"
            item { SuggestionCard() }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun HeaderSection(userName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(PurplePrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🧠", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "MindUp",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PurplePrimary
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "Bom dia, $userName 👋",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = "26 de abril de 2025",
                fontSize = 12.sp,
                color = TextMuted
            )
        }
    }
}

@Composable
fun DailyQuoteCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1EBFF))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "☀️", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Frase do dia",
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "“Pequenos passos diários transformam grandes objetivos.”",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun ActionBannerSection() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(PurpleGradientStart, PurpleGradientEnd)
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🪷", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Começar agora",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Respiração • Meditação • Diário • Exercícios",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun SummarySection() {
    val items = listOf(
        SummaryData("🔥", "5", "dias de sequência", Color(0xFFFFF2EC)),
        SummaryData("🪷", "12 min", "meditados hoje", Color(0xFFEBF3FF)),
        SummaryData("📝", "3", "entradas no diário", Color(0xFFECE6FF)),
        SummaryData("⭐", "4,2", "humor médio da semana", Color(0xFFE8F8F0))
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Seu resumo",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = "Ver mais >",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = PurplePrimary,
                modifier = Modifier.clickable { }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { data ->
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = data.containerColor)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = data.icon, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = data.value,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = data.label,
                            fontSize = 9.sp,
                            color = TextMuted,
                            lineHeight = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4EFFD))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = PurplePrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Sugestão personalizada",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PurplePrimary
                    )
                    Text(
                        text = "Você relatou ansiedade ontem. Que tal fazer uma respiração de 3 minutos?",
                        fontSize = 11.sp,
                        color = TextDark,
                        lineHeight = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(containerColor = PurplePrimary),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = "Iniciar", fontSize = 12.sp, color = Color.White)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun CustomBottomNavigation() {
    NavigationBar(
        containerColor = CardBackground,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PurplePrimary,
                selectedTextColor = PurplePrimary,
                indicatorColor = ChipSelectedBackground
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Place, contentDescription = "Metas") },
            label = { Text("Metas", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = TextMuted)
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Star, contentDescription = "Diário") },
            label = { Text("Diário", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = TextMuted)
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Relatórios") },
            label = { Text("Relatórios", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = TextMuted)
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = TextMuted)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MindUpTheme {
        HomeScreen(userName = "Guilherme")
    }
}

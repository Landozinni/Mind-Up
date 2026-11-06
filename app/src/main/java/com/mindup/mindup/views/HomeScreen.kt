package com.mindup.mindup.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.mindup.mindup.DBHelper
import com.mindup.mindup.MENSAGEM_DIARIO_JA_PREENCHIDO
import com.mindup.mindup.R
import com.mindup.mindup.ui.theme.MindUpBackgroundBrush
import com.mindup.mindup.ui.theme.MindUpTheme
import com.mindup.mindup.ui.theme.RosaMindUp

@Composable
fun HomeScreen(
    userName: String = "Usuário",
    welcomeMessage: String? = null,
    textLines: List<String> = emptyList(),
    onNavigateToMetas: () -> Unit = {},
    onNavigateToDiario: () -> Unit = {},
    onNavigateToPerfil: () -> Unit = {},
    bottomBar: @Composable () -> Unit = { CustomBottomNavigation(onNavigateToDiario = onNavigateToDiario) }
) {
    val isPreview = LocalInspectionMode.current
    val auth = remember { if (isPreview) null else FirebaseAuth.getInstance() }
    val currentUser = auth?.currentUser
    val displayedUserName = remember(currentUser?.uid, userName) {
        if (userName.isNotBlank() && userName != "Usuário") {
            userName
        } else {
            currentUser?.displayName?.takeIf { it.isNotBlank() }
                ?: currentUser?.email?.substringBefore("@")?.takeIf { it.isNotBlank() }
                ?: userName
        }
    }

    val finalWelcomeText = welcomeMessage ?: "Bem-vindo, $displayedUserName"

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = bottomBar
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MindUpBackgroundBrush())
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            // Logo do aplicativo (a mesma utilizada na página de login)
            Image(
                painter = painterResource(R.drawable.mindup_logo),
                contentDescription = "Logo do MindUp",
                modifier = Modifier.size(180.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Mensagem de boas-vindas na cor rosa do aplicativo
            Text(
                text = finalWelcomeText,
                color = RosaMindUp,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Espaço abaixo reservado para linhas de texto, com a cor da fonte em rosa do app
            CompositionLocalProvider(LocalContentColor provides RosaMindUp) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (textLines.isNotEmpty()) {
                        textLines.forEach { line ->
                            Text(
                                text = line,
                                color = RosaMindUp,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                        }
                    } else {
                        // Espaço vazio para futuras linhas de texto
                        Spacer(modifier = Modifier.height(180.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun CustomBottomNavigation(
    onNavigateToDiario: () -> Unit = {}
) {
    val context = LocalContext.current
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = RosaMindUp,
                selectedTextColor = RosaMindUp,
                indicatorColor = RosaMindUp.copy(alpha = 0.2f)
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Place, contentDescription = "Metas") },
            label = { Text("Metas", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                val dbHelper = DBHelper(context)
                val jaPreencheu = runCatching { dbHelper.possuiEntradaDiario() }.getOrDefault(false)
                if (jaPreencheu) {
                    Toast.makeText(context, MENSAGEM_DIARIO_JA_PREENCHIDO, Toast.LENGTH_SHORT).show()
                } else {
                    onNavigateToDiario()
                }
            },
            icon = { Icon(Icons.Default.Star, contentDescription = "Diário") },
            label = { Text("Diário", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Relatórios") },
            label = { Text("Relatórios", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant)
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

@Preview(showBackground = true)
@Composable
fun HomeScreenWithTextLinesPreview() {
    MindUpTheme {
        HomeScreen(
            userName = "Guilherme",
            textLines = listOf(
                "Espaço reservado para anotações do dia.",
                "Suas metas e reflexões aparecem aqui."
            )
        )
    }
}

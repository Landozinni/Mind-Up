package com.mindup.mindup.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.mindup.mindup.DBHelper
import com.mindup.mindup.MENSAGEM_DIARIO_JA_PREENCHIDO
import com.mindup.mindup.navigation.Routes

@Composable
fun BottomBar(
    navController: NavController? = null,
    currentRoute: String = Routes.INICIO
) {
    val context = LocalContext.current

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {

        // INÍCIO
        NavigationBarItem(
            selected = currentRoute == Routes.INICIO,

            onClick = {
                navController?.navigate(Routes.INICIO) {
                    launchSingleTop = true
                }
            },

            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Início"
                )
            },

            label = {
                Text("Início")
            }
        )


        // METAS
        NavigationBarItem(
            selected = currentRoute == Routes.METAS,

            onClick = {
                navController?.navigate(Routes.METAS) {
                    launchSingleTop = true
                }
            },

            icon = {
                Icon(
                    Icons.Default.TrackChanges,
                    contentDescription = "Metas"
                )
            },

            label = {
                Text("Metas")
            }
        )


        // DIÁRIO
        NavigationBarItem(
            selected = currentRoute == Routes.MINDUP || currentRoute == Routes.DIARIO,

            onClick = {
                val dbHelper = DBHelper(context)
                val jaPreencheu = runCatching { dbHelper.possuiEntradaDiario() }.getOrDefault(false)
                if (jaPreencheu) {
                    Toast.makeText(context, MENSAGEM_DIARIO_JA_PREENCHIDO, Toast.LENGTH_SHORT).show()
                } else {
                    navController?.navigate(Routes.DIARIO) {
                        launchSingleTop = true
                    }
                }
            },

            icon = {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = "Diário"
                )
            },

            label = {
                Text("Diário")
            }
        )


        // QUESTIONÁRIO DASS-21
        NavigationBarItem(
            selected = currentRoute == Routes.DASS21,

            onClick = {
                navController?.navigate(Routes.DASS21) {
                    launchSingleTop = true
                }
            },

            icon = {
                Icon(
                    Icons.Default.Assignment,
                    contentDescription = "Questionário DASS-21"
                )
            },

            label = {
                Text("DASS-21")
            }
        )


        // PERFIL
        NavigationBarItem(
            selected = currentRoute == Routes.PERFIL,

            onClick = {
                navController?.navigate(Routes.PERFIL) {
                    launchSingleTop = true
                }
            },

            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Perfil"
                )
            },

            label = {
                Text("Perfil")
            }
        )
    }
}

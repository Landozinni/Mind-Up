package com.mindup.mindup.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
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
import androidx.navigation.NavController
import com.mindup.mindup.navigation.Routes

@Composable
fun BottomBar(
    navController: NavController,
    currentRoute: String
) {

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {

        // INÍCIO
        NavigationBarItem(
            selected = currentRoute == Routes.INICIO,

            onClick = {
                navController.navigate(Routes.INICIO) {
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
                navController.navigate(Routes.METAS) {
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


        // MINDUP
        NavigationBarItem(
            selected = currentRoute == Routes.MINDUP,

            onClick = {
                navController.navigate(Routes.MINDUP) {
                    launchSingleTop = true
                }
            },

            icon = {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = "MindUp"
                )
            },

            label = {
                Text("MindUp")
            }
        )


        // RELATÓRIOS
        NavigationBarItem(
            selected = currentRoute == Routes.RELATORIOS,

            onClick = {
                navController.navigate(Routes.RELATORIOS) {
                    launchSingleTop = true
                }
            },

            icon = {
                Icon(
                    Icons.Default.BarChart,
                    contentDescription = "Relatórios"
                )
            },

            label = {
                Text("Relatórios")
            }
        )


        // PERFIL
        NavigationBarItem(
            selected = currentRoute == Routes.PERFIL,

            onClick = {
                navController.navigate(Routes.PERFIL) {
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
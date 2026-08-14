package com.mindup.mindup.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindup.mindup.views.CadastroScreen
import com.mindup.mindup.views.Dass21Screen
import com.mindup.mindup.views.Login
import com.mindup.mindup.views.LoginScreen
import com.mindup.mindup.views.Diary
import com.mindup.mindup.views.DiaryScreen

object Routes {
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
    const val DIARIO = "diary"
    const val DASS21 = "dass21"
}

@Preview
@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") {
            Login(
                onEntrar = {
                    navController.navigate("loginScreen")
                },
                onCriarConta = {
                    navController.navigate("cadastro")
                }
            )
        }

        composable("loginScreen") {
            LoginScreen(
                onEntrar = {
                    navController.navigate(Routes.DIARIO)
                },
                onCriarConta = {
                    navController.navigate("cadastro")
                }
            )
        }

        composable(Routes.DIARIO) {
            DiaryScreen(
                onVoltar = {
                    navController.popBackStack()
                },
                onNavegarParaDass = {
                    navController.navigate(Routes.DASS21)
                }
            )
        }

        composable("cadastro") {
            CadastroScreen(
                onEntrarClick = {
                    navController.navigate("loginScreen")
                },
                onCriarContaClick = {
                    // salvar no Room depois
                }
            )
        }

        composable(Routes.DASS21) {
            Dass21Screen(
                onVoltar = {
                    navController.popBackStack()
                },
                onFinalizar = {
                    navController.popBackStack()
                }
            )
        }
    }
}
package com.mindup.mindup.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mindup.mindup.views.CadastroScreen
import com.mindup.mindup.views.Login
import com.mindup.mindup.views.LoginScreen

object Routes {
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
}

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
                    // Home depois
                },
                onCriarConta = {
                    navController.navigate("cadastro")
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
    }}
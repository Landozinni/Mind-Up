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
import com.mindup.mindup.views.DiaryScreen
import com.mindup.mindup.views.PasswordReset
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mindup.mindup.components.BottomBar
import com.mindup.mindup.views.GoalsScreen
import com.mindup.mindup.views.ProfileScreen
object Routes {

    const val LOGIN = "login"
    const val LOGIN_SCREEN = "loginScreen"
    const val CADASTRO = "cadastro"
    const val ESQUECEU_SENHA = "esqueceuSenha"

    const val INICIO = "inicio"
    const val METAS = "metas"
    const val MINDUP = "mindup"
    const val RELATORIOS = "relatorios"
    const val PERFIL = "perfil"

    const val DIARIO = "diary"
    const val DASS21 = "dass21"
}
@Preview
@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        // =========================
        // LOGIN
        // =========================

        composable(Routes.LOGIN) {

            Login(
                onEntrar = {
                    navController.navigate(Routes.LOGIN_SCREEN)
                },
                onCriarConta = {
                    navController.navigate(Routes.CADASTRO)
                }
            )
        }


        // =========================
        // LOGIN SCREEN
        // =========================

        composable(Routes.LOGIN_SCREEN) {

            LoginScreen(

                onEntrar = {

                    navController.navigate(Routes.INICIO) {

                        popUpTo(Routes.LOGIN_SCREEN) {
                            inclusive = true
                        }
                    }
                },

                onCriarConta = {
                    navController.navigate(Routes.CADASTRO)
                },

                onEsqueceuSenha = {
                    navController.navigate(Routes.ESQUECEU_SENHA)
                }
            )
        }


        // =========================
        // CADASTRO
        // =========================

        composable(Routes.CADASTRO) {

            CadastroScreen(

                onEntrarClick = {
                    navController.navigate(Routes.LOGIN_SCREEN)
                },

                onCriarContaClick = {
                    // salvar no Room depois
                }
            )
        }


        // =========================
        // ESQUECEU SENHA
        // =========================

        composable(Routes.ESQUECEU_SENHA) {

            PasswordReset(
                onVoltar = {
                    navController.popBackStack()
                }
            )
        }


        // =========================
        // INÍCIO
        // =========================

        composable(Routes.INICIO) {

            Scaffold(

                bottomBar = {
                    BottomBar(
                        navController = navController,
                        currentRoute = Routes.INICIO
                    )
                }

            ) { paddingValues ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "Tela Inicial"
                    )
                }
            }
        }


        // =========================
        // METAS
        // =========================

        composable(Routes.METAS) {

            Scaffold(

                bottomBar = {
                    BottomBar(
                        navController = navController,
                        currentRoute = Routes.METAS
                    )
                }

            ) { paddingValues ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                    GoalsScreen()
                }
            }
        }


        // =========================
        // PERFIL
        // =========================

        composable(Routes.PERFIL) {

            Scaffold(

                bottomBar = {
                    BottomBar(
                        navController = navController,
                        currentRoute = Routes.PERFIL
                    )
                }

            ) { paddingValues ->

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                    ProfileScreen(
                        onBackClick = {
                            navController.navigate(Routes.INICIO)
                        },

                        onEditProfile = {
                            // fazemos depois
                        },

                        onChangePassword = {
                            // fazemos depois
                        },

                        onLogout = {

                            navController.navigate(Routes.LOGIN) {

                                popUpTo(0)
                            }
                        }
                    )
                }
            }
        }


        // =========================
        // DIÁRIO
        // =========================

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


        // =========================
        // DASS-21
        // =========================

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
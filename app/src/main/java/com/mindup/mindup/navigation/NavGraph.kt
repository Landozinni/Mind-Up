package com.mindup.mindup.navigation

import android.provider.Settings
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.mindup.mindup.DBHelper
import com.mindup.mindup.MENSAGEM_DIARIO_JA_PREENCHIDO
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mindup.mindup.components.BottomBar
import com.mindup.mindup.views.HomeScreen
import com.mindup.mindup.views.GoalsScreen
import com.mindup.mindup.views.ProfileScreen
import com.mindup.mindup.views.PracticesScreen
object Routes {

    const val LOGIN = "login"
    const val LOGIN_SCREEN = "loginScreen"
    const val CADASTRO = "cadastro"
    const val ESQUECEU_SENHA = "esqueceuSenha"

    const val INICIO = "inicio"
    const val METAS = "metas"
    const val PRATICAS = "praticas"
    const val MINDUP = "mindup"
    const val RELATORIOS = "relatorios"
    const val PERFIL = "perfil"

    const val DIARIO = "diary"
    const val DASS21 = "dass21"
}
@Preview
@Composable
fun NavGraph(
    darkTheme: Boolean = false,
    onDarkThemeChange: (Boolean) -> Unit = {}
) {

    val context = LocalContext.current
    val animationsEnabled = remember {
        runCatching {
            Settings.Global.getFloat(context.contentResolver, Settings.Global.ANIMATOR_DURATION_SCALE, 1f) > 0f
        }.getOrDefault(true)
    }

    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN,
            enterTransition = {
                if (animationsEnabled) fadeIn(tween(220)) + slideInHorizontally(tween(220)) { it / 14 }
                else fadeIn(tween(0))
            },
            exitTransition = { if (animationsEnabled) fadeOut(tween(160)) else fadeOut(tween(0)) },
            popEnterTransition = {
                if (animationsEnabled) fadeIn(tween(220)) + slideInHorizontally(tween(220)) { -it / 14 }
                else fadeIn(tween(0))
            },
            popExitTransition = {
                if (animationsEnabled) fadeOut(tween(160)) + slideOutHorizontally(tween(160)) { it / 18 }
                else fadeOut(tween(0))
            }
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

            HomeScreen(
                bottomBar = {
                    BottomBar(
                        navController = navController,
                        currentRoute = Routes.INICIO
                    )
                },
                onNavigateToMetas = {
                    navController.navigate(Routes.METAS)
                },
                onNavigateToDiario = {
                    val dbHelper = DBHelper(context)
                    val jaPreencheu = runCatching { dbHelper.possuiEntradaDiario() }.getOrDefault(false)
                    if (jaPreencheu) {
                        Toast.makeText(context, MENSAGEM_DIARIO_JA_PREENCHIDO, Toast.LENGTH_SHORT).show()
                    } else {
                        navController.navigate(Routes.DIARIO)
                    }
                },
                onNavigateToPerfil = {
                    navController.navigate(Routes.PERFIL)
                }
            )
        }


        // =========================
        // METAS
        // =========================

        composable(Routes.METAS) {

            GoalsScreen(
                bottomBar = {
                    BottomBar(
                        navController = navController,
                        currentRoute = Routes.METAS
                    )
                }
            )
        }

        // =========================
        // PRÁTICAS GUIADAS
        // =========================
        composable(Routes.PRATICAS) {
            PracticesScreen(
                bottomBar = {
                    BottomBar(navController = navController, currentRoute = Routes.PRATICAS)
                },
                onOpenDass21 = { navController.navigate(Routes.DASS21) }
            )
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
                        darkThemeEnabled = darkTheme,
                        onDarkThemeChange = onDarkThemeChange,
                        onBackClick = {
                            navController.navigate(Routes.INICIO)
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
            val dbHelper = remember { DBHelper(context) }
            val jaPreencheuHoje = remember {
                runCatching { dbHelper.possuiEntradaDiario() }.getOrDefault(false)
            }

            if (jaPreencheuHoje) {
                LaunchedEffect(Unit) {
                    Toast.makeText(context, MENSAGEM_DIARIO_JA_PREENCHIDO, Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            } else {
                DiaryScreen(

                    onVoltar = {
                        navController.popBackStack()
                    },

                    onFinalizar = {
                        navController.navigate(Routes.INICIO) {
                            popUpTo(Routes.INICIO) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
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
}

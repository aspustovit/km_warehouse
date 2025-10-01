package com.km.warehouse

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.km.warehouse.ui.DarkTopAppBar
import com.km.warehouse.ui.NavigationStep
import com.km.warehouse.ui.move_order.MoveOrderView
import com.km.warehouse.ui.scan_to_file.ScanToFileView

/**
 * Create by Pustovit Oleksandr on 9/18/2025
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ContextCastToActivity")
@Composable
fun NavigationController(modifier: Modifier) {
    val navController = rememberNavController()
    val context = LocalContext.current as MainActivity


    NavHost(
        navController = navController,
        startDestination = NavigationStep.InitialScreen,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(300)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(300)) }
    ) {
        composable<NavigationStep.InitialScreen> {
            MenuScreen(
                activity = context,
                onApprovedDocumentClick = {
                    navController.navigate(route = NavigationStep.ApprovedDocumentScreen)
                },
                onIncomeDocumentClick = {
                    navController.navigate(route = NavigationStep.IncomeDocumentScreen)
                },
                onIssuedDocumentClick = {
                    navController.navigate(route = NavigationStep.IssuedDocumentScreen)
                },
                onScanToFileClick = {
                    navController.navigate(route = NavigationStep.ScanToFileScreen)
                })
        }
        composable<NavigationStep.ApprovedDocumentScreen> {
            Column(modifier = Modifier.fillMaxSize()) {
                DarkTopAppBar(
                    title = { Text(stringResource(id = R.string.approved_documents)) },
                    modifier = Modifier.fillMaxWidth()
                )
                MoveOrderView(modifier = Modifier.fillMaxWidth(), onWidgetClicked = {

                })
            }
        }
        composable<NavigationStep.ScanToFileScreen> {
            ScanToFileView(activity = context)
        }

        composable<NavigationStep.IssuedDocumentScreen> {
            Column(modifier = Modifier.fillMaxSize()) {
                DarkTopAppBar(
                    title = { Text(stringResource(id = R.string.issued_documents)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        composable<NavigationStep.IncomeDocumentScreen> {
            Column(modifier = Modifier.fillMaxSize()) {
                DarkTopAppBar(
                    title = { Text(stringResource(id = R.string.income_documents)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
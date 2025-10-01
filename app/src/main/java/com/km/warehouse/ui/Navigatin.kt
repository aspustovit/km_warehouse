package com.km.warehouse.ui
import kotlinx.serialization.Serializable
/**
 * Create by Pustovit Oleksandr on 9/18/2025
 */

@Serializable
sealed class NavigationStep {

    @Serializable
    data object InitialScreen : NavigationStep()

    @Serializable
    data object ApprovedDocumentScreen : NavigationStep()

    @Serializable
    data object IssuedDocumentScreen: NavigationStep()

    @Serializable
    data object IncomeDocumentScreen : NavigationStep()

    @Serializable
    data object ScanToFileScreen : NavigationStep()
}
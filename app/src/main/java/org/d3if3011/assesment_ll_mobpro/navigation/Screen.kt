package org.d3if3011.assesment_ll_mobpro.navigation

import org.d3if3011.assesment_ll_mobpro.ui.screen.KEY_ID_BUKU


sealed class Screen (val route: String){
    data object Home: Screen("MainScreen")
    data object FormBaru: Screen("DetailScreen")
    data object ScreenMain: Screen("ScreenMain")
    data object FormUbah: Screen ("DetailScreen/{$KEY_ID_BUKU}"){
        fun withId(id: Long) = "DetailScreen/$id"
    }
}
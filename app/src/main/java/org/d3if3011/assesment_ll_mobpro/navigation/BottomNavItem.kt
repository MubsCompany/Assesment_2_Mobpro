package org.d3if3011.assesment_ll_mobpro.navigation

import org.d3if3011.assesment_ll_mobpro.R

data class BottomNavItem(
    val label: String,
    val icon: Int,
    val route: String
) {
    companion object {
        fun getBottomNavItems(): List<BottomNavItem> {
            return listOf(
                BottomNavItem(
                    label = "Pencatatan",
                    icon = R.drawable.ic_pencatatan,
                    route = "home"
                ),
                BottomNavItem(
                    label = "Pelaporan",
                    icon = R.drawable.ic_pelaporan,
                    route = "bookmark"
                )
            )
        }
    }
}

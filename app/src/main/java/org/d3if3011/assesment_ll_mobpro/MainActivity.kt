package org.d3if3011.assesment_ll_mobpro

import android.content.res.Resources.Theme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.d3if3011.assesment_ll_mobpro.navigation.BottomNavItem
import org.d3if3011.assesment_ll_mobpro.navigation.SetupNavGraph
import org.d3if3011.assesment_ll_mobpro.ui.screen.DetailScreen
import org.d3if3011.assesment_ll_mobpro.ui.screen.KEY_ID_BUKU
import org.d3if3011.assesment_ll_mobpro.ui.screen.MainScreen
import org.d3if3011.assesment_ll_mobpro.ui.screen.ScreenMain
import org.d3if3011.assesment_ll_mobpro.ui.theme.Assesment_ll_MobproTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Assesment_ll_MobproTheme {
                MainScreenn()
            }
        }
    }
}

@Composable
fun MainScreenn() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        NavHost(navController, startDestination = "home", Modifier.padding(it)) {
            composable("home") { MainScreen(navController) }
            composable("bookmark") { ScreenMain() }
            composable("detailScreen") { DetailScreen(navController = navController)}
            composable(
                route = "detailScreen/{${KEY_ID_BUKU}}",
                arguments = listOf(navArgument(KEY_ID_BUKU) { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong(KEY_ID_BUKU)
                DetailScreen(navController = navController, id = id ?: 0)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = BottomNavItem.getBottomNavItems()
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .background(Color.White)
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            CustomBottomNavigationItem(
                item = item,
                isSelected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun CustomBottomNavigationItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.label,
            modifier = Modifier.size(24.dp),
//            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
        )
        Text(
            text = item.label,
            color = if (isSelected) Color.Black else Color.Gray,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Assesment_ll_MobproTheme {
        MainScreenn()
    }
}

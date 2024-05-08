import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.marketplace.Addmerchant
import com.example.marketplace.ContactScreen
import com.example.marketplace.FavProductViewModel
import com.example.marketplace.FavScreen
import com.example.marketplace.HomeScreen
import com.example.marketplace.MessageViewModel
import com.example.marketplace.NavBarItem
import com.example.marketplace.ProductViewModel
import com.example.marketplace.Routes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomAppBar(
    navigationController: NavController,
    favProductViewModel: FavProductViewModel,
    productViewModel: ProductViewModel,
    MessageViewModel: MessageViewModel
) {
    val navController = rememberNavController()
    val email: String? = navController.previousBackStackEntry?.savedStateHandle?.get("email")

    Scaffold(
        bottomBar = {
            BottomNavigation(
                backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
            ) {
                val navBackStackEntry by
                navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                NavBarItem().NavBarItems().forEach { navItem ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                navItem.icon, contentDescription
                                = null
                            )
                        },
                        label = { Text(navItem.label) },
                        selected = currentDestination?.hierarchy?.any
                        { it.route == navItem.route } == true,
                        onClick = {
                            navController.navigate(navItem.route)
                            {
                                // popUpTo is used to pop up to a given  destination before navigating
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // be at most one copy of a given destination on the top of the back stack
                                launchSingleTop = true
                                // this navigation action should restore any state previously saved
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(
            navController,
            startDestination = Routes.Home.value,
//                 modifier =  Modifier.padding(paddingValues)
        ) {
            composable(Routes.Home.value) {
                navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                HomeScreen(navController, favProductViewModel)
            }
//            TODO: Change to add product
            composable(Routes.Chat.value) {
                navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                ContactScreen(MessageViewModel,navController)
            }
            composable(Routes.AddProduct.value) {
<<<<<<< Updated upstream
                Addmerchant().AppNavigation(productViewModel)
=======
                navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                Addmerchant().AddProduct(productViewModel, navController)
>>>>>>> Stashed changes
            }
            composable(Routes.Favourites.value) {
                navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                FavScreen(navController, favProductViewModel)
            }
        }


    }
}
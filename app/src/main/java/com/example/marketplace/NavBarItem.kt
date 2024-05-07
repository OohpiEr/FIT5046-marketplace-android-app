package com.example.marketplace

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

data class NavBarItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {
    fun NavBarItems(): List<NavBarItem> {
        return listOf(
            NavBarItem(
                label = "Home",
                icon = Icons.Filled.Home,
                route = Routes.Home.value
            ),
            NavBarItem(
                label = "Chat",
                icon = Icons.Filled.MailOutline,
                route = Routes.Chat.value
            ),
            NavBarItem(
                label = "Add",
                icon = Icons.Filled.Add,
                route = Routes.AddProduct.value
            ),
            NavBarItem(
                label = "Favourites",
                icon = Icons.Filled.Favorite,
                route = Routes.Favourites.value
            ),
//            NavBarItem(
//                label = "Account",
//                icon = Icons.Filled.AccountCircle,
//                route = Routes.Account.value
//            ),
        )
    }
}
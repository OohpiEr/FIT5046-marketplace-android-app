package com.example.marketplace

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

//class Favourites : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            FavScreen()
//        }
//
//        WindowCompat.setDecorFitsSystemWindows(window, false)
//    }
//
//}


//@Composable
//@Preview
//fun PreviewFavouritesScreen() {
//    val title = "Favourites"
//
//    MarketplaceTheme {
//        FavScreen()
//    }
//}

/*
* https://developer.android.com/jetpack/compose/components/app-bars
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavScreen(navController: NavHostController, favProductViewModel: FavProductViewModel) {
    val title = "Favourites"
    val email: String? = navController.previousBackStackEntry?.savedStateHandle?.get("email")
    val username: String? = navController.previousBackStackEntry?.savedStateHandle?.get("username")

//    val favProducts by favProductViewModel.allFavProducts.observeAsState(emptyList())


    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Localized description",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        IconButton(onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                            navController.currentBackStackEntry?.savedStateHandle?.set("username",username)
                            navController.navigate("home")}) {
                            Icon(Icons.Filled.Home, contentDescription = "Localized description")
                        }
                        IconButton(onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                            navController.currentBackStackEntry?.savedStateHandle?.set("username",username)
                            navController.navigate("contact")
                        },) {
                            Icon(
                                Icons.Filled.MailOutline,
                                contentDescription = "Localized description",
                            )
                        }
                        IconButton(onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                            navController.currentBackStackEntry?.savedStateHandle?.set("username",username)
                            navController.navigate("Addmerchant") }) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "Localized description",
                            )
                        }
                        IconButton(onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                            navController.currentBackStackEntry?.savedStateHandle?.set("username",username)
                            navController.navigate("Favourites") }) {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Localized description",
                            )
                        }

                    }}
            )
        },
    ) { innerPadding ->
        if (email != null && username != null) {
                FavScrollContent(innerPadding, favProductViewModel,email,username,navController)
        }
    }

}

@Composable
fun FavScrollContent(innerPadding: PaddingValues, favProductViewModel: FavProductViewModel,email:String,username:String,navController: NavController) {
    // TODO: remove this
    val favProducts by favProductViewModel.allFavProducts.observeAsState(emptyList())
    val selectedProduct = remember { mutableStateOf<Product?>(null) }
    val insertDialog = remember { mutableStateOf(false) }
    val context= LocalContext.current

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            items(favProducts) { product ->
                ProductCard(product, insertDialog, selectedProduct,email,username,navController)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    )

    if (insertDialog.value) {
        selectedProduct?.let { it.value?.let { it1 -> favProductViewModel.deleteFavProduct(it1) } }
        Toast.makeText(context,"Remove from Favourites Successfully!", Toast.LENGTH_SHORT).show()
    }
}

//@Composable
//fun FavItemCard() {
//    ElevatedCard(
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant,
//        ),
//    ) {
//        Column {
//            Row() {
//                Image(
//                    painter = painterResource(id = R.drawable.milk),
//                    contentDescription = null
//                )
//            }
//            Row(
//                modifier = Modifier
//                    .height(56.dp)
//                    .fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            )
//            {
//                Text(
//                    text = "Milk",
//                    modifier = Modifier
//                        .padding(16.dp),
//                    textAlign = TextAlign.Center,
//                )
//                Spacer(Modifier.weight(1f))
//                IconButton(onClick = { /* do something */ }) {
//                    Icon(
//                        imageVector = Icons.Filled.FavoriteBorder,
//                        contentDescription = "Localized description"
//                    )
//                }
//            }
//            Row()
//            {
//                Text(
//                    text = "$50",
//                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
//                    textAlign = TextAlign.Center,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 20.sp
//                )
//            }
//        }
//
//    }
//}
package com.example.marketplace

//import androidx.compose.material3.MaterialTheme
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext

/*
class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = MessageViewModel()
        setContent {
            val navController = rememberNavController()
           NavHost(navController = navController, startDestination = "home") {
                composable("chat") { ContactScreen(viewModel, navController) }
                composable("home"){ HomeScreen(navController)}
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

}
*/
/*
@Composable
@Preview
fun PreviewHomescreen() {
    MarketplaceTheme {
        HomeScreen(navController)
    }
}
*/
/*
* https://developer.android.com/jetpack/compose/components/app-bars
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, favProductViewModel: FavProductViewModel) {
    val title = "Home"
    val email: String? = navController.previousBackStackEntry?.savedStateHandle?.get("email")
    val username: String? = navController.previousBackStackEntry?.savedStateHandle?.get("username")
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
                        imageVector = Icons.Rounded.ShoppingCart,
                        contentDescription = "Localized description",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                },
//                scrollBehavior = scrollBehavior
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
        if (email != null && username !=null) {
            HomeScrollContent(innerPadding, favProductViewModel,email,username,navController)
        }
    }


//    if (insertDialog.value) {
//        selectedProduct?.let { favProductViewModel.insertFavProduct(it) }
//    }
//        InsertSubjectDialog(
//            onDismiss = { insertDialog.value = false },
//            onSave = { subjectName ->
//               favProductViewModel.insertProduct(Product(name = subjectName))
//            }
//        )
}

//@Composable
//fun InsertSubjectDialog(
//    onDismiss: () -> Unit,
//    onSave: (String) -> Unit
//) {
//    var productName by remember { mutableStateOf("") }
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Favourite Product") },
//        confirmButton = {
//            Button(
//                onClick = {
//                    onSave(productName)
//                    onDismiss()
//                }
//            ) {
//                Text("Save")
//            }
//        },
//        dismissButton = {
//            Button(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        },
////        text = {
////            TextField(
////                value = productName,
////                onValueChange = { productName = it },
////                modifier = Modifier.fillMaxWidth()
////            )
////        }
//    )
//}
@SuppressLint("SuspiciousIndentation")
@Composable
fun HomeScrollContent(
    innerPadding: PaddingValues, favProductViewModel: FavProductViewModel,
    email: String,username:String,navController:NavController
) {
    val db = Firebase.firestore
    val context= LocalContext.current
    val allProducts = remember { mutableStateListOf<Product>() }
    db.collection("products")
        .get()
        .addOnSuccessListener { result ->
            allProducts.clear()
            for (document in result) {
                val p = document.toObject(Product::class.java)
                allProducts.add(p)
            }
        }
        .addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }

    val selectedProduct = remember { mutableStateOf<Product?>(null) }
    val insertDialog = remember { mutableStateOf(false) }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            items(allProducts) { product ->
                ProductCard(product, insertDialog, selectedProduct, email,username,navController)
            }
        },
        modifier = Modifier
            .fillMaxSize()
    )
    if (insertDialog.value) {
        selectedProduct?.let { it.value?.let { it1 -> favProductViewModel.insertFavProduct(it1) }
        insertDialog.value = false
            Toast.makeText(context,"Add to Favourites Successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}


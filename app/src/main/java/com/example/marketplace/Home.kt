package com.example.marketplace

//import androidx.compose.material3.MaterialTheme
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf

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

//    val favProducts by FavProductViewModel.allProducts.observeAsState(emptyList())


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

        ) { innerPadding ->
        HomeScrollContent(innerPadding, favProductViewModel)
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
@Composable
fun HomeScrollContent(innerPadding: PaddingValues, favProductViewModel: FavProductViewModel) {
    val db = Firebase.firestore

    val allProducts = remember { mutableStateListOf<Product>() }

    db.collection("products")
        .get()
        .addOnSuccessListener { result ->
            for (document in result) {
                Log.d(TAG, "${document.id} => ${document.data}")
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
                ProductCard(product, insertDialog, selectedProduct)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    )
    if (insertDialog.value) {
        selectedProduct?.let { it.value?.let { it1 -> favProductViewModel.insertFavProduct(it1) }
        insertDialog.value = false
        }
    }
}


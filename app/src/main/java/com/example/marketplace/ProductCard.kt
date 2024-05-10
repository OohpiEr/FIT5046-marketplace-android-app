package com.example.marketplace

import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ProductCard(
    product: Product,
    insertDialog: MutableState<Boolean>,
    selectedProduct: MutableState<Product?>,
    email: String,
    username:String,
    navController: NavController
) {
    var decodedImage: ImageBitmap
    try {
        val imageBytes = Base64.decode(product.photo, Base64.DEFAULT)
        decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
    } catch (e: Exception) {
        decodedImage = ImageBitmap.imageResource(R.drawable.milk)
    }

    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
    ) {
        Column {
            Box (
                modifier = Modifier.height(200.dp)
            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.milk),
////                    painter = painterResource(id = R.drawable.appicon),
//                    contentDescription = product.description
//                )
                Image(
                    bitmap = decodedImage,
                    contentDescription = product.description,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.name,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {
                    insertDialog.value = true
                    selectedProduct.value = product
                }) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = "Localized description"
                    )
                }
            }
            Row {
                Text(
                    text = "$" + product.price,
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.weight(1f))
                if(product.ownerEmail !=email){
                    IconButton(onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("email", product.ownerEmail)
                        navController.currentBackStackEntry?.savedStateHandle?.set("receiver",product.ownerName)
                        navController.currentBackStackEntry?.savedStateHandle?.set("sender", username)
                        navController.currentBackStackEntry?.savedStateHandle?.set("Id", email)
                        navController.navigate("chat")
                    }) {
                        Icon(
                            imageVector = Icons.Filled.MailOutline,
                            contentDescription = "Chat With Owner",
                        )
                    }
                }

            }
        }

    }
}
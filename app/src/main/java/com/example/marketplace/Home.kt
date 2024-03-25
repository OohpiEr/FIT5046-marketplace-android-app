package com.example.marketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
//import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.example.marketplace.ui.theme.MarketplaceTheme

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Homescreen()
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
    }

}

//@Composable
//fun Homescreen() {
//    Card() {
//        Text(text = "Hello, world!")
//    }
//}

@Composable
@Preview
fun PreviewHomescreen() {
    MarketplaceTheme {
        Homescreen()
    }
}

@Composable
fun Homescreen() {
//    Card(
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant,
//        ),
//        modifier = Modifier.size(width = 240.dp, height = 100.dp)
//    ) {
//        Text(
//            text = "Filled",
//            modifier = Modifier.padding(16.dp),
//            textAlign = TextAlign.Center,
//        )
//    }
    TopAppBar()
//    Surface(
//        color = Color(0xFF6A8DCC), // Set the background color for the column
//        modifier = Modifier.fillMaxSize()
//    ) {
//        Column(modifier = Modifier.fillMaxSize()) {
//            Box(
//                modifier = Modifier
////                    .weight(0.3f)
//                    .fillMaxHeight()
//                    .padding(30.dp),
//                contentAlignment = Alignment.CenterStart,
//            ) {
//                Text(
//                    text = "Hey! New User",
//                    style = MaterialTheme.typography.headlineLarge, // Use a larger text style
//                    color = Color.White // Set text color to white
//                )
//            }
//
//
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
        /*
        * https://developer.android.com/jetpack/compose/components/app-bars
        */
fun TopAppBar() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            "Marketplace",
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

                actions = {
                    // hamburger icon
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        ScrollContent(innerPadding)
    }

}

@Composable
fun ScrollContent(innerPadding: PaddingValues) {
    val tempItems = 10

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = {
            items(tempItems) {
                ItemCard()
            }

        },
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    )
}

@Composable
fun ItemCard() {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,

        ),

        ) {
        Column {
            Row() {
                Image(
                    painter = painterResource(id = R.drawable.milk),
//                    painter = painterResource(id = R.drawable.appicon),
                    contentDescription = null
                )

            }
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Text(
                    text = "Milk",
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = "Localized description"
                    )
                }
            }
            Row()
            {
                Text(
                    text = "$50",
                    modifier = Modifier.padding(start = 16.dp, bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }

    }
}
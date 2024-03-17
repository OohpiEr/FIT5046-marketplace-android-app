package com.example.marketplace

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.marketplace.ui.theme.MarketplaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            signIn()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun signIn() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(17.dp), // padding for the Column
        verticalArrangement = Arrangement.Center, // center vertically
        horizontalAlignment = Alignment.CenterHorizontally // center horizontally
    ){
        Image(
            painter = painterResource(id = R.drawable.marketplace),
            contentDescription = "App Icon",
            modifier = Modifier
                .size(254.dp)
        )}





    Surface(
        color = Color.White,
        modifier = Modifier.padding(top = 345.dp),
        shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),


    ) {
        Box(modifier = Modifier.padding(16.dp)) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), // padding for the Column
        verticalArrangement = Arrangement.Center, // center vertically
        horizontalAlignment = Alignment.CenterHorizontally // center horizontally
    )  {
        Text(
            text = "Log In",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 13.dp, start = 8.dp, end = 8.dp)
        )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Button(
            onClick = {},
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 13.dp,
                    start = 8.dp,
                    end = 8.dp
                )
                .height(50.dp)
        ) {
            Text(text = "Log In")
        }

        Text(
            text = "Or",
            modifier = Modifier
                .padding(8.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onTertiary,),
            border = BorderStroke(0.001.dp, Color.Gray),
            modifier = Modifier.size(width = 120.dp, height = 38.dp),
            shape = RoundedCornerShape(10.dp)
             ) {
        Row(modifier = Modifier
            .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.googleicon),
                contentDescription = "Google Sign In"
            )
            Text(text = "Log In",
                modifier = Modifier
                    .padding(start = 9.dp)
                )
        }
        }




        Text(
            text = "Don't have an account? Sign Up",
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            )
    }
}
    }
}


package com.example.marketplace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat


class SignUp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           signUp()
        }

        WindowCompat.setDecorFitsSystemWindows(window,false)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun gender(
) {
    val genders = listOf( "Prefer not to say", "Male", "Female", "Non-binary", "Genderqueer", "Genderfluid", "Transgender")
    var isExpanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf(genders[0]) }
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
    ) {
        OutlinedTextField(
            modifier = Modifier .menuAnchor()
                .fillMaxWidth() .focusProperties{
                    canFocus = false}
                .padding(8.dp), readOnly = true,
            value = selectedGender,
            onValueChange = {},
            label = { Text("Gender") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false } )
        {
            genders.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) }, onClick = {
                        selectedGender = selectionOption
                        isExpanded = false },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding, )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun signUp() {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }


    Surface(
        color = Color(0xFF6A8DCC), // Set the background color for the column
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Box occupying the left half of the screen
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .padding(30.dp),
                   // .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.CenterStart,

                ) {

                Text(
                    text = "Hey! New User",
                    style = MaterialTheme.typography.headlineLarge, // Use a larger text style
                    color = Color.White // Set text color to white
                )
            }
            Surface(
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(topStart = 25.dp, topEnd = 150.dp)
            ) {
                Box(modifier = Modifier.padding(10.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp), // padding for the Column
                        verticalArrangement = Arrangement.Center, // center vertically
                        horizontalAlignment = Alignment.CenterHorizontally // center horizontally
                    ) {
                        Text(
                            text = "Sign Up",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 13.dp, start = 8.dp, end = 8.dp)
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
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

                        gender(

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
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A8DCC)),
                        ) {
                            Text(text = "Sign Up")
                        }

                        Text(
                            text = "Back to Sign In",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        Text(
                            text = "Or",
                            modifier = Modifier
                                .padding(top = 10.dp,
                                    start = 8.dp,
                                    end = 8.dp, bottom = 10.dp)
                        )

                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onTertiary,),
                            border = BorderStroke(0.001.dp, Color.Gray),
                            modifier = Modifier.size(width = 120.dp, height = 38.dp)

                            ,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Image(
                                    modifier = Modifier.size(20.dp),
                                    painter = painterResource(id = R.drawable.googleicon),
                                    contentDescription = "Google Sign In"
                                )
                                Text(
                                    text = "Sign Up",
                                    modifier = Modifier
                                        .padding(start = 9.dp)
                                )
                            }
                        }





                    }
                }
            }
        }
    }

}
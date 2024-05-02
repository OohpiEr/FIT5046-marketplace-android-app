package com.example.marketplace
import android.annotation.SuppressLint
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.FirebaseApp

class SignUp : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("UserInfo")
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "signup") {
                composable("signup") { SignUp(navController = navController, databaseReference = databaseReference) }
                composable("login") { SignIn(navController, databaseReference = databaseReference) }
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window,false)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Gender(selectedGender: String, onGenderSelected: (String) -> Unit) {
    val genders = listOf(
        "Prefer not to say", "Male", "Female", "Non-binary", "Gender-fluid", "Transgender"
    )

    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(8.dp),
            readOnly = true,
            value = selectedGender,
            onValueChange = {},
            label = { Text("Gender") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            genders.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption, modifier = Modifier.fillMaxWidth()) },
                    onClick = {
                        onGenderSelected(selectionOption) // Update selected gender
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}


@Composable
fun SignUp(navController: NavController, databaseReference: DatabaseReference) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") } // State to hold selected gender

    val context= LocalContext.current

    Surface(
        color = Color(0xFF6A8DCC),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .padding(25.dp)
            )

            Surface(
                color = Color.White,
                modifier = Modifier
                    .weight(1.4f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
            ) {
                Box(modifier = Modifier.padding(10.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
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
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )

                        // Call gender composable to select gender
                        Gender(selectedGender = selectedGender) { gender ->
                            selectedGender = gender // Update selectedGender
                        }

                        Button(
                            onClick = {
                                val userObj = UserObj(email, username, password, selectedGender)

                                // Directly set the value of userObj to the database reference
                                databaseReference.setValue(userObj)
                                    .addOnSuccessListener {
                                        // Write operation successful, show success toast
                                        Toast.makeText(context, "Added to database!", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { error ->
                                        // Write operation failed, show failure toast with error message
                                        Toast.makeText(context, "Failed to add to database: ${error.message}", Toast.LENGTH_SHORT).show()
                                    }
                            },
                            shape = RoundedCornerShape(5.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 13.dp, start = 8.dp, end = 8.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A8DCC)),
                        ) {
                            Text(text = "Sign Up")
                        }

                        Button(
                            onClick = { navController.navigate("login") },
                            modifier = Modifier.fillMaxWidth().padding(top = 7.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFFFFF)),
                        ) {
                            Text(
                                buildAnnotatedString {
                                    append("Back to ")
                                    withStyle(style = SpanStyle(color = Color(0xFF6A8DCC))) {
                                        append("Sign In")
                                    }
                                },
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
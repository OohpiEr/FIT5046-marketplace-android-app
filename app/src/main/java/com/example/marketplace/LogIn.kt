package com.example.marketplace

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.marketplace.ui.theme.MarketplaceTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class MainActivity : ComponentActivity() {
    private val viewModel = MessageViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarketplaceTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "contactScreen") {
                    composable("contactScreen") {
                        ContactScreen(viewModel = viewModel, navController)
                    }
                    composable("chatScreen") {
                        ChatScreen()
                    }
                }
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}

@Preview(showBackground = true)
@Composable
fun SignIn() {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        color = Color(0xFF6A8DCC), // Set the background color for the column
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Box occupying the left half of the screen
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .padding(30.dp),
                // .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.CenterStart,

                ) {

                Text(
                    text = "Good Day ☘",
                    style = MaterialTheme.typography.headlineLarge, // Use a larger text style
                    color = Color.White // Set text color to white
                )
            }




            Surface(
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 200.dp)
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
                            text = "Log In",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 13.dp, start = 8.dp, end = 8.dp)
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
                            Text(text = "Log In")
                        }

                        Text(
                            text = "Don't have an account? Sign Up",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)

                        )
                        Text(
                            text = "Or",
                            modifier = Modifier
                                .padding(
                                    top = 30.dp,
                                    start = 8.dp,
                                    end = 8.dp, bottom = 10.dp
                                )
                        )

                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onTertiary),
                            border = BorderStroke(0.001.dp, Color.Gray),
                            modifier = Modifier.size(width = 120.dp, height = 38.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ) {

                                GoogleLoginButton()

                            }
                        }


                    }
                }
            }
        }
    }

}

@Composable
fun GoogleLoginButton() {
    val context = LocalContext.current
    val signInLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { firebaseAuthWithGoogle(context, it) }
            } catch (e: ApiException) {
                // Handle login failure
            }
        }
    }

    val googleSignInClient = rememberGoogleSignInClient(context)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            val signInIntent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent)
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.googleicon),
            contentDescription = "Google Sign In",
            modifier = Modifier
                .padding(start = 8.dp)
                .size(20.dp)
        )

        Text("Sign in with Google")
    }
}

@Composable
private fun rememberGoogleSignInClient(context: Context): GoogleSignInClient {
    val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    return GoogleSignIn.getClient(context, options)
}

private fun firebaseAuthWithGoogle(context: Context, idToken: String) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                val email = user?.email
                Toast.makeText(context, email, Toast.LENGTH_SHORT).show()
            } else {
                // Handle login failure
            }
        }
}
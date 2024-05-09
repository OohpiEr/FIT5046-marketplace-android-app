package com.example.marketplace
import android.app.Activity
import android.content.Context
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.example.marketplace.ui.theme.marketplace_light_onPrimary
import com.example.marketplace.ui.theme.marketplace_light_primary
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LogIn : ComponentActivity() {
    private val favProductViewModel: FavProductViewModel by viewModels()
    private val productViewModel: ProductViewModel by viewModels()
    private val messageViewModel: MessageViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference("UserInfo")

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { SignIn(navController, databaseReference = databaseReference) }
                composable("signup") { SignUp(navController, databaseReference = databaseReference) }
                composable("contact") { ContactScreen(messageViewModel,navController) }
                composable("chat") { ChatScreen(navController) }
                composable("Addmerchant") { Addmerchant().AddProduct(productViewModel,navController) }
                composable("Map") { Map().MapScreen(navController) }
                composable("Favourites") { FavScreen(navController, favProductViewModel) }
                composable("home"){ HomeScreen(navController, favProductViewModel) }
            }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}


@Composable
fun SignIn(navController: NavController, databaseReference: DatabaseReference) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailDB by remember { mutableStateOf("") }
    var passwordDB by remember { mutableStateOf("") }
    var genderDB by remember { mutableStateOf("") }
    var usernameDB by remember{ mutableStateOf("") }
    val context= LocalContext.current
    val emailKey = email.replace(".", ",")
    val sharedPreferences = LocalContext.current.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
    val savedEmail = remember { mutableStateOf(sharedPreferences.getString("email", "")) }

    Surface(
        color = marketplace_light_primary, // Set the background color for the column
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
                    .padding(30.dp),
                contentAlignment = Alignment.CenterStart,
                ) {
                Text(
                    text = "☘",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold,// Use a larger text style
                    color = Color.White // Set text color to white
                ) }
            Surface(
                color = Color.White,
                modifier = Modifier.weight(1f).fillMaxHeight(),
                shape = RoundedCornerShape(topStart = 10.dp, topEnd = 150.dp)
            ) {
                Box(modifier = Modifier.padding(10.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp), // padding for the Column
                        verticalArrangement = Arrangement.Center, // center vertically
                        horizontalAlignment = Alignment.CenterHorizontally // center horizontally
                    ) {
                        Text(
                            text = "Log In",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 13.dp, start = 8.dp, end = 8.dp)
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            placeholder = { savedEmail.value?.let { Text(it) } }
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth().padding(8.dp)
                        )

                        Button(
                            onClick = {
                                // Example usage of validateInput in login logic
                                if (validateInput(email, password)) {
                                    sharedPreferences.edit().putString("email", email).apply()
                                    databaseReference.child(emailKey).get().addOnSuccessListener { snapshot ->
                                        val userObj = snapshot.getValue(UserObj::class.java)
                                        userObj?.let {
                                            emailDB = it.userEmail
                                            passwordDB = it.userPassword
                                            genderDB = it.userGender
                                            usernameDB = it.userUsername

                                            if(emailDB == email && passwordDB == password) {
                                                Toast.makeText(context, "Log In Succeed!", Toast.LENGTH_SHORT).show()
                                                navController.currentBackStackEntry?.savedStateHandle?.set("email", emailDB)
                                                navController.currentBackStackEntry?.savedStateHandle?.set("username", usernameDB)
                                                navController.navigate("home")

                                            } else{
                                                Toast.makeText(context,"Incorrect email or password!",Toast.LENGTH_SHORT).show()
                                            }
                                        } ?: run {
                                            // UserObj is null
                                            Toast.makeText(context, "User data not found!", Toast.LENGTH_SHORT).show()
                                        }
                                    }.addOnFailureListener { error ->
                                        // Handle failure to retrieve data from Firebase
                                        Toast.makeText(context, "Failed to retrieve user data: ${error.message}", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    // Invalid input, show error message to the user
                                    Toast.makeText(context, "Invalid email or password format!", Toast.LENGTH_SHORT).show()
                                }
                            },

                            shape = RectangleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 13.dp,
                                    start = 8.dp,
                                    end = 8.dp
                                )
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor =  marketplace_light_primary),
                        ) {
                            Text(text = "Log In")
                        }
                        Button(
                            onClick = { navController.navigate("signup") },
                            modifier = Modifier.fillMaxWidth().padding(top = 13.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = marketplace_light_onPrimary),
                        ) {
                            Text(
                                buildAnnotatedString {
                                    append("Don't have an account? ")
                                    withStyle(style = SpanStyle(color = marketplace_light_primary)) {
                                        append("Sign Up")
                                    }
                                },
                                color = Color.Black
                            )
                        }
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
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                GoogleLoginButton(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun validateInput(email: String, password: String): Boolean {
    if(email.isEmpty() ||  password.isEmpty()){
        return false
    }
    // Check if the email is in a valid format
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        return false  // Invalid email format
    }
    // Check if the password meets minimum length requirement
    if (password.length !in 5..12) {
        return false  // Password is too short
    }

    return true  // Input is valid
}

@Composable
fun GoogleLoginButton(navController: NavController) {
    val context = LocalContext.current

    // Remember the launcher for handling activity result
    val signInLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { idToken ->
                    // Call Firebase authentication with Google method
                    firebaseAuthWithGoogle(context, idToken) { userEmail, username ->
                        if (userEmail != null && username != null) {
                            Toast.makeText(context, "Log In Succeed!", Toast.LENGTH_SHORT).show()
                            // Navigate to home screen and pass user data
                            navController.navigate("home") {
                                navController.currentBackStackEntry?.arguments?.apply {
                                    putString("email", userEmail)
                                    putString("username", username)
                                }
                            }
                        } else {
                            Toast.makeText(context, "Failed to retrieve user info after Google sign-in", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } catch (e: ApiException) {
                // Handle Google sign-in failure
                Toast.makeText(context, "Google sign-in failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Create Google sign-in client
    val googleSignInClient = rememberGoogleSignUpClient(context)

    // Row with Google sign-in button
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            val signInIntent = googleSignInClient.signInIntent
            signInLauncher.launch(signInIntent) // Launch the sign-in intent
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.googleicon),
            contentDescription = "Google Sign In",
            modifier = Modifier
                .padding(start = 8.dp)
                .size(20.dp)
        )
        Text(
            text = "Log In",
            modifier = Modifier
                .padding(start = 9.dp)
        )
    }
}

// Function to retrieve Google sign-in client
@Composable
private fun rememberGoogleSignUpClient(context: Context): GoogleSignInClient {
    val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(context.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()
    return GoogleSignIn.getClient(context, options)
}

private fun firebaseAuthWithGoogle(context: Context, idToken: String, callback: (String?, String?) -> Unit) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = FirebaseAuth.getInstance().currentUser
                val email = user?.email
                val username = user?.displayName

                if (email != null && username != null) {
                    // Check if the user exists in Firebase
                    val emailKey = email.replace(".", ",")
                    val databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo")

                    databaseReference.child(emailKey).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userObj = snapshot.getValue(UserObj::class.java)

                            if (userObj != null && userObj.userUsername == username) {
                                callback(email, username)
                            } else {
                                Toast.makeText(context, "Username mismatch or user not found in Firebase!", Toast.LENGTH_SHORT).show()
                                callback(null, null)
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            // Handle database read error
                            callback(null, null) }
                    })
                } else {
                    // Handle missing email or username
                    callback(null, null)
                    Toast.makeText(context, "Email or username not found after Google sign-in!", Toast.LENGTH_SHORT).show()
                }
            } else {
                callback(null, null)
            }
        }
    }



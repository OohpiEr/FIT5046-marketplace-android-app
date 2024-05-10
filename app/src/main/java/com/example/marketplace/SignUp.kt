package com.example.marketplace
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.marketplace.ui.theme.marketplace_light_onPrimary
import com.example.marketplace.ui.theme.marketplace_light_primary
import java.security.MessageDigest
import java.math.BigInteger

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
    var selectedGender by remember { mutableStateOf("") }


    val context= LocalContext.current


    Surface(
        color = marketplace_light_primary,
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
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        // Call gender composable to select gender
                        Gender(selectedGender = selectedGender) { gender ->
                            selectedGender = gender // Update selectedGender
                        }

                        Button(
                            onClick = {
                                if(validateInput(context,email, username, password, selectedGender)) {
                                    val userObj = UserObj(email, username, hashPassword(password), selectedGender)
                                    if(validateEmailFormat(email)){
                                        val emailKey = email.replace(".", ",")
                                        // Directly set the value of userObj to the database reference
                                        databaseReference.child(emailKey).setValue(userObj)
                                            .addOnSuccessListener {
                                                // Write operation successful, show success toast
                                                Toast.makeText(context, "Sign Up Succeed!", Toast.LENGTH_SHORT).show()
                                                email=""
                                                username = ""
                                                password = ""
                                                selectedGender = ""

                                            }
                                            .addOnFailureListener { error ->
                                                // Write operation failed, show failure toast with error message
                                                Toast.makeText(context, "Sign up fail: ${error.message}", Toast.LENGTH_SHORT).show()
                                            }

                                    }
                                    else{
                                        Toast.makeText(context, "Invalid email format!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                      },
                            shape = RoundedCornerShape(5.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 13.dp, start = 8.dp, end = 8.dp)
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = marketplace_light_primary),
                        ) {
                            Text(text = "Sign Up")
                        }

                        Button(
                            onClick = { navController.navigate("login") },
                            modifier = Modifier.fillMaxWidth().padding(top = 7.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = marketplace_light_onPrimary),
                        ) {
                            Text(
                                buildAnnotatedString {
                                    append("Back to ")
                                    withStyle(style = SpanStyle(color = marketplace_light_primary)) {
                                        append("Sign In")
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
                        GoogleSignUpButton()
                    }
                }
            }
        }
    }
}

private fun validateInput(context: Context, email: String, username: String, password: String, selectedGender: String): Boolean {
    if(email.isEmpty() || username.isEmpty() || selectedGender.isEmpty() || password.isEmpty()){
        Toast.makeText(context, "All fields must be filled out", Toast.LENGTH_SHORT).show()
        return false
    }

    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
        return false
    }

    if (password.length !in 5..12) {
        Toast.makeText(context, "Password must be between 5 and 12 characters", Toast.LENGTH_SHORT).show()
        return false
    }

    return true
}

@Composable
private fun GoogleSignUpButton() {
    val context = LocalContext.current
    val signUpLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { firebaseAuthWithGoogle(context, it) }
            } catch (e: ApiException) {
                // Handle login failure
                Toast.makeText(context, "Google sign up failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val googleSignUpClient = rememberGoogleSignUpClient(context)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            val signUpIntent = googleSignUpClient.signInIntent
            signUpLauncher.launch(signUpIntent)
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.googleicon),
            contentDescription = "Google Sign Up",
            modifier = Modifier
                .padding(start = 8.dp)
                .size(20.dp)
        )

        Text(
            text = "Sign Up",
            modifier = Modifier
                .padding(start = 9.dp)
        )
    }
}

@Composable
private fun rememberGoogleSignUpClient(context: Context): GoogleSignInClient {
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
                val username = user?.displayName  // Retrieve the display name (username)

                if (email != null) {
                    saveUserEmailToFirebase(
                        email = email,
                        username = username,
                        onSuccess = { Toast.makeText(context, "Signed Up as: $email", Toast.LENGTH_SHORT).show() }
                    ) { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(context, "Email is null", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Handle login failure
                Toast.makeText(context, "Failed to login!", Toast.LENGTH_SHORT).show()
            }
        }
}

private fun saveUserEmailToFirebase(
    email: String?,
    username: String?,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
)  {
    if (!email.isNullOrEmpty()) {
        // Replace characters in the email to create a valid key for Firebase Realtime Database
        val emailKey = email.replace(".", ",")

        // Get reference to the "UserInfo" node in Firebase Realtime Database
        val databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo")

        // Create a UserObj instance with non-nullable parameters
        val userObj = UserObj(
            userEmail = email,
            userUsername = username ?: "",
            userPassword = "",
            userGender = ""
        )

        // Store the user's data under the emailKey in the "UserInfo" node
        databaseReference.child(emailKey).setValue(userObj)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure("Failed to save user data: ${e.message}")
            }
    } else {
        onFailure("User email is null or empty")
    }

}
fun validateEmailFormat(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    return email.matches(emailRegex.toRegex())
}

fun hashPassword(password: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(password.toByteArray(Charsets.UTF_8))
    return BigInteger(1, digest).toString(16)
}

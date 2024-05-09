package com.example.marketplace

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.marketplace.ui.theme.marketplace_light_onPrimary
import com.example.marketplace.ui.theme.marketplace_light_outline
import com.example.marketplace.ui.theme.marketplace_light_primary
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ChatScreen(navController: NavController) {
    val db = Firebase.firestore
    val messagesCollection = remember { db.collection("message") }
    val messages = remember { mutableStateListOf<Message>() }
    var listenerRegistration by remember { mutableStateOf(ListenerRegistration { }) }
    //Get parameters from previous screens for sending message
    val currentUserId: String? = navController.previousBackStackEntry?.savedStateHandle?.get("Id")
    val senderName: String? = navController.previousBackStackEntry?.savedStateHandle?.get("sender")
    val receiverId: String? = navController.previousBackStackEntry?.savedStateHandle?.get("email")
    val receiverName: String? = navController.previousBackStackEntry?.savedStateHandle?.get("receiver")
    val messageText = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    //Get parameters from previous screens for main identity of user
    val email: String? = navController.previousBackStackEntry?.savedStateHandle?.get("email")
    val username: String? = navController.previousBackStackEntry?.savedStateHandle?.get("username")
    Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor =  marketplace_light_primary,
                        titleContentColor =  marketplace_light_onPrimary,
                    ),
                    title = {
                        Text(
                            "Chat",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                            navController.currentBackStackEntry?.savedStateHandle?.set("username",username)
                            navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description",
                                tint = marketplace_light_onPrimary,
                                )
                        }
                    }
                )

        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp)
                    .padding(top = 100.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(messages) { message ->
                        if (currentUserId != null) {
                            MessageBubble(message, currentUserId)
                        }
                    }
                }
                Divider(color = Color.Gray, thickness = 1.dp)
                SendMessageInput(
                    messageText = messageText.value,
                    onMessageTextChanged = { messageText.value = it },
                    onSendMessage = {
                        if (messageText.value.isNotBlank()) {
                            //Avoiding null pointer issue
                            if (currentUserId != null && receiverId != null && senderName != null && receiverName != null) {
                                sendMessage(messagesCollection, messageText.value, currentUserId, receiverId,senderName,receiverName)
                            }
                            messageText.value = ""
                            focusManager.clearFocus()
                        }
                    }
                )
            }
        }
    )

    LaunchedEffect(Unit) {
        listenerRegistration = messagesCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.w("Chat", "Listen failed", exception)
                return@addSnapshotListener
            }

            snapshot?.let {
                if (currentUserId != null) {
                    if (receiverId != null) {
                        processMessagesSnapshot(it, messages, currentUserId,receiverId)
                    }
                }
            }
        }


    }

    DisposableEffect(Unit) {
        onDispose {
            listenerRegistration.remove()
        }
    }
}



suspend fun migrateDataFromRealtimeToFirestore() {
    //Fetch user data from real time database and migrate to firestore
    val realtimeDatabaseReference = FirebaseDatabase.getInstance().getReference("UserInfo")
    val firestore = FirebaseFirestore.getInstance()
    val usersCollection = firestore.collection("users")
    val dataSnapshot = realtimeDatabaseReference.get().await()

    dataSnapshot.children.forEach { data ->
        val userEmail = data.child("userEmail").getValue(String::class.java) ?: ""
        val userUsername = data.child("userUsername").getValue(String::class.java) ?: ""
        val userPassword = data.child("userPassword").getValue(String::class.java) ?: ""
        val userGender = data.child("userGender").getValue(String::class.java) ?: ""
        val userId = userEmail


        val existingDoc = usersCollection.document(userId).get().await()
        if (existingDoc.exists()) {

            println("Document with ID $userId already exists. Skipping.")
        } else {
            val userData = hashMapOf(
                "email" to userEmail,
                "name" to userUsername,
                "password" to userPassword,
                "gender" to userGender
            )
            //Copy data into firestore
            usersCollection.document(userId).set(userData).await()
            println("Document with ID $userId added.")
        }
    }

    println("Data migration completed.")
}


fun processMessagesSnapshot(snapshot: QuerySnapshot, messages: MutableList<Message>, currentUserId: String,receiverId:String) {
    //Fetch message data from firebase
    messages.clear()
    val newMessages = snapshot.toObjects(Message::class.java)
    //Filter message to get message associated with current user
    val filteredMessages = newMessages.filter { message ->
        (message.senderId == currentUserId &&  message.receiverId == receiverId ) || (message.receiverId == currentUserId &&  message.senderId == receiverId)
    }

    val sortedMessages = filteredMessages.sortedBy { it.timestamp }

    messages.addAll(sortedMessages)
}
@Composable
fun MessageBubble(message: Message, currentUserId: String) {
    val isCurrentUser = message.senderId == currentUserId
    val instant = Instant.ofEpochMilli(message.timestamp )
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault())
    val formattedTime = formatter.format(instant)
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!isCurrentUser) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(start = 10.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Column(
                        horizontalAlignment = Alignment.Start
                    ){
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.LightGray
                            ),
                            modifier = Modifier
                                .padding(top =  8.dp, bottom = 8.dp)
                                .padding(horizontal = 4.dp)
                        ) {
                            Text(
                                text = message.sendName + ":        " + formattedTime,
                                modifier = Modifier.padding(8.dp),
                                textAlign =  TextAlign.End,
                                style = TextStyle(fontSize = 12.sp)
                            )
                            Text(
                                text = message.text,
                                modifier = Modifier.padding(8.dp),
                                textAlign =  TextAlign.End
                            )
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.End
                    ){
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFAF3E8)
                            ),
                            modifier = Modifier
                                .padding(top =  8.dp, bottom = 8.dp)
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = "You:       at $formattedTime",
                                modifier = Modifier.padding(8.dp),
                                textAlign =  TextAlign.End,
                                style = TextStyle(fontSize = 12.sp)
                            )
                            Text(
                                text = message.text,
                                modifier = Modifier.padding(8.dp),
                                textAlign =  TextAlign.End
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 10.dp)
                    )
                }
            }
        }
    }



}


@Composable
fun SendMessageInput(
    messageText: String,
    onMessageTextChanged: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = messageText,
            onValueChange = onMessageTextChanged,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = { onSendMessage() }
            ),
            placeholder = { Text("Type a message...") },
            singleLine = true
        )
        IconButton(
            onClick = { onSendMessage() },
            enabled = messageText.isNotBlank()
        ) {
            Icon(Icons.Default.Send, contentDescription = "Send")
        }
    }
}

data class Message(
    val text: String = "",
    val senderId: String = "",
    val receiverId: String = "",
    var sendName:String = "",
    var receiverName:String = "",
    val timestamp: Long = System.currentTimeMillis()
)
fun sendMessage(
    messagesCollection: CollectionReference,
    messageText: String,
    senderId: String,
    receiverId: String,
    sendName: String,
    receiveName: String
) {
    val message = Message(messageText, senderId, receiverId,sendName,receiveName)
    messagesCollection.add(message)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(viewModel: MessageViewModel, navController: NavHostController) {
    val email: String? = navController.previousBackStackEntry?.savedStateHandle?.get("email")
    val username: String? = navController.previousBackStackEntry?.savedStateHandle?.get("username")
    val contacts = remember { mutableStateOf<List<Contact>>(emptyList()) }
    LaunchedEffect(key1 = true) {
        migrateDataFromRealtimeToFirestore()
        if (email != null) {
            viewModel.fetchMessages(currentUserID = email) { fetchedContacts ->
                contacts.value = fetchedContacts.value
            }
        }
    }

    Scaffold( topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor =  marketplace_light_primary,
                titleContentColor =  marketplace_light_onPrimary,
            ),
            title = {
                Text(
                    "Contacts",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                    navController.currentBackStackEntry?.savedStateHandle?.set("username",username)
                    navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description",
                        tint = marketplace_light_onPrimary,
                        )
                }
            },
        )
    },
        bottomBar = {
            BottomAppBar(
                containerColor =  marketplace_light_primary,
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
                            //Maintain user identify during navigation
                            navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                            navController.currentBackStackEntry?.savedStateHandle?.set("username",username)
                            navController.navigate("home")}) {
                            Icon(Icons.Filled.Home,
                                contentDescription = "Localized description",
                                tint = marketplace_light_onPrimary,)
                        }
                        IconButton(onClick = {
                            //Maintain user identify during navigation
                            navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                            navController.currentBackStackEntry?.savedStateHandle?.set("username",username)
                            navController.navigate("contact")
                        },) {
                            Icon(
                                Icons.Filled.MailOutline,
                                contentDescription = "Localized description",
                                tint = marketplace_light_outline,
                            )
                        }
                        IconButton(onClick = {
                            //Maintain user identify during navigation
                            navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                            navController.currentBackStackEntry?.savedStateHandle?.set("username",username)
                            navController.navigate("Addmerchant") }) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "Localized description",
                                tint = marketplace_light_onPrimary,
                            )
                        }
                        IconButton(onClick = {
                            //Maintain user identify during navigation
                            navController.currentBackStackEntry?.savedStateHandle?.set("email", email)
                            navController.currentBackStackEntry?.savedStateHandle?.set("username",username)
                            navController.navigate("Favourites") }) {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Localized description",
                                tint = marketplace_light_onPrimary,
                            )
                        }
                        LogoutButton(navController)

                    }}
            )
        },
    )  { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(contacts.value) { contact ->
                    if (email != null && username != null) {
                        ContactItem(contact = contact, navController = navController, username, email)
                    }
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact, navController: NavController, name: String, email:String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                //Pass data to Chat screen and record the identify of sender and receiver
                navController.currentBackStackEntry?.savedStateHandle?.set("email", contact.email)
                navController.currentBackStackEntry?.savedStateHandle?.set("receiver", contact.name)
                navController.currentBackStackEntry?.savedStateHandle?.set("sender", name)
                navController.currentBackStackEntry?.savedStateHandle?.set("Id", email)
                navController.navigate("chat")
            }
    ) {Surface(
        color = marketplace_light_onPrimary, // Set your desired background color here
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = contact.name,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = contact.email,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
        Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.fillMaxWidth())

    }


}

data class Contact(val name: String, val email: String)

class MessageViewModel : ViewModel() {
    private val db = Firebase.firestore

    fun fetchMessages(currentUserID: String, onComplete: (MutableState<List<Contact>>) -> Unit) {
        val senderIDs = mutableListOf<String>()
        val receiverIDs = mutableListOf<String>()
        val contacts = mutableStateOf<List<Contact>>(emptyList())
        //Fetch message and extract message associated with current user to build the contact list.
        viewModelScope.launch(Dispatchers.IO) {
            db.collection("message")
                .whereEqualTo("senderId", currentUserID)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val receiveID = document.getString("receiverId")
                        receiveID?.let { senderIDs.add(it) }
                    }
                    db.collection("message")
                        .whereEqualTo("receiverId", currentUserID)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            for (document in querySnapshot.documents) {
                                val senderID = document.getString("senderId")
                                senderID?.let { receiverIDs.add(it) }
                            }
                            val uniqueIDs = (senderIDs + receiverIDs).distinct()
                            //Use Ids to fetch all the user info and build contact list
                            if (uniqueIDs.isNotEmpty()) {
                                db.collection("users").whereIn(FieldPath.documentId(), uniqueIDs).get().addOnSuccessListener { querySnapshot ->
                                    val contactsList = mutableListOf<Contact>()
                                    for (document in querySnapshot.documents) {
                                        val name = document.getString("name") ?: ""
                                        val email = document.getString("email") ?: ""
                                        val newContact = Contact(name, email)
                                        contactsList.add(newContact)
                                    }
                                    contacts.value = contactsList
                                    onComplete(contacts)
                                }
                            } else {
                                Log.d("MessageViewModel", "No unique IDs found, skipping Firestore query")
                                onComplete(contacts)
                            }
                        }
                }
                .addOnFailureListener { exception ->
                    Log.e("MessageViewModel", "Error fetching messages", exception)
                }
        }
    }

}

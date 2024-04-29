package com.example.marketplace

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FieldPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val db = Firebase.firestore
    val messagesCollection = remember { db.collection("message") }
    val messages = remember { mutableStateListOf<Message>() }
    var listenerRegistration by remember { mutableStateOf(ListenerRegistration { }) }
    val messageText = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val sendName = "Stan"
    val receiveName = "St"
    val currentUserId = "qJoZYdNXv6otsvmL45Iq"
    val reid = "D9qy3RijILXCcOluhVjB"
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Chat") },

            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(messages) { message ->
                        MessageBubble(message, currentUserId)
                    }
                }
                Divider(color = Color.Gray, thickness = 1.dp)
                SendMessageInput(
                    messageText = messageText.value,
                    onMessageTextChanged = { messageText.value = it },
                    onSendMessage = {
                        if (messageText.value.isNotBlank()) {
                            sendMessage(messagesCollection, messageText.value, currentUserId, reid,sendName,receiveName)
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

            snapshot?.let { processMessagesSnapshot(it, messages, currentUserId) }
        }

    }

    DisposableEffect(Unit) {
        onDispose {
            listenerRegistration.remove()
        }
    }
}
fun processMessagesSnapshot(snapshot: QuerySnapshot, messages: MutableList<Message>, currentUserId: String) {
    messages.clear()
    val newMessages = snapshot.toObjects(Message::class.java)

    val filteredMessages = newMessages.filter { message ->
        message.senderId == currentUserId || message.receiverId == currentUserId
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
                        imageVector = Icons.Default.Person, // 使用默认的用户图标
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(start = 10.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        horizontalAlignment = Alignment.Start
                    ){

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            modifier = Modifier
                                .padding(8.dp)
                                .padding(horizontal = 8.dp)
                                .padding(start = 10.dp)
                        ) {
                            Text(
                                text = message.receiverName + ":        " + formattedTime,
                                modifier = Modifier.padding(8.dp),
                                textAlign =  TextAlign.End,
                                style = TextStyle(fontSize = 12.sp) )
                            Text(
                                text = message.text,
                                modifier = Modifier.padding(8.dp),
                                textAlign =  TextAlign.End
                            )
                        }
                    }

                } else {
                    // 如果是当前用户，则显示当前用户的默认头像和名字
                    Column(
                        horizontalAlignment = Alignment.End
                    ){

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFAF3E8)
                            ),
                            modifier = Modifier
                                .padding(8.dp)
                                .padding(horizontal = 8.dp)
                                .padding(start = 10.dp),
                        ) {
                            Text(
                                text = message.sendName + ":       from " + formattedTime,
                                modifier = Modifier.padding(8.dp),
                                textAlign =  TextAlign.End,
                                style = TextStyle(fontSize = 12.sp) )
                            Text(
                                text = message.text,
                                modifier = Modifier.padding(8.dp),
                                textAlign =  TextAlign.End
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))
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
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val currentUserId = "qJoZYdNXv6otsvmL45Iq"
    val contacts = remember { mutableStateOf<List<Contact>>(emptyList()) }
    LaunchedEffect(key1 = true) {
        viewModel.fetchMessages(currentUserID =currentUserId) { fetchedContacts ->
            contacts.value = fetchedContacts.value

        }
    }



    Scaffold( topBar = {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Text(
                    "Contacts",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
            actions = {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Localized description"
                    )
                }
            },
            scrollBehavior = scrollBehavior,
        )
    },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                actions = {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        IconButton(onClick = { /* do something */ }) {
                            Icon(Icons.Filled.Check, contentDescription = "Localized description")
                        }
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Localized description",
                            )
                        }
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                Icons.Filled.AccountBox,
                                contentDescription = "Localized description",
                            )
                        }
                        IconButton(onClick = { /* do something */ }) {
                            Icon(
                                Icons.Filled.Build,
                                contentDescription = "Localized description",
                            )
                        }
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
                    ContactItem(contact = contact, navController = navController)
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("chatScreen")
            }
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = contact.name +"   " + contact.email,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

data class Contact(val name: String, val email: String,val id:String)

class MessageViewModel : ViewModel() {
    private val db = Firebase.firestore

    fun fetchMessages(currentUserID: String, onComplete: (MutableState<List<Contact>>) -> Unit) {
        val senderIDs = mutableListOf<String>()
        val receiverIDs = mutableListOf<String>()
        val contacts = mutableStateOf<List<Contact>>(emptyList())
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

                            db.collection("users").whereIn(FieldPath.documentId(), uniqueIDs).get().addOnSuccessListener { querySnapshot ->
                                val contactsList = mutableListOf<Contact>()
                                for (document in querySnapshot.documents) {
                                    val name = document.getString("name") ?: ""
                                    val email = document.getString("email") ?: ""
                                    val id = document.id
                                    val newContact = Contact(name, email, id)
                                    contactsList.add(newContact)
                                }
                                contacts.value = contactsList
                                onComplete(contacts)
                            }
                        }
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                }
        }
    }
}
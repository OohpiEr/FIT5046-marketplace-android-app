package com.example.marketplace

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import android.widget.Toast.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface

import androidx.compose.ui.Modifier



class MainActivity : ComponentActivity() {
    private val viewModel: ProductViewModel by viewModels()
    private val addMerchant = Addmerchant()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    addMerchant.AddMerchant(viewModel)
                }
            }

    }
}




//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddMerchant(productViewModel: ProductViewModel){
//    var imageUri: Uri? by remember {
//        mutableStateOf(null)
//    }
//    val launcher =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
//            it?.let { uri ->
//                imageUri = uri
//            }
//        }
//    val context = LocalContext.current
//    var name by remember { mutableStateOf("") }
//    var price by remember { mutableStateOf("") }
//    var quantity by remember { mutableStateOf("") }
//    var address by remember { mutableStateOf("") }
//    var brand by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("") }
//    val states = listOf("VIC", "QLD", "NSW", "SA", "TAS", "WA", "ACT", "NT")
//    var isExpanded by remember { mutableStateOf(false) }
//    var selectedState by remember { mutableStateOf(states[0]) }
//    var showDialog by remember { mutableStateOf(false) }
//    Scaffold(
//        topBar = {
//            val coroutineScope = rememberCoroutineScope()
//            TopAppBar(
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor =
//                    MaterialTheme.colorScheme.primaryContainer,
//                    titleContentColor = MaterialTheme.colorScheme.primary,
//                ),
//                title = { Text(text = "Add Product",fontStyle = FontStyle.Italic) },
//                navigationIcon = {
//                    IconButton(onClick = {
//                        coroutineScope.launch {
////                            drawerState.open()
//                        }
//                    }) {
//                        Icon(Icons.Filled.Menu, contentDescription = "")
//                    }
//                },
//            )
//        }
//    ){ paddingValues ->
//    Column(modifier = Modifier
//        .fillMaxWidth()
//        .fillMaxHeight()
//        .padding(top = 15.dp) ) {
//
//        Row(modifier = Modifier
//            .padding(start = 8.dp, top = 30.dp)) {
//            Box(
//                modifier = Modifier
//                    .width(175.dp)
//                    .height(175.dp)
//                          ,
//                contentAlignment = Alignment.Center
//            )
//            {
//                AsyncImage(
//                    model = imageUri,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(175.dp),
//                    contentScale = ContentScale.Crop
//                )
//                if (imageUri == null){
//                Text("+ Add the image here", fontSize = 16.sp)}
//          }
//            Box(
//                modifier = Modifier
//                    .width(200.dp)
//                    .height(200.dp),
//                contentAlignment = Alignment.Center
//            ){      Button(
//                onClick = {
//                    launcher.launch(arrayOf("image/*"))
//                }
//            ) {
//                Text(text = "Pick Image")
//            }}
//        }
//        OutlinedTextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Product Name", fontStyle = FontStyle.Italic, fontSize = 12.sp) },
//            modifier = Modifier
//                .width(375.dp)
//                .height(65.dp)
//                .padding(4.dp)
//        )
//        OutlinedTextField(
//            value = price,
//            onValueChange = { price = it },
//            label = { Text("Price", fontSize = 12.sp, fontStyle = FontStyle.Italic) },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            modifier = Modifier
//                .width(375.dp)
//                .height(65.dp)
//                .padding(4.dp)
//        )
//        OutlinedTextField(
//            value = quantity,
//            onValueChange = {  quantity = it },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            label = { Text("Quantity", fontSize = 12.sp, fontStyle = FontStyle.Italic) },
//            modifier = Modifier
//                .width(375.dp)
//                .height(65.dp)
//                .padding(4.dp)
//        )
//        Row(modifier = Modifier.padding(start = 8.dp, top = 8.dp), horizontalArrangement= Arrangement.Center,
//        verticalAlignment = Alignment.CenterVertically) {
//            ExposedDropdownMenuBox(expanded = isExpanded, onExpandedChange = { isExpanded = it }) {
//                TextField(
//                    modifier = Modifier
//                        .menuAnchor()
//                        .height(65.dp)
//                        .width(100.dp)
//                        .focusProperties {
//                            canFocus = false
//                        }
//                        .padding(bottom = 8.dp),
//                    readOnly = true,
//                    value = selectedState,
//                    onValueChange = {},
//                    label = { Text("State") },
//                    //manages the arrow icon up and down
//                    trailingIcon = {
//                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
//                    },
//                )
//                ExposedDropdownMenu(
//                    expanded = isExpanded,
//                    onDismissRequest = { isExpanded = false })
//                {
//                    states.forEach { selectionOption ->
//                        DropdownMenuItem(
//                            text = { Text(selectionOption) },
//                            onClick = {
//                                selectedState = selectionOption
//                                isExpanded = false
//                            },
//                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
//                        )
//                    }
//                }
//            }
//            Box(modifier = Modifier
//                .padding(start = 8.dp)
//                .height(80.dp)){
//            OutlinedTextField(
//                value = address,
//                onValueChange = { address = it },
//                label = { Text("address", fontSize = 10.sp, fontStyle = FontStyle.Italic) },
//                modifier = Modifier
//                    .width(250.dp)
//                    .height(65.dp)
//                    )
//        }}
//        OutlinedTextField(
//            value = brand,
//            onValueChange = { brand = it },
//            label = { Text("Brand", fontSize = 12.sp, fontStyle = FontStyle.Italic) },
//            modifier = Modifier
//                .width(375.dp)
//                .height(65.dp)
//                .padding(4.dp)
//        )
//        OutlinedTextField(
//            value = description,
//            onValueChange = { description = it },
//            label = { Text("Description", fontSize = 12.sp, fontStyle = FontStyle.Italic) },
//            modifier = Modifier
//                .width(375.dp)
//                .height(100.dp)
//                .padding(4.dp)
//        )
//        Row(modifier = Modifier.padding(start = 8.dp, top = 8.dp)){
//            Box(modifier = Modifier
//                .height(65.dp)
//                .width(195.dp)){
////                Button(onClick = { /*TODO*/ }) {
////                    Text("   Cancel   ")
////                }
//            }
//            Text("               ")
//            Box(modifier = Modifier
//                .height(65.dp)
//                .width(195.dp)){
//                Button(onClick = { showDialog = true }) {
//                    Text("Confirm")
//                }
//            }
//        }
//        if (showDialog) {
//            AlertDialog(
//                onDismissRequest = { showDialog = false },
//                title = { Text("Confirm Action") },
//                text = { Text("Do you want to confirm or cancel this action?") },
//                confirmButton = {
//                    Button(onClick = {
//                        productViewModel.insertProduct(
//                            Product(name = name,photo = imageUri.toString(),price = price, quantity = quantity, state = selectedState,address = address, description = description ))
//                            Toast.makeText(context, "Product added successfully", Toast.LENGTH_SHORT).show()
//                        showDialog = false
//                    }) {
//                        Text("Confirm")
//                    }
//                },
//                dismissButton = {
//                    Button(onClick = { showDialog = false }) {
//                        Text("Cancel")
//                    }
//                }
//            )
//        }
//    }
// }}

//
//@Composable
//fun ImagePicker() {
//    var imageUri: Uri? by remember {
//        mutableStateOf(null)
//    }
//    val launcher =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) {
//            it?.let { uri ->
//                imageUri = uri
//            }
//        }
//    Column(
//        modifier = Modifier
//            .fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
//    ) {
//        AsyncImage(
//            model = imageUri,
//            contentDescription = null,
//            modifier = Modifier
//                .size(200.dp),
//            contentScale = ContentScale.Crop
//        )
//        Button(
//            onClick = {
//                launcher.launch(arrayOf("image/*"))
//            }
//        ) {
//            Text(text = "Pick Image")
//        }
//    }
//}






package com.example.notes.notesui





import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.data.Note
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// PIN
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinUnlockDialog(
    onUnlock: () -> Unit,
    onDismiss: () -> Unit
){
    var pinInput by remember { mutableStateOf("") }

    val correctPin = "6666"

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                if(pinInput == correctPin){
                    onUnlock()
                }
            }) {
                Text("Unlock")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Enter PIN")},
        text = {
            OutlinedTextField(
                value = pinInput,
                onValueChange = { pinInput = it},
                label = {Text("PIN")},
                singleLine = true
            )
        }
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    note: Note,
    onBack: () -> Unit,
    onDelete: (Note) -> Unit,
    onSave: (Note) -> Unit
) {
    var unlocked by remember { mutableStateOf(!note.isLocked) }
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }
    val context = LocalContext.current
    var showPinDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    val scope  = rememberCoroutineScope()
                    IconButton(onClick = {
                        scope.launch {
                            onSave(note.copy(title = title, content = content))
                            delay(800)
                            onBack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    val scope = rememberCoroutineScope()

                    IconButton(onClick = {
                        scope.launch {
                            if(unlocked) {
                                onSave(note.copy(title = title, content = content))
                                delay(800)
                                onBack()
                            }
                            else{
                                Toast.makeText(context,"What exactly are you tryna save? 😅",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Save")
                    }
                    IconButton(onClick = {

                        if(!note.isLocked || unlocked) {
                            onDelete(note)
                        }

                        else{
                            Toast.makeText(context,"Nice try, dweeb! UNLOCK IT, first! 🤡",Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                },
                windowInsets = WindowInsets.statusBars
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            if (note.isLocked && !unlocked) {

                Column( modifier = Modifier.fillMaxSize().padding(top = 125.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {

                    Icon(imageVector = Icons.Default.Lock, contentDescription = "Locked Note", modifier = Modifier.size(100.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "This note is locked. Tap to unlock.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { showPinDialog = true }) {
                        Text("Unlock")
                    }
                }
            } else {
                HorizontalDivider()
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Title", style = TextStyle(fontSize = 35.sp)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    textStyle = TextStyle(fontSize = 35.sp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Write your note...") },
                    modifier = Modifier.fillMaxSize(),
                    maxLines = Int.MAX_VALUE,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        errorBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    textStyle = TextStyle(fontSize = 20.sp)
                )
            }

            if(showPinDialog){
                PinUnlockDialog(
                    onUnlock = {
                        unlocked = true
                        showPinDialog = false
                    },
                    onDismiss = { showPinDialog = false }
                )
            }
        }
    }
}
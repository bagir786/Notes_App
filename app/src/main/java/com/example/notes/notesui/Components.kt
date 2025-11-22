package com.example.notes.notesui




import android.graphics.Insets
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notes.data.Note
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.notes.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesTopBar(
    onSearchClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text("Notes") },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
            IconButton(onClick = { expanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Settings") },
                    onClick = {
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("About") },
                    onClick = onMenuClick
                )
            }
        },
        windowInsets = WindowInsets.statusBars
    )
}


@Composable
fun NoteCard(note: Note, onClick: () -> Unit){
    Card(onClick = onClick,
        modifier = Modifier.padding(2.dp).heightIn(200.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                if(!note.isLocked) {
                    Text(
                        note.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                }
                else{
                    Text(
                        if (note.title.isNotEmpty()) note.title.take(1) + ".hidden" else "Untitled.hidden",
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                }
                if(note.isLocked){
                    Icon(Icons.Default.Lock, contentDescription = "Locked")
                }
            }

            Spacer(Modifier.height(4.dp))

            if(!note.isLocked) {
                Text(
                    text = note.content.take(50) + if (note.content.length > 50) "..." else "",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 5
                )
            }

            else{
                Text(
                    text = "Access is Restricted",
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1
                )
            }

        }
    }
}


@Composable
fun LockToggle(isLocked: Boolean, onToggle: (Boolean) -> Unit){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text("Lock this note")
        Switch(
            checked = isLocked,
            onCheckedChange = onToggle
        )
    }

    AnimatedVisibility(visible = isLocked) {
        Text(
            text = "This note will require a PIN to access.",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


@Composable
fun AnimatedEmptyState(modifier: Modifier = Modifier){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        // animation : scale
        val scale by rememberInfiniteTransition().animateFloat(
            initialValue = 1f,
            targetValue = 1.1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000),
                repeatMode = RepeatMode.Reverse
            )
        )
        Icon(
            imageVector = Icons.Default.Description,
            contentDescription = "AddNote",
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            tint = Color(0xFFF9D74C) // Windows File Explorer yellow
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "No notes yet. Tap + to add one",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}



@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(Screen.Notes, Screen.Todo)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Notes.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    when (screen) {
                        is Screen.Notes -> Icon(Icons.AutoMirrored.Default.Article, contentDescription = "Notes")
                        is Screen.Todo -> Icon(Icons.AutoMirrored.Filled.List, contentDescription = "To-Do")
                        else -> {}
                    }
                },
                label = {
                    Text(
                        when (screen) {
                            is Screen.Notes -> "Notes"
                            is Screen.Todo -> "To-Do"
                            else -> ""
                        }
                    )
                }
            )
        }
    }
}
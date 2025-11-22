package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.notes.data.NotesDatabase
import com.example.notes.data.NotesRepository
import com.example.notes.navigation.AppNavHost
import com.example.notes.notesui.BottomNavBar
import com.example.notes.ui.theme.NotesTheme
import com.example.notes.viewmodel.NotesViewModelFactory
import com.example.notes.viewmodel.NotesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Build DB + repo
        val database = NotesDatabase.getDatabase(applicationContext)
        val repository = NotesRepository(database.noteDao())

        // create viewmodel with factory
        val viewModelFactory = NotesViewModelFactory(repository)
        val notesViewModel = ViewModelProvider(this, viewModelFactory).get(NotesViewModel::class.java)

        setContent {
            val notes by notesViewModel.notes.collectAsState()
            val navController = rememberNavController()
            NotesTheme() {
                Scaffold(
                    bottomBar = { BottomNavBar(navController) },
                    contentWindowInsets = WindowInsets(0)
                ) { padding ->
                    AppNavHost(
                        navController = navController,
                        notes = notes,
                        onAddNote = { notesViewModel.addNote(it) },
                        onDeleteNote = { notesViewModel.deleteNote(it) },
                        onUpdateNote = { notesViewModel.updateNote(it) },
                        modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
                    )
                }
            }
        }
    }
}

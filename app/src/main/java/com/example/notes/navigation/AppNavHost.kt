package com.example.notes.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.notes.data.Note
import com.example.notes.notesui.AddNoteScreen
import com.example.notes.notesui.NoteDetailScreen
import com.example.notes.notesui.NotesScreen

sealed class Screen(val route: String) {
    object Notes : Screen("notes")
    object AddNote : Screen("add_note")
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Int) = "note_detail/$noteId"
    }
    object Todo : Screen("todo")
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    notes: List<Note>,
    onAddNote: (Note) -> Unit,
    onDeleteNote: (Note) -> Unit,
    onUpdateNote: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route,
        modifier = modifier
    ) {
        composable(Screen.Notes.route) {
            NotesScreen(
                notes = notes,
                onAddNoteClick = {
                    navController.navigate(Screen.AddNote.route)
                },
                onNoteClick = { note ->
                    navController.navigate(Screen.NoteDetail.createRoute(note.id))
                },
                onSearchClick = {
                    // sementara kosong biar tidak error
                }
            )
        }

        composable(Screen.AddNote.route) {
            AddNoteScreen(
                onSaveNote = {
                    onAddNote(it)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.NoteDetail.route,
            arguments = listOf(navArgument("noteId") { type = NavType.IntType })
        ) { backStackEntry ->

            val noteId = backStackEntry.arguments?.getInt("noteId")
            val note = notes.find { it.id == noteId }

            if (note != null) {
                NoteDetailScreen(
                    note = note,
                    onBack = { navController.popBackStack() },
                    onDelete = {
                        onDeleteNote(note)
                        navController.popBackStack()
                    },
                    onSave = { updatedNote ->
                        onUpdateNote(updatedNote)
                        navController.popBackStack()
                    }
                )
            } else {
                Text("Note not found")
            }
        }
    }
}

package com.example.notes.data

import kotlin.text.insert



import kotlinx.coroutines.flow.Flow

//a repository is a design pattern that acts as a mediator between your app's
//data sources (like Room, network APIs, or files) and the rest of your app — especially your ViewModels and UI.
class NotesRepository(private val noteDao: NoteDao){

    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()

    suspend fun insert(note: Note){
        noteDao.insert(note)
    }

    suspend fun update(note: Note){
        noteDao.update(note)
    }

    suspend fun delete(note: Note){
        noteDao.delete(note)
    }
}
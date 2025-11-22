package com.example.notes.data

import android.icu.text.CaseMap
import androidx.compose.ui.tooling.data.SourceContext
import androidx.room.Entity
import  androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,     //
    val timestamp: Long = System.currentTimeMillis(),
    val isLocked: Boolean = false //
)

package br.com.example.criticaltechworks.challenge.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Source")
data class SourceEntity(

    @PrimaryKey(autoGenerate = true)
    var sourceId: Int = 0,

    val id: String?,

    val name: String?
)
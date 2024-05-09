package org.d3if3011.assesment_ll_mobpro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "buku")
data class Buku (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val judulBuku: String,
    val penulisBuku: String,
    val genreBuku: String
)
package com.kofet.music.data

import android.net.Uri

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val uri: Uri,
    val albumArtUri: Uri?,
    val folder: String,
    val size: Long,
    val year: Int,
    val dataPath: String,
    val isFavorite: Boolean = false
)

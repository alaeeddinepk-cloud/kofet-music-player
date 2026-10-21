package com.kofet.music.ui

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kofet.music.data.MusicRepository
import com.kofet.music.data.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MusicViewModel : ViewModel() {
    private val repository = MusicRepository()

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    var favorites = mutableStateListOf<Long>()

    fun loadSongs(context: Context) {
        viewModelScope.launch {
            val allSongs = repository.loadSongs(context)
            _songs.value = allSongs
        }
    }

    fun toggleFavorite(songId: Long) {
        if (favorites.contains(songId)) {
            favorites.remove(songId)
        } else {
            favorites.add(songId)
        }
    }

    fun sortedByTitle(): List<Song> = _songs.value.sortedBy { it.title }
    fun sortedByArtist(): List<Song> = _songs.value.sortedBy { it.artist }
    fun sortedByAlbum(): List<Song> = _songs.value.sortedBy { it.album }
    fun sortedByDate(): List<Song> = _songs.value.sortedByDescending { it.year }
}

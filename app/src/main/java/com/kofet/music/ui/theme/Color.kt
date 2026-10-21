package com.kofet.music.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kofet.music.data.Song

@Composable
fun KofetMusicApp(permissionGranted: Boolean) {
    val viewModel: MusicViewModel = viewModel()
    val context = LocalContext.current
    val songs by viewModel.songs.collectAsState()

    LaunchedEffect(permissionGranted) {
        if (permissionGranted) {
            viewModel.loadSongs(context)
        }
    }

    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Home", "Library", "Favorites", "Settings")

    Scaffold(
        containerColor = Color(0xFF0E1220),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF121A2B)) {
                tabs.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                imageVector = when (index) {
                                    0 -> Icons.Default.Home
                                    1 -> Icons.Default.LibraryMusic
                                    2 -> Icons.Default.Favorite
                                    else -> Icons.Default.Settings
                                },
                                contentDescription = label
                            )
                        },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color(0xFF0E1220)
        ) {
            when (selectedTab) {
                0 -> HomeScreen(songs = songs.take(8), viewModel = viewModel)
                1 -> LibraryScreen(songs = songs, viewModel = viewModel)
                2 -> FavoritesScreen(songs = songs.filter { viewModel.favorites.contains(it.id) }, viewModel = viewModel)
                3 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun HomeScreen(songs: List<Song>, viewModel: MusicViewModel) {
    val accent = Brush.linearGradient(listOf(Color(0xFF7C4DFF), Color(0xFF00E5FF)))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0E1220))
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Welcome back",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Curated for your offline vibe",
                color = Color(0xFFB9C6D5),
                fontSize = 16.sp
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(accent)
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.Bottom) {
                    Text(text = "Now Playing", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Text(text = songs.firstOrNull()?.title ?: "No music found", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text(text = songs.firstOrNull()?.artist ?: "Waiting for tracks", color = Color.White.copy(alpha = 0.8f))
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Quick Picks", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("See all", color = Color(0xFF8AB4FF))
            }
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(songs) { song ->
                    Card(
                        modifier = Modifier
                            .size(width = 150.dp, height = 180.dp)
                            .clickable { },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF171F2E))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(88.dp)
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(Color(0xFF3A4C77)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White)
                            }
                            Text(song.title, color = Color.White, fontWeight = FontWeight.Bold)
                            Text(song.artist, color = Color(0xFFB9C6D5), fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item {
            MiniPlayer(songs.firstOrNull())
        }
    }
}

@Composable
fun LibraryScreen(songs: List<Song>, viewModel: MusicViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("All Songs", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
        items(songs) { song ->
            SongRow(song = song, viewModel = viewModel)
        }
    }
}

@Composable
fun FavoritesScreen(songs: List<Song>, viewModel: MusicViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Favorites", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
        if (songs.isEmpty()) {
            item {
                Text("No favorite tracks yet", color = Color(0xFFB9C6D5))
            }
        }
        items(songs) { song ->
            SongRow(song = song, viewModel = viewModel)
        }
    }
}

@Composable
fun SettingsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Settings", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
        item { SettingCard("Language", "Arabic / English / French") }
        item { SettingCard("Theme", "Dark / Light / Auto") }
        item { SettingCard("Audio Quality", "High, Balanced, Low") }
        item { SettingCard("Notifications", "Background playback") }
        item { SettingCard("Equalizer", "Bass boost, virtualizer") }
        item { SettingCard("Backup", "Restore settings") }
    }
}

@Composable
fun SongRow(song: Song, viewModel: MusicViewModel) {
    val isFavorite = viewModel.favorites.contains(song.id)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF171F2E))
            .clickable { }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF445B8F)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color.White)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(song.title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(song.artist, color = Color(0xFFB9C6D5), fontSize = 12.sp)
        }

        Icon(
            imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.Favorite,
            contentDescription = "Favorite",
            tint = if (isFavorite) Color(0xFFFFD54F) else Color(0xFFB9C6D5),
            modifier = Modifier.clickable { viewModel.toggleFavorite(song.id) }
        )
    }
}

@Composable
fun MiniPlayer(song: Song?) {
    if (song == null) return
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121A2B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(song.title, color = Color.White, fontWeight = FontWeight.Bold)
                Text(song.artist, color = Color(0xFFB9C6D5), fontSize = 12.sp)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Default.SkipPrevious, contentDescription = null, tint = Color.White)
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                Icon(Icons.Default.SkipNext, contentDescription = null, tint = Color.White)
            }
        }
    }
}

@Composable
fun SettingCard(title: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF171F2E))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, color = Color.White, fontWeight = FontWeight.Medium)
            Text(value, color = Color(0xFFB9C6D5))
        }
    }
}

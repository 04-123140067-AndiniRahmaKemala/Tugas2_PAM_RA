package com.tugas2.project

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

private data class DisplayNews(val category: String, val title: String, val raw: String)

private val newsRegex = Regex("Breaking \\[(\\w+)\\]: (.*)")

private fun parseNews(newsString: String): DisplayNews {
    val match = newsRegex.find(newsString)
    val category = match?.groups?.get(1)?.value ?: "GENERAL"
    val title = match?.groups?.get(2)?.value ?: newsString
    return DisplayNews(category, title, newsString)
}

@Composable
fun App(viewModel: NewsViewModel = viewModel { NewsViewModel() }) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val newsList = uiState.items
    val totalRead = uiState.totalCount

    val displayNewsList = remember(newsList) { newsList.map { parseNews(it) } }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFFFF0F6) // 🌸 background pink lembut
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Header(totalRead)
                HorizontalDivider(thickness = 1.dp, color = Color(0xFFFFC2D9))

                if (displayNewsList.isEmpty()) {
                    EmptyState()
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(items = displayNewsList, key = { it.raw }) { news ->
                            NewsCard(news = news)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Header(totalRead: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFE4EC)) // 🌸 header pink lebih kuat
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            Text(
                text = "News Feed",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFD63384)
            )
            Text(
                text = "Stay updated with the latest stories",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9A4D6A)
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.Article, contentDescription = "Read count", tint = Color(0xFFD63384))
            AnimatedContent(targetState = totalRead) { count ->
                Text(
                    text = "$count",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFD63384)
                )
            }
        }
    }
}

@Composable
private fun NewsCard(news: DisplayNews) {
    val icon = when (news.category) {
        "TECH" -> Icons.Default.Newspaper
        "BUSINESS" -> Icons.Default.BarChart
        "SPORTS" -> Icons.Default.SportsEsports
        else -> Icons.Default.Article
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = news.category,
                modifier = Modifier
                    .size(42.dp)
                    .padding(end = 12.dp),
                tint = Color(0xFFD63384)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = news.category,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFFD63384)
                )
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CircularProgressIndicator(color = Color(0xFFD63384))
            Text(
                "Menunggu berita terbaru...",
                color = Color(0xFF9A4D6A)
            )
        }
    }
}

package com.example.janakiprepacademy.ui.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.janakiprepacademy.data.SampleDataProvider
import com.example.janakiprepacademy.data.model.LeaderboardEntry
import com.example.janakiprepacademy.ui.theme.*

/**
 * Leaderboard Screen — All Bihar Rank + District-level rankings.
 * Shows top performers with rank medals and score metrics.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(onBackClick: () -> Unit) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val localDistrict = "Sitamarhi"

    val allData = SampleDataProvider.getSampleLeaderboard()
    val districtData = allData.filter { it.district == localDistrict }

    val displayData = if (selectedTabIndex == 0) allData else districtData

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "🏆 Leaderboard",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PureWhite)
            )
        },
        containerColor = CreamWhite
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tabs: All Bihar vs District
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = PureWhite,
                contentColor = JanakiOrange
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.Public,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (selectedTabIndex == 0) JanakiOrange else Color.Gray
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "All Bihar",
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTabIndex == 0) JanakiOrange else Color.Gray
                        )
                    }
                }
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (selectedTabIndex == 1) JanakiOrange else Color.Gray
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            localDistrict,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedTabIndex == 1) JanakiOrange else Color.Gray
                        )
                    }
                }
            }

            // Top 3 podium for All Bihar tab
            if (selectedTabIndex == 0 && displayData.size >= 3) {
                Spacer(modifier = Modifier.height(8.dp))
                PodiumSection(displayData.take(3))
            }

            // Full list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val startIndex = if (selectedTabIndex == 0) 3 else 0
                val listData = if (selectedTabIndex == 0 && displayData.size > 3) {
                    displayData.drop(3)
                } else {
                    displayData
                }

                itemsIndexed(listData) { index, entry ->
                    val rank = if (selectedTabIndex == 0) index + 4 else index + 1
                    LeaderboardRowCard(rank = rank, entry = entry)
                }

                if (listData.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("📭", fontSize = 48.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "No results from $localDistrict yet",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Gray
                                )
                                Text(
                                    "Be the first to take a mock test!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PodiumSection(top3: List<LeaderboardEntry>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(DarkSurface, DarkCard)
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // 2nd place
                if (top3.size > 1) {
                    PodiumItem(
                        rank = 2,
                        name = top3[1].userName,
                        score = top3[1].score,
                        color = SilverRank,
                        height = 90
                    )
                }
                // 1st place (tallest)
                PodiumItem(
                    rank = 1,
                    name = top3[0].userName,
                    score = top3[0].score,
                    color = GoldRank,
                    height = 120
                )
                // 3rd place
                if (top3.size > 2) {
                    PodiumItem(
                        rank = 3,
                        name = top3[2].userName,
                        score = top3[2].score,
                        color = BronzeRank,
                        height = 70
                    )
                }
            }
        }
    }
}

@Composable
private fun PodiumItem(rank: Int, name: String, score: Double, color: Color, height: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(90.dp)
    ) {
        // Crown for 1st
        if (rank == 1) {
            Text("👑", fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Avatar circle
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color)
        ) {
            Text(
                name.first().toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        Text(
            name.split(" ").first(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1
        )
        Text(
            "${score.toInt()} pts",
            style = MaterialTheme.typography.bodySmall,
            color = color,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Podium bar
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(60.dp)
                .height(height.dp)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(color.copy(alpha = 0.3f))
        ) {
            Text(
                "#$rank",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
    }
}

@Composable
private fun LeaderboardRowCard(rank: Int, entry: LeaderboardEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank badge
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(
                        when (rank) {
                            1 -> GoldRank
                            2 -> SilverRank
                            3 -> BronzeRank
                            else -> WarmGray
                        }
                    )
            ) {
                Text(
                    "#$rank",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = if (rank <= 3) Color.White else Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    entry.userName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        entry.district,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${entry.score.toInt()} pts",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = JanakiOrange
                )
                Text(
                    "${entry.accuracy}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = CorrectGreen
                )
            }
        }
    }
}

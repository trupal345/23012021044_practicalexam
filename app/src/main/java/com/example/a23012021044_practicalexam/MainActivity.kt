package com.example.a23012011075pratical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TechConferenceApp() }
    }
}

// ---------------------------
// MAIN SCREEN COMPOSABLE
// ---------------------------

@Composable
fun TechConferenceApp() {

    val allSessions = remember { sampleSessions }
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredSessions = when (selectedFilter) {
        "ALL" -> allSessions
        else -> allSessions.filter { it.tag == selectedFilter }
    }

    Scaffold(
        bottomBar = { BottomButtons() },
        containerColor = Color(0xFFF3F4FF)
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            /** Banner Image */
            item {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }

            /** Conference Info */
            item { ConferenceInfoCard() }

            /** Speakers Row */
            item { SectionTitle("Featured Speakers") }
            item { SpeakersRow() }

            /** Schedule */
            item { SectionTitle("Schedule") }
            item { FilterChips(selectedFilter) { selectedFilter = it } }

            /** Sessions */
            items(filteredSessions.size) { index ->
                SessionCard(filteredSessions[index])
            }

            /** Reviews */
            item { SectionTitle("Reviews") }
            items(sampleReviews.size) { index ->
                ReviewCard(sampleReviews[index])
            }

            item { Spacer(Modifier.height(100.dp)) }
        }
    }
}

// ---------------------------
// COMPONENTS
// ---------------------------

@Composable
fun ConferenceInfoCard() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Tech Conference 2025", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Text("Mehsana, Gujarat | 2.5 km away", color = Color.Gray, fontSize = 13.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            "Join us for a deep dive into the future of technology.",
            fontSize = 13.sp,
            color = Color(0xFF444444)
        )
        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF19C66A))
            )
            Text(" Live Updates Active", color = Color(0xFF19C66A), fontSize = 12.sp)
        }
    }
}

@Composable
fun SectionTitle(text: String) {
    Text(
        text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
    )
}

@Composable
fun SpeakersRow() {
    LazyRow(modifier = Modifier.padding(start = 12.dp)) {
        items(sampleSpeakers.size) { index ->
            SpeakerCard(sampleSpeakers[index])
        }
    }
}

@Composable
fun SpeakerCard(speaker: Speaker) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFFE2E2FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = Color(0xFF7D5FFF),
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(Modifier.height(8.dp))
        Text(speaker.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        Text(speaker.role, color = Color.Gray, fontSize = 11.sp)
    }
}

@Composable
fun FilterChips(selected: String, onSelect: (String) -> Unit) {
    val filters = listOf("ALL", "KEYNOTE", "WORKSHOP", "NETWORKING")

    Row(modifier = Modifier.padding(16.dp)) {
        filters.forEach { tag ->
            AssistChip(
                onClick = { onSelect(tag) },
                label = { Text(tag) },
                modifier = Modifier.padding(end = 8.dp),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selected == tag) Color(0xFF8264FF) else Color.Transparent,
                    labelColor = if (selected == tag) Color.White else Color.Black
                )
            )
        }
    }
}

@Composable
fun SessionCard(session: Session) {

    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .animateContentSize()
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(session.time, color = Color(0xFF8264FF), fontSize = 13.sp)
            Spacer(Modifier.width(8.dp))

            Column(Modifier.weight(1f)) {
                Text(session.title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(session.tag, fontSize = 12.sp, color = Color.Gray)
            }

            Icon(
                painter = painterResource(id = android.R.drawable.arrow_down_float),
                contentDescription = null,
                modifier = Modifier.rotate(rotation)
            )
        }

        if (expanded) {
            Spacer(Modifier.height(8.dp))
            Text(session.description, fontSize = 13.sp, color = Color.DarkGray)
            Text(session.room, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFE2E2FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = Color(0xFF7D5FFF)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(review.name, fontWeight = FontWeight.Bold)
            Text(review.text, fontSize = 13.sp, color = Color.Gray)
        }

        Text("★".repeat(review.stars), color = Color(0xFFFFB300))
    }
}

@Composable
fun BottomButtons() {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Button(
            onClick = {},
            modifier = Modifier.weight(1f)
        ) { Text("Buy Tickets") }

        OutlinedButton(
            onClick = {},
            modifier = Modifier.weight(1f)
        ) { Text("Calendar") }
    }
}

// ---------------------------
// SAMPLE DATA
// ---------------------------

data class Speaker(val name: String, val role: String)
data class Session(
    val time: String,
    val title: String,
    val tag: String,
    val description: String,
    val room: String
)
data class Review(val name: String, val text: String, val stars: Int)

val sampleSpeakers = listOf(
    Speaker("Dr. Emily Chen", "AI Researcher"),
    Speaker("Jake Wharton", "Android GDE"),
    Speaker("Sarah Connor", "Security Expert")
)

val sampleSessions = listOf(
    Session("9:00 AM", "Opening Ceremony", "ALL", "Kick-off for the event.", "Main Hall"),
    Session("10:00 AM", "The Future of AI", "KEYNOTE", "AI trends & predictions.", "Hall A"),
    Session("11:30 AM", "Kotlin Multiplatform", "WORKSHOP", "Hands-on coding workshop.", "Room 304"),
    Session("1:00 PM", "Lunch & Connect", "NETWORKING", "Meet speakers & developers.", "Cafeteria"),
    Session("2:30 PM", "Cloud Scalability", "WORKSHOP", "Scaling cloud apps.", "Room 210")
)

val sampleReviews = listOf(
    Review("Alice Johnson", "Great event! Well-organized.", 5),
    Review("Bob Smith", "Loved the keynote!", 4),
    Review("Charlie Davis", "Workshops were crowded.", 3)
)
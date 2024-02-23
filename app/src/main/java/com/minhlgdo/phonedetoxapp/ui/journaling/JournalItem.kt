package com.minhlgdo.phonedetoxapp.ui.journaling

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.minhlgdo.phonedetoxapp.data.local.model.JournalEntity

@Composable
fun JournalItem(
    journal: JournalEntity,
    onJournalEvent: (JournalingEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = journal.title, maxLines = 1, style = MaterialTheme.typography.headlineSmall,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = journal.time.toString(), style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = journal.content, maxLines = 3, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            IconButton(onClick = { onJournalEvent(JournalingEvent.DeleteJournal(journal)) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Journal"
                )
            }
        }

    }
}
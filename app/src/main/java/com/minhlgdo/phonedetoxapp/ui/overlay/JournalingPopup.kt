package com.minhlgdo.phonedetoxapp.ui.overlay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun JournalingPopup(
    onEvent: (OverlayEvent) -> Unit,
    state: OverlayUiState
) {
    AlertDialog(
        title = { Text(text = "New journal", fontSize = 18.sp) },
        onDismissRequest = { onEvent(OverlayEvent.HideJournalPopup) },
        dismissButton = {
            TextButton(onClick = { onEvent(OverlayEvent.HideJournalPopup) }) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = { onEvent(OverlayEvent.SaveJournal) }) {
                Text(text = "Save")
            }
        },
        text = {
            Column {
                Text(text = "Title")
                TextField(
                    value = state.journalTitle,
                    onValueChange = { onEvent(OverlayEvent.SetJournalTitle(it)) },
                    placeholder = { Text(text = "Title") },
                    singleLine = false,
                    maxLines = 2
                    )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text="How do you feel today?")
                TextField(
                    value = state.journalContent,
                    onValueChange = { onEvent(OverlayEvent.SetJournalContent(it)) },
                    placeholder = { Text(text = "Content") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )
            }
        }
    )
}
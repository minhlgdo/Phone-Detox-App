package com.minhlgdo.phonedetoxapp.ui.overlay

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minhlgdo.phonedetoxapp.data.local.model.ReasonEntity

@Composable
fun ReasonItem(reason: ReasonEntity, state: OverlayUiState, modifier: Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            if (state.selectedReason == reason.reason) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = reason.icon,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = reason.reason,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }

    }
}
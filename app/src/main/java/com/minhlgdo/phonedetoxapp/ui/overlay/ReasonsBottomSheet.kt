package com.minhlgdo.phonedetoxapp.ui.overlay

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.minhlgdo.phonedetoxapp.data.local.model.ReasonEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReasonsBottomSheet(
    reasonList: List<ReasonEntity>, onEvent: (OverlayEvent) -> Unit, state: OverlayUiState
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    BackHandler(enabled = sheetState.isVisible, onBack = {
        scope.launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onEvent(OverlayEvent.HideBottomSheet)
            }
        }
    })

    ModalBottomSheet(
        onDismissRequest = {
            onEvent(OverlayEvent.HideBottomSheet)
        }, sheetState = sheetState, modifier = Modifier.fillMaxHeight(0.95f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Why do you want to open ${state.appName}?",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Be mindful of your feelings and thoughts.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3)
            ) {
                items(reasonList.size) { index ->
                    val reason = reasonList[index]
                    ReasonItem(
                        reason = reason,
                        state = state,
                        modifier = Modifier
                            .padding(8.dp)
                            .height(100.dp)
                            .clickable {
                                onEvent(OverlayEvent.SelectReason(reason.reason))
                            }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.1f))
            Button(
                onClick = {
                    onEvent(OverlayEvent.UpdateAccessLog)
                    onEvent(OverlayEvent.EnterBlockedApp)
                },
                modifier = Modifier.fillMaxWidth(1f)
            ) {
                Text(text = "Save")
            }
        }
    }
}
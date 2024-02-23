package com.minhlgdo.phonedetoxapp.ui.select_apps

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.minhlgdo.phonedetoxapp.data.local.PhoneApp

@Composable
fun AppListContent(apps: List<PhoneApp>, viewModel: SelectAppsViewModel) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        LazyColumn(
            content = {
                items(apps.size) { index ->
                    val app = apps[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val isChecked = remember { mutableStateOf(app.isBlocked()) }
                        // display app icon, name and checkbox
                        Image(
                            painter = rememberDrawablePainter(drawable = app.getIcon()),
                            contentDescription = "App icon",
                            modifier = Modifier
                                .width(64.dp)
                                .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = app.getName(),
                            softWrap = true,
                            modifier = Modifier.widthIn(max = 250.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Checkbox(checked = isChecked.value,
                            onCheckedChange = {
                                isChecked.value = it
                                viewModel.onAppSelected(app, it)
                            })
                    }
                    // add space at the bottom of the last item
                    if (index == apps.size - 1) {
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
            }, verticalArrangement = Arrangement.spacedBy(2.dp)
        )
    }
}

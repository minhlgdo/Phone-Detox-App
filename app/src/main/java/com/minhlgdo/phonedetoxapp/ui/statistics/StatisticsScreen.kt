package com.minhlgdo.phonedetoxapp.ui.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.columnSeries

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen() {
    var expanded by remember { mutableStateOf(false) }
    val modelProducer = remember { CartesianChartModelProducer.build() }

    LaunchedEffect(Unit) {
        modelProducer.tryRunTransaction {
            columnSeries {
                series(
                    4, 12, 8, 16
                )
            }

        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Statistics") },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding() + 16.dp,
                    bottom = it.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                )
                .verticalScroll(rememberScrollState())
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {
                TextField(value = "Select a time range",
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.ArrowDropDown,
                            contentDescription = "Expand",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    })

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(text = { Text("Last 7 days") }, onClick = { expanded = false })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Attempts using restricted apps",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Total: 40",
                style = MaterialTheme.typography.bodyMedium
            )

            CartesianChartHost(rememberCartesianChart(
                rememberColumnCartesianLayer(),
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(),
            ), modelProducer, placeholder = { Text("Loading...") })

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Attempts using restricted apps",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Total: 40",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReasonPieChartPreview()

        }
    }
}
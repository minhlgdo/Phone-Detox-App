package com.minhlgdo.phonedetoxapp.ui.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.CartesianChartHost
import com.patrykandpatrick.vico.compose.chart.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.chart.rememberCartesianChart
import com.patrykandpatrick.vico.compose.component.rememberLineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.model.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.model.ExtraStore
import com.patrykandpatrick.vico.core.model.columnSeries
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    val modelProducer = remember { CartesianChartModelProducer.build() }
    val uiState by viewModel.uiState.collectAsState()
    val options = listOf("Last 7 days")
    var selectedOption by remember { mutableStateOf(options[0]) }

    // default values for the chart
    var chartData = (6 downTo 0).associate {
        LocalDate.now().minusDays(it.toLong()).toString() to 0
    }
    val labelListKey = ExtraStore.Key<List<String>>()
    val dateFormatter = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())

    LaunchedEffect(Unit) {

        viewModel.uiState.collect { state ->
            if (uiState.attempts.isNotEmpty()) {
                chartData = chartData + state.attempts
            }
//            println("LaunchedEffect, value of reasons: ${uiState.usageReason}")

            modelProducer.tryRunTransaction {
                // check whether extra store[ExtraStore.Key] is already set
                columnSeries {
                    series(chartData.values.toList())
                    updateExtras { it[labelListKey] = chartData.keys.toList() }
                }
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
            ExposedDropdownMenuBox(expanded = expanded,
                onExpandedChange = { expanded = !expanded }) {
                TextField(
                    value = selectedOption,
                    label = { Text("Select time range") },
                    onValueChange = { },
                    readOnly = true,
                    trailingIcon = {
                        TrailingIcon(
                            expanded = expanded
                        )
                    },
                    modifier = Modifier.menuAnchor()
                )

                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    options.forEach { text ->
                        DropdownMenuItem(onClick = {
                            selectedOption = text
                            expanded = false
                        }, text = { Text(text) })
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Attempts using restricted apps",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Total: ${uiState.totalAttempts}",
                style = MaterialTheme.typography.bodyMedium
            )

            CartesianChartHost(rememberCartesianChart(
                rememberColumnCartesianLayer(
                    listOf(
                        rememberLineComponent(
                            color = MaterialTheme.colorScheme.primary,
                            thickness = 16.dp,
                            shape = Shapes.roundedCornerShape(allPercent = 10)
                        )
                    )

                ),
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(valueFormatter = remember {
                    { x, chartValues, _ ->
                        LocalDate.parse(chartValues.model.extraStore[labelListKey][x.toInt()])
                            .format(dateFormatter)
                    }
                }),
            ),
                modelProducer,
                placeholder = { Text("Loading...") },
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Reasons for using restricted apps",
                style = MaterialTheme.typography.titleMedium
            )

            val quitFormatter = String.format("%.1f", uiState.quitRate)
            if (uiState.totalAttempts == 0) {
                Text(
                    text = "No attempts recorded",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "Among the ${uiState.totalAttempts} attempts, ${uiState.quitTimes} times ($quitFormatter%) you decided not to use the app",
                    style = MaterialTheme.typography.bodyMedium
                )

                ReasonPieChart(uiState.usageReason)
            }


        }
    }
}
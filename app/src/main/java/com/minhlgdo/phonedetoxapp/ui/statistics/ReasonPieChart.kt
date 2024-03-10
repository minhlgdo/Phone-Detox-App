package com.minhlgdo.phonedetoxapp.ui.statistics

import android.telecom.Call.Details
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.minhlgdo.phonedetoxapp.data.local.UsageResult
import com.minhlgdo.phonedetoxapp.ui.theme.Blue200
import com.minhlgdo.phonedetoxapp.ui.theme.Blue80
import com.minhlgdo.phonedetoxapp.ui.theme.Pink40
import com.minhlgdo.phonedetoxapp.ui.theme.Pink80
import com.minhlgdo.phonedetoxapp.ui.theme.Purple40
import com.minhlgdo.phonedetoxapp.ui.theme.Purple80
import com.minhlgdo.phonedetoxapp.ui.theme.PurpleGrey40
import com.minhlgdo.phonedetoxapp.ui.theme.PurpleGrey80
import com.minhlgdo.phonedetoxapp.ui.theme.Teal200
import com.minhlgdo.phonedetoxapp.ui.theme.Teal40

// Pie chart, referred from https://medium.com/@developerchunk/create-custom-pie-chart-with-animations-in-jetpack-compose-android-studio-kotlin-49cf95ef321e

@Composable
fun ReasonPieChart(
    data: List<Pair<String, UsageResult>>,
    radiusOuter: Dp = 100.dp,
    chartBarWidth: Dp = 35.dp,
    animDuration: Int = 1000
) {
    var animationPlayed by remember { mutableStateOf(false) }
    var lastValue = 0f

    // get a list of degrees for each data entry
    val degrees = data.map { 360 * it.second.prob } // 360 is the full circle
    // get a list of pairs of data entries from data

    // it is the diameter value of the Pie
    val animateSize by animateFloatAsState(
        targetValue = if (animationPlayed) radiusOuter.value * 2f else 0f, animationSpec = tween(
            durationMillis = animDuration, delayMillis = 0, easing = LinearOutSlowInEasing
        ), label = "animateSize"
    )

    // if you want to stabilize the Pie Chart you can use value -90f
    // 90f is used to complete 1/4 of the rotation
    val animateRotation by animateFloatAsState(
        targetValue = if (animationPlayed) 90f * 11f else 0f, animationSpec = tween(
            durationMillis = animDuration, delayMillis = 0, easing = LinearOutSlowInEasing
        ), label = "animateRotation"
    )

    // 10 colors for 10 data entries
    val colors = listOf(
        Purple40, PurpleGrey40, Purple80, PurpleGrey80, Teal40, Teal200, Pink40, Pink80, Blue200, Blue80
    )

    // to play the animation only once when the function is Created or Recomposed
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(top=40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pie Chart using Canvas Arc
        Box(
            modifier = Modifier
                .size(animateSize.dp),
            contentAlignment = Alignment.Center,

            ) {
            Canvas(
                modifier = Modifier
                    .size(radiusOuter * 2f)
                    .rotate(animateRotation)
            ) {
                // draw each Arc for each data entry in Pie Chart
                degrees.forEachIndexed { index, value ->
                    drawArc(
                        color = colors[index],
                        lastValue,
                        value,
                        useCenter = false,
                        style = Stroke(chartBarWidth.toPx(), cap = StrokeCap.Butt)
                    )
                    lastValue += value
                }
            }
        }

        DetailsPieChart(data = data, colors = colors)

    }
}


@Composable
fun DetailsPieChart(
    data: List<Pair<String, UsageResult>>, colors: List<Color>
) {
    // get the count for each data entry


    Column(
        modifier = Modifier
            .padding(top = 40.dp)
            .fillMaxWidth()
    ) {
        // create the data items
        data.forEachIndexed { index, value ->
            DetailsPieChartItem(
                data = value, color = colors[index]
            )
        }

    }
}

@Composable
fun DetailsPieChartItem(
    data: Pair<String, UsageResult>, height: Dp = 45.dp, color: Color
) {

    Surface(
        modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp),
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .background(
                        color = color, shape = RoundedCornerShape(10.dp)
                    )
                    .size(height)
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = data.first,
                    style = MaterialTheme.typography.labelLarge
                )
                val quitFormatter = String.format("%.1f", data.second.prob*100)
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = "${data.second.value} ($quitFormatter%)"
                )
            }

        }

    }

}

//@Preview(showBackground = true, device = Devices.PIXEL_4_XL)
//@Composable
//fun ReasonPieChartPreview() {
//    val originalData = listOf(
//        "Boredom" to 50,
//        "Stress" to 20,
////        "Tired" to 0,
//        "Procrastination" to 30,
////        "Anxious" to 0,
////        "Sad" to 0,
////        "Can not sleep" to 0,
////        "Toilet" to 0,
////        "Reply to messages" to 0
//    )
//    val percentageData = listOf(
//        "Boredom" to 0.5f,
//        "Stress" to 0.2f,
////        "Tired" to 0f,
//        "Procrastination" to 0.3f,
////        "Anxious" to 0f,
////        "Sad" to 0f,
////        "Can not sleep" to 0f,
////        "Toilet" to 0f,
////        "Reply to messages" to 0f
//    )
//    ReasonPieChart(originalData, percentageData)
//}
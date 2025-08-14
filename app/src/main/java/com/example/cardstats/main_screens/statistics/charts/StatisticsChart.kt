package com.example.cardstats.main_screens.statistics.charts

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.cardstats.ui.theme.MainRed
import com.example.cardstats.user.UserViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.koin.androidx.compose.koinViewModel

@Composable
fun StatisticsChart(
    userViewModel: UserViewModel = koinViewModel(),
    period: String = "DAY",
    modifier: Modifier = Modifier
) {
    var chartData by remember { mutableStateOf<List<ChartData>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(period) {
        isLoading = true
        chartData = userViewModel.getChartData(period)
        isLoading = false
        Log.d("StatisticsChart", "Chart data loaded: $chartData")
    }

    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(MainRed, RoundedCornerShape(16.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    } else if (chartData.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(MainRed, RoundedCornerShape(16.dp))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data available",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    } else {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(MainRed, RoundedCornerShape(16.dp))
                .padding(8.dp),
            factory = { context ->
                LineChart(context).apply {
                    setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
                    description.isEnabled = false
                    setTouchEnabled(true)
                    isDragEnabled = true
                    setScaleEnabled(true)
                    setPinchZoom(true)

                    val entriesWins = chartData.mapIndexed { index, data ->
                        Entry(index.toFloat(), data.wins)
                    }
                    val entriesLosses = chartData.mapIndexed { index, data ->
                        Entry(index.toFloat(), data.losses)
                    }

                    val dataSetWins = LineDataSet(entriesWins, "Wins").apply {
                        color = Color.Green.toArgb()
                        setDrawCircles(true)
                        lineWidth = 2f
                        setDrawValues(false)
                    }
                    val dataSetLosses = LineDataSet(entriesLosses, "Losses").apply {
                        color = Color.Red.toArgb()
                        setDrawCircles(true)
                        lineWidth = 2f
                        setDrawValues(false)
                    }

                    val lineData = LineData(dataSetWins, dataSetLosses)
                    this.data = lineData

                    xAxis.isEnabled = false
                    axisLeft.isEnabled = false
                    axisRight.isEnabled = false
                    legend.isEnabled = false

                    invalidate()
                }
            },
            update = { chart ->
                val entriesWins = chartData.mapIndexed { index, data ->
                    Entry(index.toFloat(), data.wins)
                }
                val entriesLosses = chartData.mapIndexed { index, data ->
                    Entry(index.toFloat(), data.losses)
                }

                val dataSetWins = LineDataSet(entriesWins, "Wins").apply {
                    color = Color.Green.toArgb()
                    setDrawCircles(true)
                    lineWidth = 2f
                    setDrawValues(false)
                }
                val dataSetLosses = LineDataSet(entriesLosses, "Losses").apply {
                    color = Color.Red.toArgb()
                    setDrawCircles(true)
                    lineWidth = 2f
                    setDrawValues(false)
                }

                chart.data = LineData(dataSetWins, dataSetLosses)
                chart.invalidate()
            }
        )
    }
}

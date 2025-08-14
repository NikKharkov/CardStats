package com.example.cardstats.main_screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.main_screens.calendar.CalendarViewModel
import com.example.cardstats.ui.theme.MainRed
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import org.koin.androidx.compose.koinViewModel

@Composable
fun CalendarComponent(
    modifier: Modifier = Modifier,
    calendarViewModel: CalendarViewModel = koinViewModel())
{
    val winLossMap by calendarViewModel.winLossMap.collectAsState()

    val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val startDate = currentDate.minus(DatePeriod(days = 9))
    val datesPreviousWeek = (0..6).map { startDate.plus(DatePeriod(days = it)) }
    val datesCurrentWeek = (7..13).map { startDate.plus(DatePeriod(days = it)) }
    val dayAbbreviations = arrayOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MainRed)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                datesPreviousWeek.forEach { date ->
                    val isCurrentDay = date == currentDate
                    val isBeforeCurrent = date <= currentDate
                    val winLoss = winLossMap[date] ?: Pair(0, 0)
                    val wins = winLoss.first
                    val losses = winLoss.second
                    val backgroundColor = when {
                        wins > losses -> Color(0xFF4A883D)
                        losses > wins -> Color.Red
                        wins == 0 && losses == 0 -> Color.Black
                        else -> Color.Transparent
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.background(
                            if (isBeforeCurrent) backgroundColor else Color.Transparent,
                            RoundedCornerShape(20.dp)
                        )
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            color = if (isCurrentDay) Color.Yellow else Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(8.dp)
                        )
                        Text(
                            text = dayAbbreviations[date.dayOfWeek.ordinal],
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                datesCurrentWeek.forEach { date ->
                    val isBeforeCurrent = date <= currentDate
                    val isCurrentDay = date == currentDate
                    val winLoss = winLossMap[date] ?: Pair(0, 0)
                    val wins = winLoss.first
                    val losses = winLoss.second
                    val backgroundColor = when {
                        wins > losses -> Color(0xFF4A883D)
                        losses > wins -> Color.Red
                        wins == 0 && losses == 0 -> Color.Black
                        else -> Color.Gray
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.background(
                            if (isBeforeCurrent) backgroundColor else Color.Transparent,
                            RoundedCornerShape(20.dp)
                        )
                    ) {
                        Text(
                            text = date.dayOfMonth.toString(),
                            color = if (isCurrentDay) Color.Yellow else Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(8.dp)
                        )
                        Text(
                            text = dayAbbreviations[date.dayOfWeek.ordinal],
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
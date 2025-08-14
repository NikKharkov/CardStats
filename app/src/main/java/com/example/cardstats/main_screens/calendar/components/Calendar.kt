package com.example.cardstats.main_screens.calendar.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.R
import com.example.cardstats.add_game.data.entity.GameSessionEntity
import com.example.cardstats.main_screens.calendar.CalendarViewModel
import com.example.cardstats.ui.theme.MainRed
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import org.koin.androidx.compose.koinViewModel

@SuppressLint("NewApi")
@Composable
fun Calendar(
    onDateClicked: (LocalDate, List<GameSessionEntity>) -> Unit,
    modifier: Modifier = Modifier
) {
    val calendarViewModel: CalendarViewModel = koinViewModel()
    val currentMonth by calendarViewModel.currentMonth.collectAsState()
    val selectedDate by calendarViewModel.selectedDate.collectAsState()
    val gamesForSelectedDate by calendarViewModel.gamesForSelectedDate.collectAsState()
    val winLossMap by calendarViewModel.winLossMap.collectAsState()

    val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val daysInMonth = when (val monthNumber = currentMonth.monthNumber) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (currentMonth.year % 4 == 0 && (currentMonth.year % 100 != 0 || currentMonth.year % 400 == 0)) 29 else 28
        else -> throw IllegalArgumentException("Invalid month number: $monthNumber")
    }

    val firstDayOfMonth = LocalDate(currentMonth.year, currentMonth.month, 1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek
    val dayAbbreviations = arrayOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su")

    val emptyDaysBefore = firstDayOfWeek.ordinal
    val totalDays = emptyDaysBefore + daysInMonth
    val rows = (totalDays + 6) / 7

    val monthNames = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    val monthName = monthNames[currentMonth.monthNumber - 1]
    val year = currentMonth.year

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MainRed)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_back_ios),
                contentDescription = "Previous Month",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { calendarViewModel.previousMonth() }
            )
            Text(
                text = "$monthName $year".uppercase(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(R.drawable.arrow_forward),
                contentDescription = "Next Month",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { calendarViewModel.nextMonth() }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dayAbbreviations.forEach { day ->
                Text(
                    text = day,
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Column {
            for (row in 0 until rows) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0 until 7) {
                        val dayIndex = row * 7 + col
                        val dayOfMonth = dayIndex - emptyDaysBefore + 1

                        if (dayIndex < emptyDaysBefore || dayOfMonth > daysInMonth) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                            )
                        } else {
                            val date = LocalDate(currentMonth.year, currentMonth.month, dayOfMonth)
                            val isCurrentDay = date == currentDate
                            val isSelected = date == selectedDate
                            val isBeforeCurrent = date <= currentDate
                            val winLoss = winLossMap[date] ?: Pair(0, 0)
                            val wins = winLoss.first
                            val losses = winLoss.second
                            val backgroundColor = when {
                                wins > losses -> Color(0xFF4A883D)
                                losses > wins -> Color.Red
                                wins == 0 && losses == 0 -> Color.Black
                                else -> Color.Gray
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        calendarViewModel.selectDate(date)
                                        onDateClicked(date, gamesForSelectedDate)
                                    }
                                    .padding(4.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .align(Alignment.Center)
                                        .background(
                                            if (isBeforeCurrent && !isSelected) backgroundColor else if (isSelected) Color.Transparent else Color.Transparent,
                                            RoundedCornerShape(20.dp)
                                        )
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = dayOfMonth.toString(),
                                            color = if (isCurrentDay) Color.Yellow else Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center
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
            }
        }
    }
}
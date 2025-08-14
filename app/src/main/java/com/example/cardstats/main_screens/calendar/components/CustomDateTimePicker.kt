package com.example.cardstats.main_screens.calendar.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.R
import com.example.cardstats.ui.theme.MainRed
import com.example.cardstats.util.NavigationButton
import dev.darkokoa.datetimewheelpicker.WheelDateTimePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import dev.darkokoa.datetimewheelpicker.core.format.DateOrder
import dev.darkokoa.datetimewheelpicker.core.format.MonthDisplayStyle
import dev.darkokoa.datetimewheelpicker.core.format.TimeFormat
import dev.darkokoa.datetimewheelpicker.core.format.dateFormatter
import dev.darkokoa.datetimewheelpicker.core.format.timeFormatter
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CustomDateTimePicker(
    onDateTimeSelected: (LocalDateTime) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedDateTime by remember { mutableStateOf<LocalDateTime?>(null) }

    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val currentDate = currentDateTime.date

    val minDate = LocalDateTime(currentDate.year, 1, 1,0,0)
    val maxDate = LocalDateTime(currentDate.year, 12, 31,23,59)
    val monthNames = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    if (selectedDateTime == null) {
        selectedDateTime = currentDateTime
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MainRed, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        WheelDateTimePicker(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            startDateTime = selectedDateTime ?: currentDateTime,
            minDateTime = minDate,
            maxDateTime = maxDate,
            textColor = Color.White,
            textStyle = TextStyle(fontSize = 18.sp, color = Color.White),
            onSnappedDateTime = { snappedDateTime ->
                selectedDateTime = snappedDateTime
            },
            dateFormatter = dateFormatter(
                dateOrder = DateOrder.DMY,
                monthDisplayStyle = MonthDisplayStyle.SHORT,
                formatMonth = { month, _ -> monthNames[month.number - 1] },
                formatDay = { day -> day.toString() }
            ),
            timeFormatter = timeFormatter(
                timeFormat = TimeFormat.HOUR_24
            ),
            selectorProperties = WheelPickerDefaults.selectorProperties(
                enabled = true,
                shape = RoundedCornerShape(0.dp),
                color = Color.White.copy(alpha = 0.2f),
                border = BorderStroke(2.dp, Color.White)
            )
        )

        NavigationButton(
            image = R.drawable.btn_save,
            onClick = {
                selectedDateTime?.let { onDateTimeSelected(it) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}

package com.example.cardstats.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardstats.R
import com.example.cardstats.ui.theme.MainRed
import dev.darkokoa.datetimewheelpicker.WheelDatePicker
import dev.darkokoa.datetimewheelpicker.core.WheelPickerDefaults
import dev.darkokoa.datetimewheelpicker.core.format.DateOrder
import dev.darkokoa.datetimewheelpicker.core.format.MonthDisplayStyle
import dev.darkokoa.datetimewheelpicker.core.format.dateFormatter
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.todayIn

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CustomDatePicker(
    onDateSelected: (LocalDate) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    val minDate = LocalDate(currentDate.year, 1, 1)
    val maxDate = LocalDate(currentDate.year, 12, 31)
    val monthNames =
        arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    if (selectedDate == null) {
        selectedDate = currentDate
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MainRed, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        WheelDatePicker(
            startDate = selectedDate ?: currentDate,
            minDate = minDate,
            maxDate = maxDate,
            dateFormatter = dateFormatter(
                dateOrder = DateOrder.DMY,
                monthDisplayStyle = MonthDisplayStyle.SHORT,
                formatYear = { "" },
                formatMonth = { month, _ -> monthNames[month.number - 1] },
                formatDay = { day -> day.toString() }
            ),
            size = DpSize(250.dp, 120.dp),
            rowCount = 5,
            textStyle = TextStyle(fontSize = 18.sp),
            textColor = Color.White,
            selectorProperties = WheelPickerDefaults.selectorProperties(
                enabled = true,
                shape = RoundedCornerShape(0.dp),
                color = Color.White.copy(alpha = 0.2f),
                border = BorderStroke(2.dp, Color.White)
            )
        ) { snappedDate ->
            selectedDate = snappedDate
        }

        NavigationButton(
            image = R.drawable.btn_save,
            onClick = {
                selectedDate?.let { onDateSelected(it) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )
    }
}
package com.example.cardstats.main_screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cardstats.main_screens.statistics.charts.StatisticsChart
import com.example.cardstats.main_screens.statistics.component.AverageResultComponent
import com.example.cardstats.main_screens.statistics.component.BestWorstDay
import com.example.cardstats.main_screens.statistics.component.TotalWinningsComponent
import com.example.cardstats.ui.theme.MainRed
import com.example.cardstats.user.UserViewModel
import com.example.cardstats.util.BottomNavBar
import com.example.cardstats.util.CustomTopAppBar
import com.example.cardstats.util.Wallpapers
import org.koin.androidx.compose.koinViewModel

@Composable
fun StatisticsScreen(
    navController: NavController,
    userViewModel: UserViewModel = koinViewModel(),
) {
    val usersData by userViewModel.user.collectAsState()
    var selectedPeriod by remember { mutableStateOf("DAY") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                navController = navController
            )
        },
        bottomBar = {
            BottomNavBar(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(WindowInsets.navigationBars.asPaddingValues()),
                navController = navController
            )
        }
    ) { innerPaddings ->
        Wallpapers()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPaddings)
        ) {
            Text(
                text = "STATISTICS",
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                TotalWinningsComponent(
                    points = usersData.points,
                    balance = usersData.balance,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "\uD83C\uDFAF Win percentage: ${usersData.winPercentage}%",
                        modifier = Modifier
                            .background(MainRed, RoundedCornerShape(16.dp))
                            .fillMaxWidth()
                            .padding(8.dp),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    AverageResultComponent(
                        averagePoints = usersData.averageDayPoints.toInt(),
                        averageDollars = (usersData.bestDayIncome + usersData.worstDayIncome) / 2,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                BestWorstDay(
                    supportingText = "\uD83D\uDD25 Best day",
                    day = usersData.bestDay,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    color = Color(0xFF19751E),
                    dollars = usersData.bestDayIncome,
                    points = usersData.bestDayPoints
                )
                BestWorstDay(
                    supportingText = "\uD83D\uDCC9 Worst day",
                    day = usersData.worstDay,
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    color = Color(0xFFBA0000),
                    dollars = usersData.worstDayIncome,
                    points = usersData.worstDayPoints
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("DAY", "WEEK", "MONTH").forEach { period ->
                    Text(
                        text = period,
                        modifier = Modifier
                            .background(
                                if (selectedPeriod == period) MainRed else Color.Gray,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                selectedPeriod = period
                            }
                            .padding(8.dp),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
            StatisticsChart(
                period = selectedPeriod,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
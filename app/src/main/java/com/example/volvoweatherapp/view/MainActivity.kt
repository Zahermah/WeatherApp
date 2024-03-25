package com.example.volvoweatherapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.example.volvoweatherapp.ui.theme.VolvoWeatherAppTheme
import com.example.volvoweatherapp.viewmodel.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VolvoWeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShowWeatherList()
                }
            }
        }
    }
}

@Composable
fun ShowWeatherList() {
    val viewModel: WeatherViewModel = viewModel()
    WeatherScreen(viewModel = viewModel)
}

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    val weatherList by viewModel.weatherData.observeAsState(emptyList())

    LazyColumn {
        item {
            Text(
                text = "Weather Locations",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        items(weatherList) { weather ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "City: ${weather.name}")
                    Text(text = "Temperature: ${weather.main.temp} °C")
                    Text(text = "Humidity: ${weather.main.humidity} %")
                    Text(text = "Wind Speed: ${weather.wind.speed} m/s, Direction: ${weather.wind.deg}°")
                    Text(text = "Sunrise: ${unixTimestampToDateTime(weather.sys.sunrise)}")
                    Text(text = "Sunset: ${unixTimestampToDateTime(weather.sys.sunset)}")
                    weather.weather.firstOrNull()?.let { weatherDetail ->
                        Text(text = "Description: ${weatherDetail.description}")
                        // Determine the image URL or resource ID based on the weather condition
                        val imageUrl = when (weatherDetail.id) {
                            in 200..232 -> "https://openweathermap.org/img/wn/11d@2x.png"
                            in 300..321 -> "https://openweathermap.org/img/wn/09d@2x.png"
                            in 500..531 -> "https://openweathermap.org/img/wn/09d@2x.png"
                            in 600..622 -> "https://openweathermap.org/img/wn/13d@2x.png"
                            in 701..781 -> "https://openweathermap.org/img/wn/50d@2x.png"
                            800 -> "https://openweathermap.org/img/wn/01d@2x.png"
                            in 801..804 -> "https://openweathermap.org/img/wn/03d@2x.png"
                            else -> "https://openweathermap.org/img/wn/01n@2x.png"
                        }

                        GlideImage(
                            imageUrl = imageUrl,
                            modifier = Modifier.size(64.dp),
                            contentDescription = "Weather condition"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlideImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }
        },
        modifier = modifier,
        update = { imageView ->
            Glide.with(imageView.context)
                .load(imageUrl)
                .into(imageView)
        }
    )
}

@SuppressLint("SimpleDateFormat")
fun unixTimestampToDateTime(unixTimestamp: Long): String {
    val date = Date(unixTimestamp * 1000)
    val format = SimpleDateFormat("HH:mm:ss")
    return format.format(date)
}

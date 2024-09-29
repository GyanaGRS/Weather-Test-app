package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView temperature, description, cityName;
    private ImageView weatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temperature = findViewById(R.id.temperature);
        description = findViewById(R.id.description);
        cityName = findViewById(R.id.city_name);
        weatherIcon = findViewById(R.id.weather_icon);

        fetchWeatherData("Dhenkanal");
    }

    private void fetchWeatherData(String city) {
        WeatherAPIService service = RetrofitClientInstance.getRetrofitInstance().create(WeatherAPIService.class);
        Call<WeatherResponse> call = service.getWeather(city, "94ac2b4560f9fd30380516168a0f4b5d"); // Use your key here
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();
                    temperature.setText(weather.getMain().getTemp() + "°C");
                    description.setText(weather.getWeather().get(0).getDescription());
                    cityName.setText(weather.getName());
                    Glide.with(MainActivity.this)
                            .load("https://openweathermap.org/img/w/" + weather.getWeather().get(0).getIcon() + ".png")
                            .into(weatherIcon);
                } else {
                    Log.e("MainActivity", "Response failed");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("MainActivity", "API Call Failed", t);
            }
        });
    }

}

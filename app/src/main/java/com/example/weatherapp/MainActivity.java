package com.example.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etCity;
    private Button btnFetchWeather;
    private CardView cardWeather;
    private TextView tvTemperatureCelsius, tvTemperatureFahrenheit, tvTemperatureKelvin, tvHumidity, tvWind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCity = findViewById(R.id.etCity);
        btnFetchWeather = findViewById(R.id.btnFetchWeather);
        cardWeather = findViewById(R.id.cardWeather);
        tvTemperatureCelsius = findViewById(R.id.tvTemperatureCelsius);
        tvTemperatureFahrenheit = findViewById(R.id.tvTemperatureFahrenheit);
        tvTemperatureKelvin = findViewById(R.id.tvTemperatureKelvin);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvWind = findViewById(R.id.tvWind);

        btnFetchWeather.setOnClickListener(v -> fetchWeather());
    }

    private void fetchWeather() {
        String city = etCity.getText().toString().trim();
        if (city.isEmpty()) {
            Toast.makeText(this, "Please enter a city", Toast.LENGTH_SHORT).show();
            return;
        }

        WeatherApiService apiService = ApiClient.getApiClient().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getWeather(city, "c98b9c29ef87968f2ce11a05f9717dc5");
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weather = response.body();

                    // Temperature in Celsius
                    double tempCelsius = weather.getMain().getTemp() - 273.15;
                    tvTemperatureCelsius.setText(String.format("Temperature (Celsius): %.2f°C", tempCelsius));

                    // Convert to Fahrenheit
                    double tempFahrenheit = (tempCelsius * 9 / 5) + 32;
                    tvTemperatureFahrenheit.setText(String.format("Temperature (Fahrenheit): %.2f°F", tempFahrenheit));

                    // Convert to Kelvin
                    double tempKelvin = weather.getMain().getTemp();
                    tvTemperatureKelvin.setText(String.format("Temperature (Kelvin): %.2fK", tempKelvin));

                    // Display other data
                    tvHumidity.setText(String.format("Humidity: %d%%", weather.getMain().getHumidity()));
                    tvWind.setText(String.format("Wind Speed: %.2f m/s", weather.getWind().getSpeed()));

                    cardWeather.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, "City not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

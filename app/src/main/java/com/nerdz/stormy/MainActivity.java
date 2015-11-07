package com.nerdz.stormy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by YagoErnandes on 26/10/15.
 */

public class MainActivity extends AppCompatActivity {

    //Variables Inicialization
    public static final String TAG = MainActivity.class.getSimpleName();

    private String forecastUrl, apiKey;
    private double latitude, longitude;
    private CurrentWeather mCurrentWeather;

    private TextView mTimeLabel;
    private TextView mTemperatureLabel;
    private TextView mLocationLabel;
    private TextView mHumidityValue;
    private TextView mPrecipValue;
    private TextView mSummaryLabel;

    private ImageView mIconImageView;
    private ImageView mRefreshImageView;

    private ProgressBar mProgressBar;

    //private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Standards methods of OnCreate method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setting instance for variables of UI
        variablesInstance();
        //Setting action for the refresh button
        refreshButtonAction();
        //Getting information of Weather
        getForecast(latitude,longitude);

    }

    private void refreshButtonAction() {
        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude,longitude);
            }
        });
    }

    private void variablesInstance() {
        mTimeLabel = (TextView) findViewById(R.id.timeLabel);
        mTemperatureLabel = (TextView) findViewById(R.id.temperatureLabel);
        mLocationLabel = (TextView) findViewById(R.id.locationLabel);
        mHumidityValue = (TextView) findViewById(R.id.humidityValue);
        mPrecipValue = (TextView) findViewById(R.id.precipValue);
        mSummaryLabel = (TextView) findViewById(R.id.summaryLabel);
        mIconImageView = (ImageView) findViewById(R.id.iconImageView);
        mRefreshImageView = (ImageView) findViewById(R.id.refreshImageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        //
        latitude = -15.8348;
        longitude = -48.0556;
    }

    private void getForecast(double LAT, double LONG) {
        //Setting Connection data
        apiKey = "e509410f516ccd47d11a4406414785c8";
        forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + LAT + "," + LONG;
        //Setting connection
        if(isNetworkAvailable()) {
            toogleRefresh();
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(forecastUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toogleRefresh();
                        }
                    });

                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toogleRefresh();
                        }
                    });
                    try {
                        //Response response = call.execute(); // Its a Synchronous method, it means that will execute and wait for a answer
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mCurrentWeather = getCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught ::: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "Exception caught ::: ", e);
                    }
                }
            });
        }
        //Call finish
        else{
            Toast.makeText(this, R.string.network_unavailable_message,
                    Toast.LENGTH_LONG).show();

        }
    }

    private void toogleRefresh() {
        if(mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    private void updateDisplay() {
        mTimeLabel.setText(mCurrentWeather.getFormattedTime());
        mTemperatureLabel.setText(mCurrentWeather.getTemperatureCelcius() + "");
        mLocationLabel.setText(mCurrentWeather.getTimeZone());
        mHumidityValue.setText(mCurrentWeather.getHumidity() + "");
        mPrecipValue.setText(mCurrentWeather.getPrecipChance() + "%");
        mSummaryLabel.setText(mCurrentWeather.getSummary());
        Drawable drawable = getResources().getDrawable(mCurrentWeather.getIconId());
        mIconImageView.setImageDrawable(drawable);
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        //----
        JSONObject currently = forecast.getJSONObject("currently");
        //----
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setTimeZone(forecast.getString("timezone"));
        //----
        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(netInfo != null && netInfo.isConnected())
            isAvailable = true;
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }
}

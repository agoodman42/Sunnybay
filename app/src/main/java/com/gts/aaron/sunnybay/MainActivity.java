package com.gts.aaron.sunnybay;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import org.json.JSONException;
import static com.google.android.gms.wearable.DataMap.TAG;
import static java.lang.String.format;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, AsyncResponse {
    //I spent too much time trying to extract the callbacks from main to make this
    //more modular. Definitely an area of improvement for me.


    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;



    //initialising the Google Api client for location services
    GoogleApiClient mGoogleApiClient;
    Location location;
    private LocationRequest mLocationRequest;

    float currentLatitude;
    float currentLongitude;

    TextView mLatitudeText;
    TextView mLongitudeText;
    TextView mWeatherText;
    TextView mTempMaxText;
    TextView mtempMinText;
    TextView mhumidityText;
    TextView mRainText;
    TextView mSnowText;
    TextView mRainTimeText;
    TextView mSnowTimeText;
    TextView mCloudsText;
    TextView mWindSpeedText;
    TextView mWindDirectionText;

    AsyncDownloadForecastTask downloadForecastTask;
    String forecastURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitudeText = (TextView) findViewById(R.id.LatitudeTextId);
        mLongitudeText = (TextView) findViewById(R.id.LongitudeTextId);
        mWeatherText = (TextView) findViewById(R.id.weatherDescriptionId);
        mTempMaxText = (TextView) findViewById(R.id.dailyMaxTempId);
        mtempMinText = (TextView) findViewById(R.id.dailyMinLowId);
        mhumidityText = (TextView) findViewById(R.id.humidityId);
        mRainText = (TextView) findViewById(R.id.rainId);
        mRainTimeText = (TextView) findViewById(R.id.rainTimeId);
        mSnowText = (TextView) findViewById(R.id.snowId);
        mSnowTimeText = (TextView) findViewById(R.id.snowTimeId);
        mCloudsText = (TextView) findViewById(R.id.cloudTextId);
        mWindSpeedText  = (TextView) findViewById(R.id.windSpeedId);
        mWindDirectionText  = (TextView) findViewById(R.id.windDirectionId);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();



    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        //it was necessary for me to change the permissions in my phone manually.

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //start with the low-hanging fruit, getting last location
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

        else {
            //if that doesn't work, we'll get a new one.
            handleNewLocation(location);

            currentLatitude = (float) location.getLatitude();
            currentLongitude = (float) location.getLongitude();

            mLatitudeText.setText(String.valueOf(currentLatitude));
            mLongitudeText.setText(String.valueOf(currentLongitude));
        };


        forecastURL =
                format("http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s" +
                                "&units=imperial",
                        currentLatitude,currentLongitude,"adbde9f8749df6495ab16f0906fe7d48");



        //moment of truth, does the new URL work in our asynctask
        downloadForecastTask = new AsyncDownloadForecastTask(this, forecastURL);

        //I need to set the delegate listener back to this class so we can get
        //the results of the asyncTask
        downloadForecastTask.delegate = this;
        downloadForecastTask.execute();
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //went with some boilerplate here to handle when connection failed.
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    //once we have all the pieces finished processing ( coordinates, formatted string, JSON)
    //it's time to update the View layer.
    @Override
    public void processFinish(String output) {
        String outputString = output;

        JsonWeatherParser parser = new JsonWeatherParser();
        try {
            Weather currentWeather = parser.getWeather(outputString);


                mWeatherText.setText(currentWeather.currentCondition.getDescr());
                mTempMaxText.setText(String.valueOf(currentWeather.temperature.getMaxTemp())+"°");
                mtempMinText.setText(String.valueOf(currentWeather.temperature.getMinTemp())+"°");
                mhumidityText.setText(String.valueOf(currentWeather.currentCondition.getHumidity())+"%");
                mRainText.setText(String.valueOf((int)currentWeather.rain.getAmmount())+" \"");
                mRainTimeText.setText(String.valueOf(currentWeather.rain.getTime()));
                mCloudsText.setText(String.valueOf(currentWeather.clouds.getPerc())+"%");
                mWindSpeedText.setText(String.valueOf(currentWeather.wind.getSpeed())+"mph");
                mWindDirectionText.setText(String.valueOf(currentWeather.wind.getDeg())+"°");

                if(currentWeather.snow.getAmmount() <= 0.0){ mSnowText.setText("None");
                mSnowTimeText.setText(("n/a"));
                }else {
                    mSnowText.setText(String.valueOf(currentWeather.snow.getAmmount()) + "\"");
                    mSnowTimeText.setText(String.valueOf(currentWeather.snow.getTime()));
                }

            if(currentWeather.rain.getAmmount() <= 0.0){ mRainText.setText("None");
                mRainTimeText.setText(("n/a"));
            }else {
                mRainText.setText(String.valueOf((int)currentWeather.rain.getAmmount()) + "\"");
                mRainTimeText.setText(String.valueOf(currentWeather.rain.getTime()));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

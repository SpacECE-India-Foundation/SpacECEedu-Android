package com.spacECE.spaceceedu.Location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class LocationService extends Activity {
    
    private LocationManager locationManager;
    private LocationListener locationListener;

    private String url = "http://spacefoundation.in/test/SpacECE-PHP/api/add_tracking_api.php";

    public void Start(Context context, Activity activity) {

        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("TAG", "onLocationChanged: " + location.toString());
                //make this start to start sending location to the server
//                startLocationServices(location);
            }

        };

        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        } else {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,
                    1000, 10, locationListener);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,
                        1000, 10, locationListener);
            }
        }
    }

    private void startLocationServices(Location location) {
            
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody fromBody = new FormBody.Builder()
                        .add("user_id", "16")
                        .add("latitude", String.valueOf(location.getLatitude()))
                        .add("longitude", String.valueOf(location.getLongitude()))
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(fromBody)
                        .build();
                
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        System.out.println("Registration Error ApI " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String resp = response.body().string();
                        System.out.println(resp);
                    }
                });
            }
        });
        thread.start();
    }
   
    
}


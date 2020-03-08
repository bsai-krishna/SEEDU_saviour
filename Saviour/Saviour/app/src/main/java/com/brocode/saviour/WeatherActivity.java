package com.brocode.saviour;

import android.Manifest;
import android.os.Bundle;

import com.brocode.saviour.Common.Common;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.brocode.saviour.ui.main.WeatherPagerAdapter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private CoordinatorLayout coordinatorLayout;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        coordinatorLayout = findViewById(R.id.clRoot);

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if(report.areAllPermissionsGranted())
                        {
                            buildLocationRequest();
                            buildLocationCallback();

                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(WeatherActivity.this);
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            Snackbar.make(coordinatorLayout, "Permission Denied,", Snackbar.LENGTH_LONG)
                                    .show();
                    }
                }).check();
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }

    private void buildLocationCallback() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Common.current_location =   locationResult.getLastLocation();

                WeatherPagerAdapter weatherPagerAdapter = new WeatherPagerAdapter(WeatherActivity.this, getSupportFragmentManager());
                ViewPager viewPager = findViewById(R.id.view_pager);
                viewPager.setAdapter(weatherPagerAdapter);
                TabLayout tabs = findViewById(R.id.weather_tabs);
                tabs.setupWithViewPager(viewPager);

                Log.e("Location", locationResult.getLastLocation().getLatitude() + " / " + locationResult.getLastLocation().getLongitude() );
            }
        };
    }
}
package com.microdrones.technical_test.dronepath;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.microdrones.technical_test.Calculations.Calculations;
import com.microdrones.technical_test.ClassFunctions.ClassFunctions;
import com.microdrones.technical_test.ClassFunctions.PrintLog;
import com.microdrones.technical_test.Model.CompleteFlyingDataModel;
import com.microdrones.technical_test.dronepath.databinding.ActivityDroneMapBinding;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class DroneMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "DroneMap";

    private static final int HANDLER_UPDATE_MAP = 0;
    private static boolean checkFlight;
    private static Context context;
    private static Button btnStartStop, btnNext;
    private static String[] configFileList, droneFileList, missionFileList;
    private static String[] _strConfigData, _strDroneData, _strMissionData;
    private static CountDownLatch countDownLatch;
    private static GoogleMap mMap;
    private static LatLng latLng;
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case HANDLER_UPDATE_MAP:
                    mMap.clear();

                    // Show toast on screen.
                    if (checkFlight) {
                        Toast.makeText(context, "Mission Success: Drone landed at destination.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Mission Failed: Drone landed before destination.", Toast.LENGTH_SHORT).show();
                    }

                    // Show source marker on map.
                    Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_location_red);
                    BitmapDescriptor markerIcon = ClassFunctions.getMarkerIconFromDrawable(circleDrawable);

                    int noOfPoints = CompleteFlyingDataModel.getInstance().getMissionModel().getTotalPoints();
                    latLng = new LatLng(CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(0).getLatitude(),
                            CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(0).getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(markerIcon)
                            .title("Source")
                            .snippet("Drone Start Point."));


                    // Show planned drone route on map.
                    ArrayList<LatLng> points = new ArrayList<>();
                    for (int i = 0; i < noOfPoints; i++) {
                        points.add(new LatLng(CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(i).getLatitude(),
                                CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(i).getLongitude()));
                    }

                    PolylineOptions polylineOptions = new PolylineOptions();

                    polylineOptions.addAll(points)
                            .width(5)
                            .color(Color.RED);

                    mMap.addPolyline(polylineOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                    // Show destination on map.
                    circleDrawable = getResources().getDrawable(R.drawable.ic_location_green);
                    markerIcon = ClassFunctions.getMarkerIconFromDrawable(circleDrawable);
                    latLng = new LatLng(CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(noOfPoints - 1).getLatitude(),
                            CompleteFlyingDataModel.getInstance().getMissionModel().getPoints(noOfPoints - 1).getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .icon(markerIcon)
                            .title("Destination")
                            .snippet("Drone Drop Point."));

                    // Show animation of drone moving from source to destination on planned route.
                    Bitmap bitmap;
                    circleDrawable = getResources().getDrawable(R.drawable.ic_drone);
                    try {
                        bitmap = Bitmap.createBitmap(circleDrawable.getIntrinsicWidth(), circleDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

                        Canvas canvas = new Canvas(bitmap);
                        circleDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        circleDrawable.draw(canvas);
                        ClassFunctions.setAnimation(mMap, points, bitmap);
                    } catch (OutOfMemoryError e) {
                        PrintLog.Log(TAG, "Exceptions converting bitmap: " + e.getMessage());
                    }

                    // Zoom the map for given area of map.
                    zoomRoute(mMap, points);

                    break;
            }

            return true;
        }
    });

    /**
     * This will read the data files and get the configuration, mission and drone data and store it
     * in DAO classes. This operation runs on separate thread.
     * Also after file reading it start to check the mission wise possibilities.
     */
    private final Thread readFileData = new Thread() {
        @Override
        public void run() {
            super.run();

            try {
                configFileList = getAssets().list("data/configurations");
                droneFileList = getAssets().list("data/drones");
                missionFileList = getAssets().list("data/missions");

                _strMissionData = new String[missionFileList.length];
                _strConfigData = new String[configFileList.length];
                _strDroneData = new String[droneFileList.length];
            } catch (IOException e) {
                e.printStackTrace();
            }

            /** Assumptions:
             * - number of files are equal in all data folders.
             * - Calculations would be done using Drone-n.json, Config-n.json & Missions-n.json, where as n would remain same for all 3 files.
             * e.g. Drone-1.json, Config-1.json & Missions-1.json
             * Drone-2.json, Config-2.json & Missions-2.json... etc...
             */

            for (int i = 0; i < configFileList.length; i++) {

                try {
                    InputStream is = getAssets().open("data/configurations/" + configFileList[i]);
                    int size = is.available();
                    byte[] buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    _strConfigData[i] = new String(buffer, StandardCharsets.UTF_8);

                    is = getAssets().open("data/drones/" + droneFileList[i]);
                    size = is.available();
                    buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    _strDroneData[i] = new String(buffer, StandardCharsets.UTF_8);

                    is = getAssets().open("data/missions/" + missionFileList[i]);
                    size = is.available();
                    buffer = new byte[size];
                    is.read(buffer);
                    is.close();
                    _strMissionData[i] = new String(buffer, StandardCharsets.UTF_8);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < _strConfigData.length; i++) {
                countDownLatch = new CountDownLatch(1);
                CompleteFlyingDataModel.getInstance().init(_strConfigData[i], _strDroneData[i], _strMissionData[i]);

                checkFlight = Calculations.checkIfFlightPossible();
                mHandler.obtainMessage(HANDLER_UPDATE_MAP, CompleteFlyingDataModel.getInstance()).sendToTarget();
                if (checkFlight) {
                    Log.e(TAG, "mission-" + (i + 1) + ": true - The mission was flown successfully.");
                } else {
                    Log.e(TAG, "mission-" + (i + 1) + ": false - The mission could not be flown successfully.");
                }
                try {
                    countDownLatch.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };
    private ActivityDroneMapBinding binding;


    /**
     * Zoom map for the provided area.
     */
    public static void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty()) return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 100;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDroneMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        context = this;
        btnStartStop = findViewById(R.id.btn_start_stop);
        btnNext = findViewById(R.id.btn_next_mission);

        // Start the mission
        btnStartStop.setOnClickListener(view -> {
            readFileData.start();
            btnStartStop.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
        });

        // Run next mission
        btnNext.setOnClickListener(view -> {
            countDownLatch.countDown();
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
    }
}
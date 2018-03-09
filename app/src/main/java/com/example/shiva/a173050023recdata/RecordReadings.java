package com.example.shiva.a173050023recdata;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class RecordReadings extends Service implements SensorEventListener, LocationListener {
    private static final String DEBUG_TAG = "RecordReadingsService";
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private double accelx;
    private double accely;
    private double accelz;
    private double lat;
    private double lon;
    private boolean firstEntry;
    private SensorManager sensorManager;
    private LocationManager locationManager;
    private String filename = null;
    private String csvName = null;
    @Override
    public void onDestroy() {
        Log.v(DEBUG_TAG, "Service destroy called");
        locationManager.removeUpdates(this);
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }



    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        firstEntry = true;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensorAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorAccel, SensorManager.SENSOR_DELAY_NORMAL);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isNetworkEnabled) {
            Log.v(DEBUG_TAG, "Network not enabled");
            return START_NOT_STICKY;
        }
        Criteria criteria = new Criteria();
        String provider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.v(DEBUG_TAG, "No GPS Permission");
            return  START_NOT_STICKY;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(provider, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // grab the values and timestamp -- off the main thread
        Log.v(DEBUG_TAG, "onSensorChanged fired");
        accelx = event.values[0];
        accely = event.values[1];
        accelz = event.values[2];
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        SharedPreferences pref = getSharedPreferences("my_pref", 0);
        String label = pref.getString("label", null);
        if(firstEntry)
            csvName = ts;
        new SensorEventLoggerTask(lat, lon, accelx, accely, accelz, firstEntry, ts, label, csvName).execute();
        firstEntry = false;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v(DEBUG_TAG, "onLocationChanged fired");
        lat = location.getLatitude();
        lon = location.getLongitude();
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        SharedPreferences pref = getSharedPreferences("my_pref", 0);
        String label = pref.getString("label", null);
        if(firstEntry)
            csvName = ts;
        new SensorEventLoggerTask(lat, lon, accelx, accely, accelz, firstEntry, ts, label, csvName).execute();
        firstEntry = false;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private class SensorEventLoggerTask extends AsyncTask<Void, Void, Void> {
        private double accelx;
        private double accely;
        private double accelz;
        private double lat;
        private double lon;
        private boolean firstEntry;
        private String ts;
        private String label;
        private String csvName;

        SensorEventLoggerTask(double lat, double lon, double accelx, double accely, double accelz, boolean firstEntry, String ts, String label, String csvName)
        {
            this.lat = lat;
            this.lon = lon;
            this.accelx = accelx;
            this.accelx = accely;
            this.accelx = accelz;
            this.firstEntry = firstEntry;
            this.ts = ts;
            this.label = label;
            this.csvName = csvName;
        }
        @Override
        protected Void doInBackground(Void... params) {
            // log the value
            if(!isExternalStorageWritable())
            {
                Log.v(DEBUG_TAG, "External storage not writable");
                return null;
            }
            File folder = new File(getApplicationContext().getFilesDir() + File.separator + "Readings");
            if (!folder.exists())
            {
                Log.v(DEBUG_TAG, "Folder doesn't exist");
                if (!folder.mkdir())
                {
                    Log.v(DEBUG_TAG, "Couldn't create folder" + folder.mkdir());
                    return null;
                }
            }
            if(filename == null)
                filename = folder.toString() + File.separator + csvName + ".csv";
            Log.v(DEBUG_TAG, "Path is: " + filename);
            try
            {
                FileWriter writer = null;
                SharedPreferences pref = getApplicationContext().getSharedPreferences("my_pref", 0);
                if(firstEntry)
                {
                    writer = new FileWriter(filename);
                    writer.write(pref.getString("fname", null) + pref.getString("lname", null) + "," + pref.getString("contact", null) + "," + pref.getString("email", null) + "," + pref.getInt("gender", 0) + "," + pref.getString("age", null) + "\n");
                    writer.write("timestamp,lat,long,accelx,accely,accelz,label\n");
                    writer.flush();
                    writer.close();
                }
                writer = new FileWriter(filename, true);
                writer.write(ts + "," + lat + "," + lon + "," +  accelx + "," +  accely+ "," +  accelz + "," + label + "\n");
                writer.close();
                File folder_vis = new File(Environment.getExternalStorageDirectory() + File.separator + "Readings");
                String filename_vis = folder_vis.toString() + File.separator + csvName + ".csv";
                File path = new File(folder_vis.toString());
                path.mkdirs();
                if(firstEntry)
                {
                    writer = new FileWriter(filename_vis);
                    writer.write(pref.getString("fname", null) + pref.getString("lname", null) + "," + pref.getString("contact", null) + "," + pref.getString("email", null) + "," + pref.getInt("gender", 0) + "," + pref.getString("age", null) + "\n");
                    writer.write("timestamp,lat,long,accelx,accely,accelz,label\n");
                    writer.flush();
                    writer.close();
                }
                writer = new FileWriter(filename_vis, true);
                writer.write(ts + "," + lat + "," + lon + "," +  accelx + "," +  accely+ "," +  accelz + "," + label + "\n");
                writer.close();
            }
            catch (IOException e)
            {
                Log.v(DEBUG_TAG, "IOException encountered " + e.getMessage());
            }

            return null;
        }
    }
}

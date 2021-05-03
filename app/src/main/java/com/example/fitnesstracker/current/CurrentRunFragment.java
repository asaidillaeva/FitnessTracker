package com.example.fitnesstracker.current;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.fitnesstracker.App;
import com.example.fitnesstracker.R;
import com.example.fitnesstracker.RunItem;
import com.example.fitnesstracker.data.RunDao;
import com.example.fitnesstracker.data.RunDatabase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class CurrentRunFragment extends Fragment implements OnMapReadyCallback, GoogleMap.SnapshotReadyCallback,
        GoogleMap.OnMapLoadedCallback {

    GoogleMap mGoogleMap;
    private View mView;
    private MapView mapView;
    double distance = 0;
    int millis;
    double speed;
    TextView speedTextView;
    TextView distanceTextView;
    Chronometer chronometer;
    EditText nameOfRun;
    private Handler handler = new Handler();
    private RunDao runItemDao;
    private String imageMap;
    View runView;
    FloatingActionButton fabStartRun;
    FloatingActionButton fabStopRun;
    private ArrayList<LatLng> listOfLatLng;

    public CurrentRunFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_current_run, container, false);

        runView = mView.findViewById(R.id.modeRun);
        fabStartRun = mView.findViewById(R.id.fab_start);
        fabStartRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                App.locationChanges = null;
                fabStartRun.setVisibility(View.GONE);
                fabStopRun.setVisibility(View.VISIBLE);
                runView.setVisibility(View.VISIBLE);
                startRun();
            }
        });

        fabStopRun = mView.findViewById(R.id.fabStop);
        fabStopRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sure();
            }
        });

        speedTextView = mView.findViewById(R.id.speedRunMode);
        distanceTextView = mView.findViewById(R.id.distanceRunMode);
        chronometer = mView.findViewById(R.id.durationRunMode);
        nameOfRun = mView.findViewById(R.id.editText);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
    }

    private void startRun() {
        handler.post(run);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();


    }

    private final Runnable run = new Runnable() {
        @Override
        public void run() {
            float result = 0;
            if (App.locationChanges != null) {
                int size = App.locationChanges.size();
                double startLatitude;
                double startLongitude;
                if (size == 1) {
                    startLatitude = App.startLocation.getLatitude();
                    startLongitude = App.startLocation.getLongitude();

                } else {
                    startLatitude = App.locationChanges.get(0).getLatitude();
                    startLongitude = App.locationChanges.get(0).getLongitude();
                }
                double endLatitude = App.locationChanges.get(size - 1).getLatitude();
                double endLongitude = App.locationChanges.get(size - 1).getLongitude();
                Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, new float[]{result});
                drawPolygon(App.locationChanges);
                App.locationChanges = null;
            }
            distance += result;
            distanceTextView.setText((int) distance + "m");
            millis = (int) (SystemClock.elapsedRealtime() - chronometer.getBase()) / 3600000;
            if (distance == 0) {
                speed = 0;
            } else {
                speed = distance / millis;
            }
            speedTextView.setText((int) speed + "m/s");
            result = 0;
            handler.postDelayed(this, 1000);
        }
    };

    private void sure() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        builder.setTitle("Do you want to stop run?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stopRun();
            }
        });

        builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(), "Continue running", Toast.LENGTH_SHORT);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void stopRun() {
        chronometer.stop();
        handler.removeCallbacksAndMessages(null);
        save();

    }

    private void save() {
        if (nameOfRun.getText().toString().trim().equals("")) {
            nameOfRun.setText("Unknown run");
        }
        onMapLoaded();
        AddRunItemAsyncTask task = new AddRunItemAsyncTask();
        task.execute();

        mGoogleMap.setOnMapLoadedCallback(this);
        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
        runView.setVisibility(View.GONE);
        fabStopRun.setVisibility(View.GONE);
        fabStartRun.setVisibility(View.VISIBLE);
        updateUI();
    }

    public void updateUI() {
        nameOfRun.setText("");
        speedTextView.setText("");
        distanceTextView.setText("");
        mapView.onStart();
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng latLng;
        try {
            latLng = new LatLng(App.startLocation.getLatitude(), App.startLocation.getLongitude());
        } catch (NullPointerException e) {
            latLng = new LatLng(42, 71);
        }
        listOfLatLng.add(latLng);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Your current location"));

    }

    private void drawPolygon(ArrayList<Location> locChanges) {

        int length = locChanges.size();
        if (length == 0) {
            return;
        }

        PolygonOptions poly = new PolygonOptions();
        poly.fillColor(Color.BLUE);
        poly.add(new LatLng(App.startLocation.getLatitude(), App.startLocation.getLongitude()));

        for (int i = 0; i < length; i++) {
            poly.add(new LatLng(locChanges.get(i).getLatitude(), locChanges.get(i).getLongitude()));
        }
        mGoogleMap.addPolygon(poly);
    }


    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    public void onMapLoaded() {
        if (mGoogleMap != null) {
            mGoogleMap.snapshot(this);
        }
    }

    @Override
    public void onSnapshotReady(Bitmap bitmap) {
        if (isExternalStorageWritable()) {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/saved_images");
            myDir.mkdirs();

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fname = "Shutta_" + timeStamp + ".jpg";

            File file = new File(myDir, fname);
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            imageMap = file.getAbsolutePath();
        }
    }


    public class AddRunItemAsyncTask extends AsyncTask<RunItem, Void, RunItem> {


        @Override
        protected void onPostExecute(RunItem product) {
            super.onPostExecute(product);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected RunItem doInBackground(RunItem... runItems) {
            DateTimeFormatter tf = DateTimeFormatter.ofPattern(" HH:mm:ss");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDateTime now = LocalDateTime.now();
            String date = df.format(now);
            String time = tf.format(now);

            runItemDao = RunDatabase.getDatabase(getContext()).runDao();
            RunItem runItem = new RunItem(nameOfRun.getText().toString(), distance, speed, millis*360, imageMap, date, time);
            runItemDao.insert(runItem);
            return null;
        }
    }
}

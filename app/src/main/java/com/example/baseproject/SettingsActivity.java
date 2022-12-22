package com.example.baseproject;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.security.Permissions;
import java.security.Timestamp;
import java.security.acl.Permission;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements SensorEventListener {

    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 123;

    SensorManager sensorManager;
    Sensor lightSensor;
    EditText nameEditText;
    TextView lightValueTextView;
    RecyclerView sensorsList;
    ImageView imageView;
    File photoFile;
    Button makePhotoButton;
    Button saveButton;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        nameEditText = findViewById(R.id.nameEditText);
        lightValueTextView = findViewById(R.id.lightValue);
        sensorsList = findViewById(R.id.sensorsList);
        imageView = findViewById(R.id.imagePhoto);
        makePhotoButton = findViewById(R.id.makePhotoButton);
        saveButton = findViewById(R.id.saveButton);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        makePhotoButton.setOnClickListener(view -> TryToGetPermissionAndMakePhoto(Manifest.permission.CAMERA));
        saveButton.setOnClickListener(view -> SaveValues());

        SetSavedName();
        SetSavedPhoto();
        SetSensorList();
    }

    private void SetSavedName() {
        String studentName = preferences.getString("name", null);
        if (studentName != null){
            nameEditText.setText(studentName);
        }
    }

    private void SetSavedPhoto() {
        String imagePath = preferences.getString("student_photo_path", null);
        if (imagePath != null && !imagePath.equals("")) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(myBitmap);
        }
    }

    private void SetSensorList() {
        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        String[] arraySensors = new String[sensors.size()];
        int index = 0;
        for (Sensor sensor : sensors) {
            arraySensors[index] = sensor.getName();
            index++;
        }
        sensorsList.setLayoutManager(new LinearLayoutManager(this));
        SensorsListAdapter adapter = new SensorsListAdapter(arraySensors);
        sensorsList.setAdapter(adapter);
    }

    private void TryToGetPermissionAndMakePhoto(String PERMISSION) {
        int permissionCheck = ActivityCompat.checkSelfPermission(this, PERMISSION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION)) {
                showExplanation(
                        getResources().getString(R.string.get_permissions_label),
                        getResources().getString(R.string.make_photo_permission_label),
                        PERMISSION,
                        PERMISSION_REQUEST_CODE);
            }
            else {
                requestPermission(PERMISSION, PERMISSION_REQUEST_CODE);
            }
        }
        else {
            MakePhoto();
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> requestPermission(permission, permissionRequestCode));
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    private void MakePhoto() {
        Intent makePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (makePhotoIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e("CAMERA","Unable to create photo file", ex);
            }

            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile);
                makePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                try {
                    startActivityForResult(makePhotoIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException ex){
                    Log.e("ACTIVITY", "Unable to start activity", ex);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String fileName = "JPEG_STUDENT_PHOTO";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(fileName, ".jpg", storageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float lux = sensorEvent.values[0];
        lightValueTextView.setText(String.format("%s lux", lux));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    private void SaveValues() {
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putString("name", nameEditText.getText().toString());
        if (photoFile != null){
            prefEditor.putString("student_photo_path", photoFile.getAbsolutePath());
        }
        prefEditor.apply();

        finish();
    }
}
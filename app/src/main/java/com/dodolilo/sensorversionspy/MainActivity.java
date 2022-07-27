package com.dodolilo.sensorversionspy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorSpy sensorSpy = new SensorSpy(this);
        String sensorsInf = sensorSpy.getAllSensorInf();
        String phoneName = Build.MODEL;
        CsvDataTools.saveCsvToExternalStorage(phoneName.concat(" Sensor Informations"), CsvDataTools.FileSaveType.CSV, sensorsInf, this);
    }


}
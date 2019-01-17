package com.example.ksi.androidweargesture;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.Arrays;

/**
 * Created by Ksi on 1.12.2018.
 */

public class SensorReader implements SensorEventListener {
    private static SensorReader sensorReader;

    private final SensorManager mSensorManager;
    private final Sensor mAccelerometerSensor;
    private final Queue queue = Queue.getInstance();


    public static SensorReader getInstance() {
        System.out.println("Sensor Instance");
        //System.out.println(sensorReader);
        if(sensorReader == null) {
            System.out.println("SensorReader = NULL");
            sensorReader = new SensorReader();
        }

        return sensorReader;
    }

    private SensorReader() {
        //Sensormanager'i context'siz cagiramadigimiz icin getSensorManager methoduna ihtiyacimiz var.
        mSensorManager = MainActivity.getSensorManager();
        System.out.println("Sensor Manager: " + mSensorManager);
        System.out.println(mSensorManager.getSensorList(Sensor.TYPE_LINEAR_ACCELERATION));
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    void enable() {
        Queue.getInstance().reset();

        registerSensor();
    }

    void disable() {
        unRegisterSensor();
    }


    private void registerSensor() {
        mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
    }


    private void unRegisterSensor() {
        mSensorManager.unregisterListener(this, mAccelerometerSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //Log.d("Sensor", "valuelar degisti");
        final float[] gravity = new float[3];
        final float[] linear_acceleration = new float[3];
        final float[] values = new float[3];
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION ) {
            values[0] = (int)(event.values[0] * 100.);
            values[1] = (int)(event.values[1] * 100.);
            values[2] = (int)(event.values[2] * 100.);

//            System.out.println(Arrays.toString(values));
//            handler.post(new Runnable() {
//                public void run() {
//                    queue.add(values);
//                }
//            });
            queue.add(values);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO: should I consider this?
    }

}

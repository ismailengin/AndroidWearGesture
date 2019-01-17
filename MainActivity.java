package com.example.ksi.androidweargesture;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.KeyEvent;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends WearableActivity {

    public static TextView mTextView;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private static Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mDevice;
    private BluetoothSocket mBtSocket;
    private static OutputStream mmOutputStream;

    public MainActivity() {
    }

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainActivity.context = this.getApplicationContext();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        mTextView = (TextView) findViewById(R.id.text);
        SensorReader.getInstance().enable();
        Queue.getInstance().enable();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }

        if (mBluetoothAdapter != null) {

            String address = "00:21:13:00:FA:FD";

            System.out.println("Bluetooth devices:" + mBluetoothAdapter.getBondedDevices());
            if(BluetoothAdapter.checkBluetoothAddress(address))
            {
                mDevice = mBluetoothAdapter.getRemoteDevice(address);

            }
            try {
                mBtSocket = mDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mBluetoothAdapter.cancelDiscovery();
            try {
                mBtSocket.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        try {
            mmOutputStream = mBtSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Enables Always-on
        setAmbientEnabled();
    }

    @Override
    protected void onDestroy() {
        Queue.getInstance().disable();
        try {
            mBtSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch(keyCode) {
            case KeyEvent.KEYCODE_NAVIGATE_NEXT: // Flick wrist out
                GestureInterpreter.getInstance().setGesture(2);
                return true;
            case KeyEvent.KEYCODE_NAVIGATE_PREVIOUS: // Flick Wrist in
                GestureInterpreter.getInstance().setGesture(1);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static SensorManager getSensorManager() {
        return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public static AssetManager getAssetManager() {
        return  context.getAssets();
    }

    public static void ChangeText(String a) {
        mTextView.setText(a);
    }

    public static void writeBtSocket(String a) {
        try {
            mmOutputStream.write(a.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package com.example.phonedialler;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PhoneDialerActivity extends AppCompatActivity
                                implements SensorEventListener {

    private EditText phoneNumber;
    private SensorManager sensorManager;
    private TextView accelerationOx;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float ax = event.values[0];
        accelerationOx.setText(String.format("Ox: %.2f m/s²", ax));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dialer);

        //  Ox Acceleration text box
        accelerationOx = findViewById(R.id.accelerationOx);

        //  Phone number text box
        phoneNumber = findViewById(R.id.phoneNumber);
        phoneNumber.setEnabled(false);


        //  DIGITS
        Button[] numberButtons = new Button[10];
        for (int digit = 0; digit <= 9; digit++) {
            String buttonID = "btn" + digit;
            @SuppressLint("DiscouragedApi")
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());

            numberButtons[digit] = findViewById(resID);

            //  Set on-click listener
            numberButtons[digit].setOnClickListener(new Listener());
        }

        //  SYMBOLS
        Button starBtn = findViewById(R.id.btnstar);
        starBtn.setOnClickListener(new Listener());

        Button hashBtn = findViewById(R.id.btnhash);
        hashBtn.setOnClickListener(new Listener());

        //  DELETE
        ImageButton deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(v -> {
            String currentText = phoneNumber.getText().toString();
            if (!currentText.isEmpty()) {
                phoneNumber.setText(currentText.substring(0, currentText.length() - 1));
            }
        });

        //  START CALL
        ImageButton callButton = findViewById(R.id.callBtn);

        callButton.setOnClickListener(v -> {
            String number = phoneNumber.getText().toString();
            if (!number.isEmpty()) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    return;
                }
                startActivity(callIntent);
            }
        });

        //  END CALL
        ImageButton closeButton = findViewById(R.id.endCallBtn);
        closeButton.setOnClickListener(v -> finish());

        //  Sensors
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);

        //  LAB 4 - CONTACTS MANAGER
        ImageButton contactsManagerButton = findViewById(R.id.contactsManager);
        contactsManagerButton.setOnClickListener(v -> {
            String extractedNumber = phoneNumber.getText().toString();

            if (!extractedNumber.isEmpty()) {
                Intent intent = new Intent("com.example.contactsmanager.intent.action.ContactsManagerActivity");
                intent.putExtra("com.example.contactsmanager.PHONE_NUMBER_KEY", extractedNumber);
                startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == Constants.CONTACTS_MANAGER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Contact saved successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Something went wrong. Couldn't save contact", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class Listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button b = (Button) v;
            phoneNumber.append(b.getText());
        }
    }

}
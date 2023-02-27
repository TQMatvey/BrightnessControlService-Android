package org.tqmatvey.brightnessctrl;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start the background service
        Intent serviceIntent = new Intent(this, BackgroundService.class);
        startService(serviceIntent);

        finish();
    }
}

package org.tqmatvey.brightnessctrl;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

public class BackgroundService extends Service {

    private ContentObserver mBrightnessObserver;
    private boolean mIsRunning;

    @Override
    public void onCreate() {
        super.onCreate();
        mIsRunning = false;
        mBrightnessObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                int currentBrightness = getBrightness();
                if (currentBrightness == 255 && currentBrightness != 561) {
                    setBrightness(561);
                }
            }
        };
        getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mBrightnessObserver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mIsRunning) {
            mIsRunning = true;
            Log.d("BackgroundService", "Service started");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mBrightnessObserver);
        mIsRunning = false;
        Log.d("BackgroundService", "Service stopped");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private int getBrightness() {
        int brightness = 0;
        try {
            Process suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            os.writeBytes("cat /sys/class/backlight/panel/brightness\n");
            os.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(suProcess.getInputStream()));
            String line = reader.readLine();
            brightness = Integer.parseInt(line);
            os.writeBytes("exit\n");
            os.flush();
            suProcess.waitFor();
        } catch (Exception e) {
            Log.e("BackgroundService", "Failed to read brightness: " + e.getMessage());
        }
        return brightness;
    }

    private void setBrightness(int brightness) {
        try {
            Process suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            os.writeBytes("echo " + brightness + " > /sys/class/backlight/panel/brightness\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            suProcess.waitFor();
        } catch (Exception e) {
            Log.e("BackgroundService", "Failed to set brightness: " + e.getMessage());
        }
    }
}
package org.tqmatvey.brightnessctrl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Objects;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED)) {
            // Start your service here
            Intent serviceIntent = new Intent(context, BackgroundService.class);
            context.startForegroundService(serviceIntent);
        }
    }
}

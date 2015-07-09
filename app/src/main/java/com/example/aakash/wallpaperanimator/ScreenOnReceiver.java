package com.example.aakash.wallpaperanimator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenOnReceiver extends BroadcastReceiver {
    public ScreenOnReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Screen on", "Broadcast Received");
        Intent i = new Intent(context,ChangeWallpaper.class);
        context.startService(i);
    }
}

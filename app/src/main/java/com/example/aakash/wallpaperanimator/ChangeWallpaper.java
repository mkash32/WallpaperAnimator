package com.example.aakash.wallpaperanimator;

import android.app.IntentService;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class ChangeWallpaper extends IntentService {
    public ChangeWallpaper(){super("Change Wallpaper");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences pref = getSharedPreferences(MainActivity.SPreferences, MODE_PRIVATE);
        int current = pref.getInt("current", -1);
        int size = pref.getInt("size", 0);
        Log.d("Change Wallpaper:", "Switching...");
        if(size!=0) {
            if (++current == size)
                current = 0;
            WallpaperManager wm = WallpaperManager.getInstance(this);
            Uri uri = Uri.parse(pref.getString("" + current, ""));

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                wm.setBitmap(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("current",current);
            editor.commit();
        }
    }
}

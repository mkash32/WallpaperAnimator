package com.example.aakash.wallpaperanimator;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{
    private ListView listView;
    private Button addButton,changeButton;
    private ArrayList<Bitmap> bitmaps=new ArrayList<Bitmap>();
    private ArrayList<Uri> uris = new ArrayList<Uri>();
    private static ArrayList<Uri> nuris = new ArrayList<Uri>();
    private ImageListAdapter iladapter;
    private final int PIC_SELECT_REQUEST=1;
    public static final String SPreferences = "MyWallpapers";
    private Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listView);
        addButton = (Button)findViewById(R.id.add_button);
        changeButton = (Button)findViewById(R.id.change_button);
        load();
        iladapter = new ImageListAdapter(bitmaps,this);
        listView.setAdapter(iladapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select a picture"), PIC_SELECT_REQUEST);
            }
        });

        changeButton.setOnClickListener(this);
        serviceIntent = new Intent(MainActivity.this,ScreenOnOffService.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences pref = getSharedPreferences(SPreferences,MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int size = pref.getInt("size",0);
        for(int i=0;i<nuris.size();i++)
            editor.putString(""+(size+i),nuris.get(i).getPath());
        size += nuris.size();
        editor.putInt("size", size);
        editor.commit();
        Log.d("URI", "Size on destroy: " + size);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences pref = getSharedPreferences(SPreferences, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        int size = pref.getInt("size", 0);
        int test=0;
        for(int i=0;i<nuris.size();i++) {
            editor.putString("" + (size + i), nuris.get(i).toString());
        }
            size += nuris.size();
        editor.putInt("size", size);
        editor.commit();
        Log.d("URI on save","Saved Path: " + pref.getString("" + test, "nothing here"));
        Log.d("URI", "Size on destroy: " + size);
        nuris.clear();
        startService(serviceIntent);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PIC_SELECT_REQUEST && resultCode==RESULT_OK)
        {
            Uri uri = data.getData();
            Bitmap image = null;
            try {
                this.grantUriPermission(this.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                //getContentResolver().takePersistableUriPermission(uri, takeFlags);
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            uris.add(uri);
            nuris.add(uri);
            bitmaps.add(image);
            iladapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
            Intent i = new Intent(MainActivity.this,ChangeWallpaper.class);
            startService(i);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void load(){
        SharedPreferences pref = getSharedPreferences(SPreferences, MODE_PRIVATE);
        Log.d("URI",pref.getString("0","0 isnt there!"));
        int size = pref.getInt("size",0);
        Log.d("URI", "Size: " + size);

        for(int i=0;i<size;i++)
        {
            String s = ""+i;
            Log.d("URI", "Path: " + pref.getString("s", null));
            Uri uri = Uri.parse(pref.getString(s, null));
            Log.d("URI", uri.getPath());
            uris.add(uri);
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(),uris.get(i));
                bitmaps.add(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

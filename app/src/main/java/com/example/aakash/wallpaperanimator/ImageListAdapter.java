package com.example.aakash.wallpaperanimator;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Aakash on 7/8/2015.
 */
public class ImageListAdapter extends BaseAdapter {

    private ArrayList<Bitmap> bitmaps;
    private Context c;
    public ImageListAdapter(ArrayList<Bitmap> bitmaps, Context c) {
        this.bitmaps = bitmaps;
        this.c = c;
    }

    @Override
    public int getCount() {
        return bitmaps.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.image_item,parent,false);
        ImageView imageView = (ImageView)v.findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmaps.get(position));
        return v;
    }
}

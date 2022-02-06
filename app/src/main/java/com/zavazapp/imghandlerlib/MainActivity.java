package com.zavazapp.imghandlerlib;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;
import com.zavazapp.imagehandlerlibrary.ZZImageHandler;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ZZImageHandler ih;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Picasso.get().load("https://kontos.co.me/wp-content/uploads/2016/11/Nascite-trudnicko-1024x717.jpg").resize(150, 150).into(imageView);
        ih = new ZZImageHandler(MainActivity.this);

    }

    public void startCapture(View view) {
        ih.startImageChooser();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ih.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Uri uri = ih.getImageUri(requestCode, resultCode, data);
            imageView.setImageURI(uri);
        } catch (IOException e) {
            e.printStackTrace();
            ih.startImageChooser();
        }
    }
}
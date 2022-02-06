package com.zavazapp.imghandlerlib;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lyrebirdstudio.croppylib.Croppy;
import com.lyrebirdstudio.croppylib.main.CropRequest;
import com.lyrebirdstudio.croppylib.main.CroppyActivity;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.zavazapp.imagehandlerlibrary.ImageHandler;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ImageHandler ih;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        Picasso.get().load("https://kontos.co.me/wp-content/uploads/2016/11/Nascite-trudnicko-1024x717.jpg").resize(150, 150).into(imageView);
        ih = new ImageHandler(MainActivity.this);

    }

    public void startCapture(View view) {
        ih.getPicture();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ih.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }else {


//                imageView.setImageURI(ih.onActivityResult(requestCode, resultCode, data, imageView));

            try {
                CropImage.activity(ih.onActivityResult(requestCode, resultCode, data, imageView)).start(MainActivity.this);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
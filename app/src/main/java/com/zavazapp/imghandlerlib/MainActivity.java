package com.zavazapp.imghandlerlib;

import static com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.zavazapp.imagehandlerlibrary.CircleTransform;
import com.zavazapp.imagehandlerlibrary.ImageHandler;
import com.zavazapp.imagehandlerlibrary.RadiusRectangleTransform;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

    }

    public void startCapture(View view) {
        ImageHandler ih = new ImageHandler(MainActivity.this, 111);
        ih.getPicture();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null) {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri capturedImageUri = result.getUri();
                //do whatever with image Uri.
                //Set image view for example.
                Picasso.get().load(capturedImageUri).transform(new RadiusRectangleTransform(4)).into(imageView);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCapture(null);
        }
    }


}
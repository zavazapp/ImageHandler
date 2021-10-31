package com.zavazapp.imagehandlerlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ImageHandler {
    private Activity activity;
    public int permissionsRequestCode;

    public ImageHandler(Activity activity, int permissionsRequestCode) {
        this.activity = activity;
        this.permissionsRequestCode = permissionsRequestCode;
    }

    /*
    request codes:
    0 - profile image
     */
    public void getPicture() {
        startCapture();
    }

    private void startCapture(){
        if (checkForPermissions(permissionsRequestCode)){
            Log.i("requestCode", String.valueOf(permissionsRequestCode));
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(activity);
        }
    }

    private boolean checkForPermissions(int PERMITION_REQUEST_CODE) {
        int permission1 = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);
        int permission3 = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        String PERMITION_REQUESTED;
        /* read/write permition */
        if (permission1 != PackageManager.PERMISSION_GRANTED) {
            PERMITION_REQUESTED = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            makeRequest(PERMITION_REQUESTED, PERMITION_REQUEST_CODE);
        }

        if (permission2 != PackageManager.PERMISSION_GRANTED) {
            PERMITION_REQUESTED = Manifest.permission.CAMERA;
            makeRequest(PERMITION_REQUESTED, PERMITION_REQUEST_CODE);
        }

        if (permission3 != PackageManager.PERMISSION_GRANTED) {
            PERMITION_REQUESTED = Manifest.permission.READ_EXTERNAL_STORAGE;
            makeRequest(PERMITION_REQUESTED, PERMITION_REQUEST_CODE);
        }
        return (permission1 + permission2 + permission3) == 0;
    }

    private void makeRequest(String PERMISSION_REQUESTED, int PERMITION_REQUEST_CODE) {
        ActivityCompat.requestPermissions(activity, new String[]{PERMISSION_REQUESTED}, PERMITION_REQUEST_CODE);
    }



}





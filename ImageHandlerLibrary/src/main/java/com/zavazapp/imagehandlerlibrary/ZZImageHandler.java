package com.zavazapp.imagehandlerlibrary;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ZZImageHandler {
    private Activity activity;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    public ZZImageHandler(Activity activity) {
        this.activity = activity;
    }

    public void startImageChooser() {
        startCapture();
    }

    private void startCapture(){
        if(checkAndRequestPermissions(activity)){
            chooseImage();
        }
    }

    private void chooseImage(){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    activity.startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activity.startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }


    private static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                    context,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT).show();
                } else if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(activity, "FlagUp Requires Access to Your Storage.", Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage();
                }
                break;
        }
    }


    public Uri getImageUri(int requestCode, int resultCode, Intent data) throws IOException {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            return getCropUri(resultCode, data);
        }else {
            startCrop(resultCode, requestCode, data);
        }
        return null;
    }

    public Bitmap getBitmap(int requestCode, int resultCode, Intent data) throws IOException {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            return getCropBitmap(resultCode, data);
        }else {
            startCrop(resultCode, requestCode, data);
        }
        return null;
    }

    private void startCrop(int resultCode, int requestCode, Intent data) throws IOException {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        CropImage.activity(saveFile((Bitmap) data.getExtras().get("data"))).start(activity);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
//                                Bitmap selectedBitmap = BitmapFactory.decodeFile(picturePath);
                                cursor.close();
                                CropImage.activity(selectedImage).start(activity);
                            }
                        }
                    }
                    break;
            }
        }
    }

    private Uri getCropUri(int resultCode, Intent data) {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            Uri resultUri = result.getUri();
            return resultUri;
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            //DO NOTHING

        }
        return null;
    }

    private Bitmap getCropBitmap(int resultCode, Intent data) throws IOException {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            return ((Bitmap) data.getExtras().get("data"));
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            //DO NOTHING

        }
        return null;
    }

    private Uri saveFile(Bitmap bitmap) throws IOException {
        File tempFilesDir = new File(activity.getCacheDir(), "Images");
        tempFilesDir.mkdir();

        FileOutputStream fileOutputStream = new FileOutputStream(tempFilesDir + "/cropImage.png/");

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        fileOutputStream.close();

        File fileToSend = new File(tempFilesDir, "cropImage.png");

        return FileProvider.getUriForFile((Context) activity, activity.getPackageName()+ ".fileprovider", fileToSend);
    }
}





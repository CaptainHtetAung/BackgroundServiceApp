package com.htetaung.backgroundapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PictureViewActivity extends AppCompatActivity implements CameraManager.PictureCapture {

    private ImageView imageView;
    private Button save2Storagebtn;
    private Bitmap bitmap;
    static final int WRITE_EXTERNAL_STORAGE = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_view);
        imageView = findViewById(R.id.image);
        save2Storagebtn = findViewById(R.id.save_to_storage);

        CameraManager mgr = new CameraManager(this);
        mgr.setPictureCapture(this);
        mgr.takePhoto();

        save2Storagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(PictureViewActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE);
                } else {
                    if (bitmap != null) {
                        new ChooserDialog().with(PictureViewActivity.this)
                                .withFilter(true, false)
                                .withStartFile(Environment.getExternalStorageDirectory().getAbsolutePath())
                                .withChosenListener(new ChooserDialog.Result() {
                                    @Override
                                    public void onChoosePath(String path, File pathFile) {
                                        Toast.makeText(PictureViewActivity.this, "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                                        String date = dateFormat.format(new Date());
                                        String filepath = pathFile.getPath() + File.separator + date + ".jpg";
                                        File pictureFile = new File(filepath);
                                        FileOutputStream fos = null;
                                        try {
                                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                            byte[] byteArray = stream.toByteArray();
                                            fos = new FileOutputStream(pictureFile);
                                            fos.write(byteArray);
                                            fos.close();
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                })
                                .build()
                                .show();
                    } else {
                        Toast.makeText(PictureViewActivity.this, "No Image Captured", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onPictureCapture(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        this.bitmap = bitmap;
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(PictureViewActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PictureViewActivity.this, permission)) {
                ActivityCompat.requestPermissions(PictureViewActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(PictureViewActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE) {
            if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                if (bitmap != null) {
                    new ChooserDialog().with(PictureViewActivity.this)
                            .withFilter(true, false)
                            .withStartFile(Environment.getExternalStorageDirectory().getAbsolutePath())
                            .withChosenListener(new ChooserDialog.Result() {
                                @Override
                                public void onChoosePath(String path, File pathFile) {
                                    Toast.makeText(PictureViewActivity.this, "FOLDER: " + path, Toast.LENGTH_SHORT).show();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                                    String date = dateFormat.format(new Date());
                                    String filepath = pathFile.getPath() + File.separator + date + ".jpg";
                                    File pictureFile = new File(filepath);
                                    FileOutputStream fos = null;
                                    try {
                                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                        byte[] byteArray = stream.toByteArray();
                                        fos = new FileOutputStream(pictureFile);
                                        fos.write(byteArray);
                                        fos.close();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            })
                            .build()
                            .show();
                } else {
                    Toast.makeText(PictureViewActivity.this, "No Image Captured", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

}


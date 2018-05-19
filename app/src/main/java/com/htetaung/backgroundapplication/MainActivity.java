package com.htetaung.backgroundapplication;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup cameraChooserRG;
    private RadioButton frontCameraRB;
    private RadioButton backCameraRB;
    private Button startServiceBtn;
    private boolean isMyServiceRunning;
    static final Integer CAMERA = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadUI();
        isMyServiceRunning=isMyServiceRunning(BackgroundService.class);
        Log.w("Is my service running",isMyServiceRunning+"SSS");
        configUI();
        cameraChooserRG.setOnCheckedChangeListener(this);
        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    askForPermission(Manifest.permission.CAMERA,CAMERA);
                }else {
                    if(!isMyServiceRunning){
                        startService(new Intent(MainActivity.this,BackgroundService.class));
                        startServiceBtn.setText("Stop Service");
                    }else {
                        startServiceBtn.setText(R.string.start_service);
                        stopService(new Intent(MainActivity.this,BackgroundService.class));
                    }
                }

            }
        });
    }



    /**
     * use when front camera is selected in radio button
     */
    private void onFrontCameraSelected(){
        Util.frontCameraActivate(this);
    }
    /**
     * use when back camera is selected in radio button
     */
    private void onBackCameraSelected(){
        Util.backCameraActivate(this);
    }


    /**
     * loading UI Components
     * that will be called in OnCreate
     */
    private void loadUI(){
        cameraChooserRG=findViewById(R.id.camera_chooser);
        frontCameraRB=findViewById(R.id.front_camera);
        backCameraRB=findViewById(R.id.back_camera);
        startServiceBtn=findViewById(R.id.start_service);
    }

    private void configUI(){
        if(Util.isBackCamera(this)){
            cameraChooserRG.check(backCameraRB.getId());
        }else {
            cameraChooserRG.check(frontCameraRB.getId());
        }
        if(isMyServiceRunning){
            startServiceBtn.setText("Stop Service");
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==frontCameraRB.getId()){
            onFrontCameraSelected();
        }else if(checkedId==backCameraRB.getId()){
            onBackCameraSelected();
        }else {
            Log.w("Warning","Unsupported Camera");
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){
            if(!isMyServiceRunning){
                startService(new Intent(MainActivity.this,BackgroundService.class));
                startServiceBtn.setText("Stop Service");
            }else {
                startServiceBtn.setText(R.string.start_service);
                stopService(new Intent(MainActivity.this,BackgroundService.class));
            }
        }
    }
}

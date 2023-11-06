package com.example.btlmp4.Permission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.btlmp4.MainActivity;
import com.example.btlmp4.R;

public class AllowPermission extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allow_permission);
        permission();
    }

    private void permission() {
        if(checkPermission()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(AllowPermission.this, MainActivity.class);
                    startActivity(intent);
                }
            }, 1000);
        }else{
            capQuyen23to29();
            capQuyen30();
            permission();
        }
    }

    private void capQuyen30() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()){
                AlertDialog.Builder builder = new AlertDialog.Builder(AllowPermission.this);
                builder.setTitle(R.string.app_permission)
                        .setMessage(R.string.app_permission_mes)
                        .setPositiveButton(R.string.app_permission_btn,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        try {
                                            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                            intent.addCategory("android.intent.category.DEFAULT");
                                            intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                                            startActivityIfNeeded(intent, 102);
                                        }catch (Exception e){
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                            startActivityIfNeeded(intent, 102);
                                        }
                                    }
                                }).create().show();
            }
        }
    }

    private void capQuyen23to29() {
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(AllowPermission.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);
            ActivityCompat.requestPermissions(AllowPermission.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    101);
        }
    }

    private boolean checkPermission() {
        if(ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            return  false;
        }
        if(ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }
}
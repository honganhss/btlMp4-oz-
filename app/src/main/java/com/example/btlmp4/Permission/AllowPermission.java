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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.btlmp4.MainActivity;
import com.example.btlmp4.R;

public class AllowPermission extends AppCompatActivity {
    Button btnCapQuyen, btnBatDau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allow_permission);
        btnBatDau = findViewById(R.id.button);
        btnCapQuyen = findViewById(R.id.button2);
        btnCapQuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoCapQuyen();
            }
        });
        btnBatDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllowPermission.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void MoCapQuyen(){
        try {
            // Tạo intent mở màn hình cài đặt quyền truy cập
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

}
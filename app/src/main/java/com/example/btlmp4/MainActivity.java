package com.example.btlmp4;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btlmp4.Folder.MediaFile;
import com.example.btlmp4.Folder.VdAdapter;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MediaFile> mediaFiles = new ArrayList<>();
    private ArrayList<String> allFolderList = new ArrayList<>();
    RecyclerView recyclerView;
    VdAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.folder_rv);
        showFolder();
    }

    private void showFolder() {
        mediaFiles = fetchMedia(); 
        adapter = new VdAdapter(mediaFiles, allFolderList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));
        adapter.notifyDataSetChanged();
    }

    //Lấy danh sách các đối tượng MediaFile
    // Bằng cách dùng MediaStore để lấy tệp media từ thiết bị
    private ArrayList<MediaFile> fetchMedia() {
        ArrayList<MediaFile> mediaFileArrayList = new ArrayList<>();

        //Tạo truy vấn vào MediaStore để lấy thông tin về các tệp phương tiện
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            //Tạo con trỏ
        Cursor cursor = getContentResolver().query(uri, null, null,
                null, null);
        if (cursor != null && cursor.moveToNext()){
            do{
                @SuppressLint("Range")
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                @SuppressLint("Range")
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                @SuppressLint("Range")
                String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                @SuppressLint("Range")
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                @SuppressLint("Range")
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                @SuppressLint("Range")
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                @SuppressLint("Range")
                String dateAdded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));

                //Sau khi lấy thông tin, tạo đối tượng MediaFile
                MediaFile mediaFile = new MediaFile(id, title, displayName,
                        size, duration, path, dateAdded);

                    //lấy vị trí của dấu gạch chéo cuối cùng (/) trong chuỗi path.
                int index = path.lastIndexOf("/");
                    //trích xuất từ vị trí 0 đến vị trí trước dấu gạch chéo cuối cùng (/).
                String subString = path.substring(0, index);

                //Kiểm tra xem đường dẫn đến thư mục đã tồn tại chưa
                //Nếu chưa thì thêm vào List
                if (!allFolderList.contains(subString)){
                    allFolderList.add(subString);
                }

                //Thêm thông tin video đã đọc vào danh sách video của thư mục
                mediaFileArrayList.add(mediaFile);
            }while(cursor.moveToNext());
        }
        return  mediaFileArrayList;
    }

}
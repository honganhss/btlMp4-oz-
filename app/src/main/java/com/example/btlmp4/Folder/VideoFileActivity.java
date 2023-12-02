package com.example.btlmp4.Folder;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.btlmp4.R;

import java.util.ArrayList;

public class VideoFileActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String MY_PREF = "my_prep";
    RecyclerView recyclerView;
    private ArrayList<MediaFile> videoFileArrayList = new ArrayList<>();
    private ArrayList<String> allFolderList = new ArrayList<>();
    static FileVdAdapter adapter;
    String folder_name;
    SwipeRefreshLayout swipeRefreshLayout;

    String sortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_file);

        folder_name = getIntent().getStringExtra("folderName");
        getSupportActionBar().setTitle(folder_name);

        recyclerView = findViewById(R.id.videos_rv);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_vd);
        showVideoFile();
        swipe();
    }
    private void swipe(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showVideoFile();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showVideoFile() {
        videoFileArrayList = fetchMedia(folder_name);
        adapter = new FileVdAdapter(videoFileArrayList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));
        adapter.notifyDataSetChanged();

    }

    private ArrayList<MediaFile> fetchMedia(String folderName) {
        SharedPreferences preferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        String sort_value = preferences.getString("sort", "abcd");

        ArrayList<MediaFile> videoFiles = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        if (sort_value.equals("XếpTheoTen")) {
            sortOrder = MediaStore.MediaColumns.DISPLAY_NAME +" ASC";
        }else if(sort_value.equals("XếpTheoKíchThước")) {
            sortOrder = MediaStore.MediaColumns.SIZE +" DESC";
        }else if(sort_value.equals("XếpTheoNgày")) {
            sortOrder = MediaStore.MediaColumns.DATE_ADDED +" DESC";
        }else{
            sortOrder = MediaStore.MediaColumns.DURATION +" DESC";
        }
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArg = new String[]{"%"+ folderName + "%"};
        Cursor cursor = getContentResolver().query(uri, null,
                selection, selectionArg, sortOrder);
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
                MediaFile mediaFile = new MediaFile(id, title, displayName,
                        size, duration, path, dateAdded);
                videoFiles.add(mediaFile);

            }while (cursor.moveToNext());
        }
        return videoFiles;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_vd);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SharedPreferences preferences = getSharedPreferences(MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        int id = item.getItemId();
        if(id == R.id.refresh_file){
            finish();
            startActivity(getIntent());
        }
        if(id == R.id.sort_by){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Sắp Xếp theo: ");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.apply();
                    finish();
                    startActivity(getIntent());
                    dialog.dismiss();
                }
            });
            String[] items = {"Tên (Từ A đến Z)", "Kích thước (Từ bé đến lớn)", "Ngày (Từ mới đến cũ)",
                "Độ dài(Từ lớn đến bé)"};
            alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case 0:
                            editor.putString("sort", "XếpTheoTen");
                            break;
                        case 1:
                            editor.putString("sort", "XếpTheoKíchThước");
                            break;
                        case 2:
                            editor.putString("sort", "XếpTheoNgày");
                            break;
                        case 3:
                            editor.putString("sort", "XếpTheoĐộDài");
                            break;
                    }
                }
            });
            alertDialog.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String inputs = newText.toLowerCase();
        ArrayList<MediaFile> mediaFiles = new ArrayList<>();
        for (MediaFile media:videoFileArrayList){
            if(media.getTitle().toLowerCase().contains(inputs)) {
                mediaFiles.add(media);
            }
        }
        VideoFileActivity.adapter.updateVideoFile(mediaFiles);
        return true;
    }
}
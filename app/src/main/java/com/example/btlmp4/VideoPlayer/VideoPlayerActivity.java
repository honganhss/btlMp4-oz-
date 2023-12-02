package com.example.btlmp4.VideoPlayer;

import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.btlmp4.Folder.MediaFile;
import com.example.btlmp4.R;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<MediaFile> mVideoFiles = new ArrayList<>();
    ExoPlayer playerV;
    StyledPlayerView playerView;
    int position;
    String videoTitle;
    ConcatenatingMediaSource concatenatingMediaSource;
    ImageView nextButton, previousButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        playerView = findViewById(R.id.playerView);
        getIntentAct();
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        playVideo();
    }

    private void playVideo() {
        String path = mVideoFiles.get(position).getPath();
        Uri uri = Uri.parse(path);
        playerV = new ExoPlayer.Builder(VideoPlayerActivity.this).build();

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                this, Util.getUserAgent(this, "app"));
        concatenatingMediaSource = new ConcatenatingMediaSource();
        for (int i = 0; i < mVideoFiles.size(); i++) {
            new File(String.valueOf(mVideoFiles.get(i)));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(Uri.parse(String.valueOf(uri))));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        playerView.setPlayer(playerV);
        playerView.setKeepScreenOn(true);
        playerV.prepare(concatenatingMediaSource);
        playerV.seekTo(position, C.TIME_UNSET);
        playerV.setPlayWhenReady(true);
    }


    public void getIntentAct(){
        position = getIntent().getIntExtra("position", 1);
        videoTitle = getIntent().getStringExtra("video_title");
        mVideoFiles = getIntent().getExtras().getParcelableArrayList("videoArrayList");
        nextButton = findViewById(com.google.android.exoplayer2.R.id.exo_next);
        previousButton = findViewById(com.google.android.exoplayer2.R.id.exo_prev);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == com.google.android.exoplayer2.R.id.exo_next){
            try {
                playerV.stop();
                position++;
                playVideo();
            }catch (Exception e){
                Toast.makeText(this, "Không còn video tiếp", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if(v.getId( )== com.google.android.exoplayer2.ui.R.id.exo_prev){
            try {
                playerV.stop();
                position--;
                playVideo();
            }catch (Exception e){
                Toast.makeText(this, "Không còn video trước", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

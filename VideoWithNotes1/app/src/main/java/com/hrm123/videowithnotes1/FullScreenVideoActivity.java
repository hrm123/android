package com.hrm123.videowithnotes1;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class FullScreenVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_videoview);

        videoView = findViewById(R.id.videoView);

        String vurl =  getIntent().getStringExtra("vurl");
        String fullScreen =  getIntent().getStringExtra("fullScreen");
        /*
        if("y".equals(fullScreen)){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        }

         */

        Uri videoUri = Uri.parse(vurl);

        videoView.setVideoURI(videoUri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.start();
            }
        });

        if(isLandScape()){
            mediaController = new FullScreenMediaController(this);
        }else {
            mediaController = new MediaController(this);
        }
        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        // videoView.start();
    }



    private boolean isLandScape(){
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();
        int rotation = display.getRotation();

        if (rotation == Surface.ROTATION_90
                || rotation == Surface.ROTATION_270) {
            return true;
        }
        return false;
    }
}
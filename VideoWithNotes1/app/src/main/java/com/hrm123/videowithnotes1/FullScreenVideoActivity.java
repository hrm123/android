package com.hrm123.videowithnotes1;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import androidx.appcompat.app.AppCompatActivity;

public class FullScreenVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;


    protected String getFile(String... f_url) {
        int count;
        try {
            String root = Environment.getExternalStorageDirectory().toString();

            System.out.println("Downloading");
            URL url = new URL(f_url[0]);

            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length
            int lenghtOfFile = connection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file

            OutputStream output = new FileOutputStream(root+"/1.mp4");
            byte data[] = new byte[1024];

            long total = 0;
            while ((count = input.read(data)) != -1) {
                total += count;

                // writing data to file
                output.write(data, 0, count);

            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            return root+"/1.mp4";
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            return "";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_videoview);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        videoView = findViewById(R.id.videoView);

        String vurl =  getIntent().getStringExtra("vurl");
        String fullScreen =  getIntent().getStringExtra("fullScreen");
        vurl = getFile(vurl);
        /*
        if("y".equals(fullScreen)){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        }

         */

        // vurl = "https://imdb-video.media-imdb.com/vi3874602777/1434659607842-pgv4ql-1570801130330.mp4?Expires=1575807928&Signature=kat8CHmRBTA~Rn0xXDZVWrnr7cJAk2OILjpybh4GEAn7tqyAEFaQhKfT4a6Qztq8GResBLGXa08KGVjiIH1~o6QMLwz9ACwNhXNDC8AVTgooIRv~qX6Olt33RYbf7xLh9z44~OU7wEOHiYfOOBvXIH7m-5BeCzVoq35R~D~mSEGuB7WRecnEPjOkvX5VAPn9VxWgPsmxDFpOyxp-IsjM1VSLY7GYcHjNyTnMkhAR8k5hvPcCvbv3i3vyktUMfwzyyvkxDvv9fmIkYXZhqPk1VKihcFlui9BWlLW9DbnfvJyszd8AEXZgrrP4YAIRYiJDTa5A-Rf~17DWm8bNFHcb8A__&Key-Pair-Id=APKAIFLZBVQZ24NQH3KA";
        Uri videoUri = Uri.parse(vurl);



        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(false);
                //mp.setDataSource(vurl);
                videoView.start();
                mediaController.show(0);
            }
        });

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return false;
            }
        });

        if(isLandScape()){
            mediaController = new FullScreenMediaController(this);
        }else {
            mediaController = new MediaController(this);
        }
        mediaController.setAnchorView(videoView);
        mediaController.hide();

        videoView.setMediaController(mediaController);
        //videoView.start();
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
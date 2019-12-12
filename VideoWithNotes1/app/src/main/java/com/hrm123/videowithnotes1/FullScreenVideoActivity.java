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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;

import com.google.protobuf.ByteString;
import com.hrm123.nextgenvideosvc.Chunk;
import com.hrm123.nextgenvideosvc.FileListReq;
import com.hrm123.nextgenvideosvc.FileReq;
import com.hrm123.nextgenvideosvc.FileListResp;
import com.hrm123.nextgenvideosvc.FileName;
import com.hrm123.nextgenvideosvc.NextGenVideoServiceGrpc;
import com.hrm123.nextgenvideosvc.SvcResponse;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

public class FullScreenVideoActivity extends AppCompatActivity {

    private VideoView videoView;
    private MediaController mediaController;


    protected String getFile(String... f_url) {
        int count;
        try {
            if(f_url[0].indexOf("amazonaws.com") <=0 && f_url[0].indexOf(".168.com") <=0){
                return f_url[0]; //no need to download bcos it is already local
            }

            String root = Environment.getExternalStorageDirectory().toString();
            System.out.println("Downloading");
            URL url = new URL(f_url[0]);


            String fileName =
                    f_url[0].replace(
                            "http://ec2-3-135-87-107.us-east-2.compute.amazonaws.com/",
            "");
            String fullFileName = root + "/" + fileName;
            try {
                File fl = new File(fullFileName);
                if (fl.exists()) {
                    return fullFileName; // no need to download it again
                }
            }
            catch(Exception ex){
                Log.i("Info", "file " + fileName + " is being downloaded");

            }


            GetRecFromCloud(fileName,fullFileName );
            return fullFileName;
            /*
            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length
            int lenghtOfFile = connection.getContentLength();

            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream to write file

            OutputStream output = new FileOutputStream(fullFileName);
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

            return fullFileName;

             */
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            return "";
        }
    }


    private void GetRecFromCloud(String fileName, String localPath){

        // videoPath ->   /storage/emulated/0/Pictures/Sceneform/Sample16ec31c13c1.mp4

        /*
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                "172.17.198.241", 33333 )
                .usePlaintext()
                .build();
        */

        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                //"192.168.1.39", 33333 )
                "3.134.87.107", 33333)
                .usePlaintext()
                .build();
        final CountDownLatch finishLatch = new CountDownLatch(1);
        String status = "processing";
        // NextGenVideoSvcGrpc.NextGenVideoServiceStub stub = NextGenVideoSvcGrpc.newStub(channel);
        NextGenVideoServiceGrpc.NextGenVideoServiceStub stub = NextGenVideoServiceGrpc.newStub(channel);
        FileReq.Builder reqBuilder = FileReq.newBuilder();

        try {
        final BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(localPath));



        stub.getFile(reqBuilder.setFullfilename(fileName).build(), new ServerCallStreamObserver<Chunk>() {
            int totalBytes = 0;

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public void setOnCancelHandler(Runnable onCancelHandler) {

            }

            @Override
            public void setCompression(String compression) {

            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setOnReadyHandler(Runnable onReadyHandler) {

            }

            @Override
            public void disableAutoInboundFlowControl() {

            }

            @Override
            public void request(int count) {

            }

            @Override
            public void setMessageCompression(boolean enable) {

            }

            @Override
            public void onNext(Chunk value) {
                byte[] buffer = value.getPayLoad().toByteArray();
                totalBytes += buffer.length;
                try {
                    out.write(buffer);
                }
                catch(Exception ex){
                    //ignore
                    int i = 0;
                }

            }

            @Override
            public void onError(Throwable t) {
                try{
                    out.flush();
                    out.close();
                    finishLatch.countDown();
                }
                catch(Exception ex){
                    //ignore
                    int i = 0;
                }
            }

            @Override
            public void onCompleted() {
                try{
                out.flush();
                out.close();
                    finishLatch.countDown();
                }
                catch(Exception ex){
                    //ignore
                    int i = 0;
                }
            }
        });
            finishLatch.await(2, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
            return;
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
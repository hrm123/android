package com.hrm123.videowithnotes;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.CamcorderProfile;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.TransformableNode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class VideoRecordingActivity extends AppCompatActivity
        implements ModelLoader.ModelLoaderCallbacks {
    private static final String TAG = VideoRecordingActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private Node infoCard;
    private WritingArFragment arFragment;
    private ModelRenderable andyRenderable;
    // Model loader class to avoid leaking the activity context.
    private ModelLoader modelLoader;

    // VideoRecorder encapsulates all the video recording functionality.
    private VideoRecorder videoRecorder;

    // The UI to record.
    private FloatingActionButton recordButton;

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted1");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    // downloadPdfFile();
                }else{
                   // progress.dismiss();
                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    // SharePdfFile();
                }else{
                    // progress.dismiss();
                }
                break;
        }
    }

    private String m_Text = "";

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }

        if(!isReadStoragePermissionGranted()){
            return;
        }
        if(!isWriteStoragePermissionGranted()){
            return;
        }

        setContentView(R.layout.activity_ux);
        arFragment = (WritingArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);


        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    final EditText input = new EditText(this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Enter name")
                            // .setMessage("Are you sure you want to delete this entry?")
                            .setView(input)
                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    m_Text = input.getText().toString();
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    infoCard = new Node();
                    //infoCard.isTopLevel();
                    infoCard.setParent(anchorNode);
                    infoCard.setEnabled(true);
                    infoCard.setLocalPosition (new Vector3(0f,0f,-1f));
                            /*
                    infoCard.setLocalPosition(new Vector3(hitResult.getHitPose().tx(),
                            hitResult.getHitPose().ty(),
                            hitResult.getHitPose().tz()));

                             */
                    ViewRenderable.builder()
                            .setView(this, R.layout.name_info_view)
                            .build()
                            .thenAccept(
                                    (renderable) -> {
                                        infoCard.setRenderable(renderable);
                                        TextView textView = (TextView) renderable.getView();
                                        textView.setText("rammohan holagundi");
                                        textView.setTextColor(ColorStateList.valueOf(Color.MAGENTA));

                                    })
                            .exceptionally(
                                    (throwable) -> {
                                        throw new AssertionError("Could not load plane card view.", throwable);
                                    });
                });

        // Initialize the VideoRecorder.
        videoRecorder = new VideoRecorder();
        int orientation = getResources().getConfiguration().orientation;
        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_2160P, orientation);
        videoRecorder.setSceneView(arFragment.getArSceneView());

        recordButton = findViewById(R.id.record);
        recordButton.setOnClickListener(this::toggleRecording);
        recordButton.setEnabled(true);
        recordButton.setImageResource(R.drawable.round_videocam);
    }

    @Override
    protected void onPause() {
        if (videoRecorder.isRecording()) {
            toggleRecording(null);
        }
        super.onPause();
    }

    /*
     * Used as a handler for onClick, so the signature must match onClickListener.
     */
    private void toggleRecording(View unusedView) {
        if (!arFragment.hasWritePermission()) {
            Log.e(TAG, "Video recording requires the WRITE_EXTERNAL_STORAGE permission");
            Toast.makeText(
                    this,
                    "Video recording requires the WRITE_EXTERNAL_STORAGE permission",
                    Toast.LENGTH_LONG)
                    .show();
            arFragment.launchPermissionSettings();
            return;
        }
        boolean recording = videoRecorder.onToggleRecord();
        if (recording) {
            recordButton.setImageResource(R.drawable.round_stop);
        } else {
            recordButton.setImageResource(R.drawable.round_videocam);
            String videoPath = videoRecorder.getVideoPath().getAbsolutePath();
            Toast.makeText(this, "Video saved: " + videoPath, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Video saved: " + videoPath);

            // Send  notification of updated content.
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, "Sceneform Video");
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            values.put(MediaStore.Video.Media.DATA, videoPath);
            getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
        }
    }

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     *
     * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     *
     * <p>Finishes the activity if Sceneform can not run
     */
    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }



    @Override
    public void setRenderable(ModelRenderable modelRenderable) {
        andyRenderable = modelRenderable;
    }

    @Override
    public void onLoadException(Throwable throwable) {
        Toast toast = Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Log.e(TAG, "Unable to load andy renderable", throwable);
    }
}

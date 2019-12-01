package com.hrm123.videowithnotes1.ui.home;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.CamcorderProfile;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.ar.core.Anchor;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.protobuf.ByteString;
import com.hrm123.videowithnotes1.Chunk;
import com.hrm123.videowithnotes1.ModelLoader;
import com.hrm123.videowithnotes1.NextGenVideoSvc;
import com.hrm123.videowithnotes1.NextGenVideoSvcGrpc;
import com.hrm123.videowithnotes1.R;
import com.hrm123.videowithnotes1.SvcResponse;
import com.hrm123.videowithnotes1.VideoRecorder;
import com.hrm123.videowithnotes1.WritingArFragment;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment
        implements ModelLoader.ModelLoaderCallbacks{

    private HomeViewModel homeViewModel;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private Node infoCard;
    private WritingArFragment arFragment;
    // Model loader class to avoid leaking the activity context.
    private ModelLoader modelLoader;

    // VideoRecorder encapsulates all the video recording functionality.
    private VideoRecorder videoRecorder;

    // The UI to record.
    private FloatingActionButton recordButton;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        if (!checkIsSupportedDeviceOrFinish(getActivity())) {
            return root;
        }

        if(!isReadStoragePermissionGranted()){
            return root;
        }
        if(!isWriteStoragePermissionGranted()){
            return root;
        }
        /*
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */
        arFragment = (WritingArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);
        Scene scene = arFragment.getArSceneView().getScene();
        scene.setOnTouchListener(null);
        scene.setOnTouchListener(new Scene.OnTouchListener() {
            @Override
            public boolean onSceneTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                handleOnTouchV0(hitTestResult, motionEvent);
                return true; // return false means new touch events are not generated
            }
        });
        // Initialize the VideoRecorder.
        videoRecorder = new VideoRecorder();
        int orientation = getResources().getConfiguration().orientation;
        videoRecorder.setVideoQuality(CamcorderProfile.QUALITY_2160P, orientation);
        videoRecorder.setSceneView(arFragment.getArSceneView());

        recordButton = root.findViewById(R.id.record);
        recordButton.setOnClickListener(this::toggleRecording);
        recordButton.setEnabled(true);
        recordButton.setImageResource(R.drawable.round_videocam);
        return root;
    }

    @Override
    public void setRenderable(ModelRenderable modelRenderable) {
        // andyRenderable = modelRenderable;
    }

    @Override
    public void onLoadException(Throwable throwable) {
        Toast toast = Toast.makeText(getContext(), "Unable to load andy renderable", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Log.e(TAG, "Unable to load andy renderable", throwable);
    }
    /*
     * Used as a handler for onClick, so the signature must match onClickListener.
     */
    private void toggleRecording(View unusedView) {
        if (!arFragment.hasWritePermission()) {
            Log.e(TAG, "Video recording requires the WRITE_EXTERNAL_STORAGE permission");
            Toast.makeText(
                    getActivity(),
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
            Toast.makeText(getActivity(), "Video saved: " + videoPath, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Video saved: " + videoPath);

            // Send  notification of updated content.
            ContentValues values = new ContentValues();
            values.put(MediaStore.Video.Media.TITLE, "Sceneform Video");
            values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
            values.put(MediaStore.Video.Media.DATA, videoPath);
            getContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);

            // save to cloud
            SaveRecToCloud(videoPath);


        }
    }

    private void SaveRecToCloud(String videoPath){
        ManagedChannel channel = ManagedChannelBuilder.forAddress(
                "3.134.87.107", 33333 )
                .usePlaintext()
                .build();
        String status = "processing";
        NextGenVideoSvcGrpc.NextGenVideoServiceStub stub = NextGenVideoSvcGrpc.newStub(channel);
        StreamObserver<Chunk> resp = stub.saveMp4File(new StreamObserver<SvcResponse>() {
            @Override
            public void onNext(SvcResponse value) {

            }

            @Override
            public void onError(Throwable t) {
                String status = "error";
            }

            @Override
            public void onCompleted() {
                String status = "completed";
            }
        });

        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(videoPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        byte[] buffer = new byte[1024];
        Chunk.Builder chunkBuilder = Chunk.newBuilder();
        int tmp = 0;
        int totalBytes = 0;
        try {
            int n = 0;
            while ((tmp = in.read(buffer, 0, 1024)) > 0) {
                /* do whatever you want with buffer here */
                totalBytes += tmp;
                ByteString byteString = ByteString.copyFrom(buffer, 0, tmp);
                // File m = builder.setBinary(byteString).build();

                resp.onNext(chunkBuilder.setPayLoad(byteString).build());
            }
            resp.onCompleted();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally { // always close input stream
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handleOnTouchV0(HitTestResult hitTestResult, MotionEvent motionEvent) {
        Log.d(TAG, "handleOnTouch");

        if (motionEvent.getAction()  == MotionEvent.ACTION_UP
                && motionEvent.getPointerCount() == 1
        ) {
            long eventTime = motionEvent.getEventTime() - motionEvent.getDownTime();
            if(eventTime < 100) { // if press was for less than 1 sec
                return;
            } else{
                int i=0;
            }
        } else{
            return;
        }
        // First call ArFragment's listener to handle TransformableNodes.
        arFragment.onPeekTouch(hitTestResult, motionEvent);
        Frame frame = arFragment.getArSceneView().getArFrame();
        Session session = arFragment.getArSceneView().getSession();

        session.getConfig().setPlaneFindingMode(Config.PlaneFindingMode.DISABLED); // no need to find planes since we write nameplate in spsecific position in air
        /*
        //works kind of okay .. trying better solutions
        Anchor anchor = session.createAnchor(
                frame.getCamera().getDisplayOrientedPose());//works even better
        Anchor anchor = session.createAnchor(
                frame.getCamera().getDisplayOrientedPose()
                        .compose(Pose.makeTranslation(0, 0, -0.3f))
                );
         */

        Anchor anchor = session.createAnchor(
                frame.getCamera().getDisplayOrientedPose()
                        .compose(Pose.makeTranslation(0, -0.2f, -0.3f))
        );
        //              .extractTranslation());

        Log.d(TAG, "act = " + motionEvent.getAction());
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter name")
                // .setMessage("Are you sure you want to delete this entry?")
                .setView(input)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        renderInfoCard(m_Text, anchor);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }


    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
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

    private void renderInfoCard(String txt, Anchor anchor){

        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());
        infoCard = new Node();
        infoCard.setName("infocard" + (new Date()).getTime());
        //infoCard.isTopLevel();
        infoCard.setParent(anchorNode);
        infoCard.setEnabled(true);
        //float[] pos = { 0,0,-1 };
        //float[] rotation = {0,0,0,1};
        // Anchor anchor =  session.createAnchor(new Pose(pos, rotation));
        infoCard.setLocalPosition(new Vector3(0f,0f,-1f));

        ViewRenderable.builder()
                .setView(getContext(), R.layout.name_info_v2)
                .build()
                .thenAccept(
                        (note) -> {
                            infoCard.setRenderable(note);
                            TextView textView =  note.getView().findViewById(R.id.nameInfoCard);


                            //textView.setText("rammohan holagundi");
                            textView.setText(txt);
                            // int alpha = 70;
                            //textView.setBackgroundColor(Color.argb(alpha, 132,154,201));
                            // textView.setTextColor(ColorStateList.valueOf(Color.MAGENTA));

                        })
                .exceptionally(
                        (throwable) -> {
                            throw new AssertionError("Could not load plane card view.", throwable);
                        });
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


    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ( getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted1");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked1");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
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
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted2");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked2");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted2");
            return true;
        }
    }
}
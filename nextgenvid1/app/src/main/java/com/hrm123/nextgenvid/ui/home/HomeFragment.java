package com.hrm123.nextgenvid.ui.home;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.hrm123.nextgenvid.ModelLoader;
import com.hrm123.nextgenvid.R;
import com.hrm123.nextgenvid.WritingArFragment;

public class HomeFragment extends Fragment
        implements ModelLoader.ModelLoaderCallbacks{
    private WritingArFragment arFragment;
    private HomeViewModel homeViewModel;
    private Node infoCard;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private ModelLoader modelLoader;
    private ModelRenderable modelRenderable;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        arFragment = (WritingArFragment) getChildFragmentManager().findFragmentById(R.id.ux_fragment);

        modelLoader = new ModelLoader(this);
        modelLoader.loadModel(getActivity(), R.raw.andy);
        if (!checkIsSupportedDeviceOrFinish(getActivity())) {
            return root;
        }
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue with delete operation
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
                            .setView(getActivity(), R.layout.name_info_view)
                            .build()
                            .thenAccept(
                                    (renderable) -> {
                                        infoCard.setRenderable(renderable);
                                        TextView textView = (TextView) renderable.getView();
                                        textView.setText("bestandroid1");
                                        textView.setTextColor(ColorStateList.valueOf(Color.MAGENTA));

                                    })
                            .exceptionally(
                                    (throwable) -> {
                                        throw new AssertionError("Could not load plane card view.", throwable);
                                    });

                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(modelRenderable);
                    andy.select();

                }
        );
        /*
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        // WritingArFragment ar1 = new WritingArFragment();
        ft.replace(R.id.ux_fragment,arFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.addToBackStack(null);
        ft.commit();
        */

        return root;
    }

    @Override
    public void setRenderable(ModelRenderable modelRenderable) {
        modelRenderable = modelRenderable;
    }

    @Override
    public void onLoadException(Throwable throwable) {
        Toast toast = Toast.makeText(getActivity(), "Unable to load model", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Log.e(TAG, "Unable to load model", throwable);
    }

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
}
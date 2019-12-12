package com.hrm123.videowithnotes1;


/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.google.ar.core.AugmentedImage;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.concurrent.CompletableFuture;

/**
 * Node for rendering an augmented image. The image is framed by placing the virtual picture frame
 * at the corners of the augmented image trackable.
 */
@SuppressWarnings({"AndroidApiChecker"})
public class AugmentedImageNode extends AnchorNode {

    private static final String TAG = "AugmentedImageNode";

    // The augmented image represented by this node.
    private AugmentedImage image;

    // Models of the 4 corners.  We use completable futures here to simplify
    // the error handling and asynchronous loading.  The loading is started with the
    // first construction of an instance, and then used when the image is set.
    private static CompletableFuture<ModelRenderable> ulCorner;
    private static CompletableFuture<ModelRenderable> urCorner;
    private static CompletableFuture<ModelRenderable> lrCorner;
    private static CompletableFuture<ModelRenderable> llCorner;
    private static CompletableFuture<ModelRenderable> jet;
    private static CompletableFuture<ModelRenderable> hab;
    private static CompletableFuture<ModelRenderable> partyBalloon;
    private static CompletableFuture<ModelRenderable> partyBalloon1;
    public AugmentedImageNode(Context context) {
        // Upon construction, start loading the models for the corners of the frame.
        if (ulCorner == null) {
            ulCorner =
                    ModelRenderable.builder()
                            .setSource(context, Uri.parse("models/frame_upper_left.sfb"))
                            .build();
            urCorner =
                    ModelRenderable.builder()
                            .setSource(context, Uri.parse("models/frame_upper_right.sfb"))
                            .build();
            llCorner =
                    ModelRenderable.builder()
                            .setSource(context, Uri.parse("models/frame_lower_left.sfb"))
                            .build();
            lrCorner =
                    ModelRenderable.builder()
                            .setSource(context, Uri.parse("models/frame_lower_right.sfb"))
                            .build();
            jet = ModelRenderable.builder()
                    .setSource(context, Uri.parse("models/CUPIC_JEt.sfb"))
                    .build();
            hab = ModelRenderable.builder()
                    .setSource(context, Uri.parse("models/Hot air balloon.sfb"))
                    .build();
            partyBalloon = ModelRenderable.builder()
                    .setSource(context, Uri.parse("models/balloon.sfb"))
                    .build();
            partyBalloon1 = ModelRenderable.builder()
                    .setSource(context, Uri.parse("models/model.sfb"))
                    .build();

        }
    }

    /**
     * Called when the AugmentedImage is detected and should be rendered. A Sceneform node tree is
     * created based on an Anchor created from the image. The corners are then positioned based on the
     * extents of the image. There is no need to worry about world coordinates since everything is
     * relative to the center of the image, which is the parent node of the corners.
     */
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    public void setImage(AugmentedImage image, WritingArFragment frag) {
        this.image = image;

        // If any of the models are not loaded, then recurse when all are loaded.
        if (!ulCorner.isDone() || !urCorner.isDone()
                || !llCorner.isDone() || !lrCorner.isDone()
                || !jet.isDone() || !hab.isDone() || !partyBalloon.isDone()
        || !partyBalloon1.isDone()) {
            CompletableFuture.allOf(ulCorner, urCorner, llCorner, lrCorner,
                    jet, hab, partyBalloon, partyBalloon1)
                    .thenAccept((Void aVoid) -> setImage(image, frag))
                    .exceptionally(
                            throwable -> {
                                Log.e(TAG, "Exception loading", throwable);
                                return null;
                            });
        }

        final float xtntx = image.getExtentX();
        final float xtntz = image.getExtentZ();

        // Set the anchor based on the center of the image.
        setAnchor(image.createAnchor(image.getCenterPose()));

        // Make the 4 corner nodes.
        Vector3 localPosition = new Vector3();
        Node cornerNode;
        final float scale =  Math.min(
                xtntx, xtntz) * 0.2f;

        Vector3 scaleVector = new Vector3(
                1,
                5,
                1
        );
        final int setScale = 1;
        Boolean useHab = false;
        // Upper left corner.
        localPosition.set(-0.5f * xtntx, 0.0f, -0.5f * xtntx);
        if(setScale == 1) {
           // cornerNode = new TransformableNode(frag.getTransformationSystem());
            SolarSettings solarSettings = new SolarSettings();
            solarSettings.setOrbitSpeedMultiplier(10f);
            solarSettings.setRotationSpeedMultiplier(2f);
            Vector3 scaleVectorAirplane = new Vector3(
                    0.01f,
                    0.01f,
                    0.01f
            );
            Vector3 scaleVectorHab = new Vector3(
                    .1f,
                    .1f,
                    .1f
            );
            Vector3 current = scaleVectorAirplane;

            if(useHab) {
                current = scaleVectorHab;
            }
            cornerNode = new RotatingNode(solarSettings,true,true,0.047f);
            ((RotatingNode)cornerNode).setDegreesPerSecond(4);
        cornerNode.setLocalScale(current);
                if(useHab) {
                    cornerNode.setLocalRotation(new Quaternion(0, 0, 0, 1));
                }

        } else{
            cornerNode = new Node();
        }
        cornerNode.setLocalPosition(localPosition);
        cornerNode.setParent(this);


        // cornerNode.setLocalScale(scaleVector);
       // cornerNode.setRenderable(ulCorner.getNow(null)); 123
        if(useHab) {
            cornerNode.setRenderable(partyBalloon.getNow(null));
        } else{
            // cornerNode.setRenderable(jet.getNow(null));
            cornerNode.setRenderable(ulCorner.getNow(null));
        }
        cornerNode.setEnabled(true);


        // Upper right corner.
        localPosition.set(0.5f * xtntx, 0.0f, -0.5f * xtntx);
        if(setScale == 1) {
            cornerNode = new TransformableNode(frag.getTransformationSystem());
            cornerNode.setLocalScale(scaleVector);
        } else{
            cornerNode = new Node();
        }

        cornerNode.setLocalPosition(localPosition);
        cornerNode.setParent(this);
        cornerNode.setRenderable(urCorner.getNow(null));
        cornerNode.setEnabled(true);



        // Lower right corner.
        localPosition.set(0.5f * xtntx, 0.0f, 0.5f * xtntx);
        if(setScale == 1) {
            cornerNode = new TransformableNode(frag.getTransformationSystem());
            cornerNode.setLocalScale(scaleVector);
        } else{
            cornerNode = new Node();
        }
        cornerNode.setLocalPosition(localPosition);
        cornerNode.setParent(this);
        cornerNode.setRenderable(lrCorner.getNow(null));
        cornerNode.setEnabled(true);


        // Lower left corner.
        localPosition.set(-0.5f * xtntx, 0.0f, 0.5f * xtntx);


        if(setScale == 1) {
            cornerNode = new TransformableNode(frag.getTransformationSystem());
            cornerNode.setLocalScale(scaleVector);
        } else{
            cornerNode = new Node();
        }
        cornerNode.setLocalPosition(localPosition);
        cornerNode.setParent(this);
        cornerNode.setRenderable(llCorner.getNow(null));
        cornerNode.setEnabled(true);
    }

    public AugmentedImage getImage() {
        return image;
    }
}

/*
 * Copyright (C) The Android Open Source Project
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
package com.google.android.gms.samples.vision.face.eyeware;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
//import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.samples.vision.face.eyeware.ui.camera.CameraSource;
import com.google.android.gms.samples.vision.face.eyeware.ui.camera.CameraSourcePreview;
import com.google.android.gms.samples.vision.face.eyeware.ui.camera.GraphicOverlay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Activity for the face tracker app.  This app detects faces with the rear facing camera, and draws
 * overlay graphics to indicate the position, size, and ID of each face.
 */
public final class EyewareActivity extends AppCompatActivity {
    private static final String TAG = "FaceTracker";

    //Information we use to keep track of the frames brought out
    private String framesName = "";
    private String framesColor = "";
    private int framesPictureID;
    private int framesPrice;

    private boolean TAKE_PHOTO_BY_SMILE_PERMISSION_GRANTED = false;
    protected int alreadyInFavesCounter = 0;
    public static final String FAVORITES_LIST = "Favorites";


    private CameraSource mCameraSource = null;

    private CameraSourcePreview mPreview;
    private GraphicOverlay mGraphicOverlay;

    private static final int RC_HANDLE_GMS = 9001;
    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    //==============================================================================================
    // Activity Methods
    //==============================================================================================

    private static Bitmap eraseBG(Bitmap src, int color) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap b = src.copy(Bitmap.Config.ARGB_8888, true);
        b.setHasAlpha(true);

        int[] pixels = new int[width * height];
        src.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < width * height; i++) {
            if (pixels[i] == color) {
                pixels[i] = 0;
            }
        }

        b.setPixels(pixels, 0, width, 0, 0, width, height);

        return b;
    }

    private void takeScreenshot() {

        mCameraSource.takePicture(null, new CameraSource.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes) {
                //Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //Log.d("BITMAP", bmp.getWidth() + "x" + bmp.getHeight());
                Log.d(TAG, "Picture Taken!");
                Date now = new Date();
                android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
                try {
                    String mainpath = Environment.getExternalStorageDirectory().toString() + "/";
                    File basePath = new File(mainpath);
                    if (!basePath.exists())
                        Log.d("CAPTURE_BASE_PATH", basePath.mkdirs() ? "Success": "Failed");
                    File captureFile = new File(mainpath + "photo_" + now + ".jpg");
                    if (!captureFile.exists())
                        Log.d("CAPTURE_FILE_PATH", captureFile.createNewFile() ? "Success": "Failed");
                    FileOutputStream stream = new FileOutputStream(captureFile);

                    //Get the graphics to overlay on the photo

                    //View v1 = mGraphicOverlay.getRootView();
                    //v1.setDrawingCacheEnabled(true);
                    Bitmap graphics = getGraphicOverlay(mGraphicOverlay);
                    //v1.setDrawingCacheEnabled(false);

                    graphics = eraseBG(graphics, -16777216);
                    Log.d(TAG, "Graphics W: " + graphics.getWidth() + ", Graphics H: " + graphics.getHeight());

                    Bitmap decoded = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    Bitmap photo = Bitmap.createScaledBitmap(decoded, graphics.getHeight(),
                            graphics.getWidth(), true);

                    Bitmap test = overlay(photo, graphics);

                    test.compress(Bitmap.CompressFormat.JPEG, 90, stream);

                    //stream.write(bytes);
                    stream.flush();
                    stream.close();
                    //openScreenshot(captureFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //savePicture(bytes);
            }
        });
    }

    public Bitmap getGraphicOverlay(GraphicOverlay mGraphicOverlay) {
        Bitmap bmp = Bitmap.createBitmap(mGraphicOverlay.getWidth(),
                mGraphicOverlay.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        mGraphicOverlay.layout(mGraphicOverlay.getLeft(), mGraphicOverlay.getTop(),
                mGraphicOverlay.getRight(), mGraphicOverlay.getBottom());
        mGraphicOverlay.draw(canvas);
        return bmp;
    }



    public static Bitmap overlay(Bitmap photo, Bitmap graphics) {
        Bitmap bmOverlay = Bitmap.createBitmap(graphics.getWidth(), graphics.getHeight(), photo.getConfig());
        Canvas canvas = new Canvas(bmOverlay);


        Matrix transform = new Matrix();
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        transform.setRotate(-90, 0, 0);

        canvas.setMatrix(transform);
        canvas.drawBitmap(photo, -photo.getWidth(), 0, null);

        //canvas.drawBitmap(photo, -photo.getWidth(), (int)(0.5*(graphics.getWidth() - photo.getHeight())), null);
        canvas.restore();
        transform.reset();

        //graphics = Bitmap.createScaledBitmap(graphics, canvas.getWidth(), canvas.getHeight(), false);
        //transform.setRotate(0, photo.getWidth()/2, photo.getHeight()/2);
        //canvas.scale(-1,1);

        //transform.postScale(1.35f, 1.0125f,0,0);
        transform.postScale(-1,1,canvas.getWidth()/2, canvas.getHeight()/2);

        canvas.setMatrix(transform);
        canvas.drawBitmap(graphics, 0, 0, null);
        //canvas.drawBitmap(graphics, (int)(0.5*(graphics.getWidth() - photo.getHeight())), 0, null);

        Log.d(TAG, "Graphics Width: " + graphics.getWidth() + ", Graphics Height: " + graphics.getHeight());

        return bmOverlay;
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image*//*");
        startActivity(intent);
    }



    /**
     * Initializes the UI and initiates the creation of a face detector.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        Bundle extras = getIntent().getExtras();
        framesName = extras.getString("framesName");
        framesColor = extras.getString("framesColor");
        framesPrice = extras.getInt("framesPrice");
        framesPictureID = extras.getInt("selectedFrames");

        SharedPreferences favorites = getSharedPreferences(FAVORITES_LIST, MODE_PRIVATE);



        TextView nameText = (TextView) findViewById(R.id.txtFrameName);
        TextView colorText = (TextView) findViewById(R.id.txtFrameColor);
        TextView priceText = (TextView) findViewById(R.id.txtFramePrice);

        mPreview = (CameraSourcePreview) findViewById(R.id.preview);
        mGraphicOverlay = (GraphicOverlay) findViewById(R.id.faceOverlay);

        Button checkOutButton = (Button)findViewById(R.id.checkOutButton);
        checkOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PurchaseActivity.class);
                i.putExtra("framesName", framesName);
                i.putExtra("framesPictureID", framesPictureID);
                i.putExtra("framesPrice", framesPrice);
                i.putExtra("framesColor", framesColor);
                startActivity(i);
            }
        });





        // Check for the camera permission before accessing the camera.  If the
        // permission is not granted yet, request permission.
        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (rc == PackageManager.PERMISSION_GRANTED) {
            createCameraSource();
        } else {
            requestCameraPermission();
        }
        int rrw = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (rrw != PackageManager.PERMISSION_GRANTED) {
            requestReadWritePermission();
        }

        nameText.setText(framesName);
        colorText.setText(framesColor);
        priceText.setText("$" + framesPrice + ".00");

        registerTapCallback();
    }

    private void registerTapCallback() {
        final CheckBox checkbox = (CheckBox)findViewById(R.id.photoCheckBox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TAKE_PHOTO_BY_SMILE_PERMISSION_GRANTED = isChecked;

            }
        });
    }


    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */


    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{
                Manifest.permission.CAMERA
        };

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    private void requestReadWritePermission() {
        Log.w(TAG, "Read/Write permissions not granted. Requesting permission");

        final String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, permissions, 1);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        1);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_storage_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    /**
     * Creates and starts the camera.  Note that this uses a higher resolution in comparison
     * to other detection examples to enable the barcode detector to detect small barcodes
     * at long distances.
     */
    private void createCameraSource() {

        Context context = getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        if (!detector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(480, 640)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(60.0f)
                .build();
    }

    /**
     * Restarts the camera.
     */
    @Override
    protected void onResume() {
        super.onResume();

        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    /**
     * Callback for the result from requesting permissions. This method
     * is invoked for every call on {@link #requestPermissions(String[], int)}.
     * <p>
     * <strong>Note:</strong> It is possible that the permissions request interaction
     * with the user is interrupted. In this case you will receive empty permissions
     * and results arrays which should be treated as a cancellation.
     * </p>
     *
     * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either {@link PackageManager#PERMISSION_GRANTED}
     *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
     * @see #requestPermissions(String[], int)
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != RC_HANDLE_CAMERA_PERM) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source");
            // we have permission, so create the camerasource
            createCameraSource();
            return;
        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Face Tracker sample")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }

    //==============================================================================================
    // Camera Source Preview
    //==============================================================================================

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    //==============================================================================================
    // Graphic Face Tracker
    //==============================================================================================

    /**
     * Factory for creating a face tracker to be associated with a new face.  The multiprocessor
     * uses this factory to create face trackers as needed -- one for each individual.
     */
    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }

    /**
     * Face tracker for each detected individual. This maintains a face graphic within the app's
     * associated face overlay.
     */
    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private GlassesGraphic mGlassesGraphic;
        private long startTime = 0;

        //Bundle extras = getIntent().getExtras();



        GraphicFaceTracker(GraphicOverlay overlay) {
            Bundle extras = getIntent().getExtras();
            int framesID = extras.getInt("selectedFrames");
            mOverlay = overlay;
            mGlassesGraphic = new GlassesGraphic(overlay, framesID);
        }

        /**
         * Start tracking the detected face instance within the face overlay.
         */
        @Override
        public void onNewItem(int faceId, Face item) {
            mGlassesGraphic.setId(faceId);
        }

        /**
         * Update the position/characteristics of the face within the overlay.
         */
        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(mGlassesGraphic);
            mGlassesGraphic.updateFace(face);

            /**
             * Heart of the "Smile to Take Photo" code. Waits at least two seconds before taking
             * a photo.
             */

            if(mGlassesGraphic.getmFaceHappiness() > 0.8f) {
                if(startTime == 0) {
                    startTime = System.currentTimeMillis();
                    Log.d(TAG, "TIMER STARTED");
                }
                Log.d(TAG, "TIMER CURRENTLY AT" + (System.currentTimeMillis()/1000L - startTime/1000L));
                if((System.currentTimeMillis() - startTime)/1000L >= 2L) {
                    startTime = 0;
                    SharedPreferences preferences = getSharedPreferences(FAVORITES_LIST, MODE_PRIVATE);

                    if(!preferences.getBoolean("" + framesPictureID, false)) {
                        //SharedPreferences settings = getSharedPreferences(FAVORITES_LIST, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("" + framesPictureID, true);

                        //Commit to save the edit
                        editor.commit();

                        Snackbar.make(mGraphicOverlay, "\"" + framesName + " (" + framesColor + ")" + "\" saved to Favorites!",
                                Snackbar.LENGTH_LONG)
                                .show();
                    } else if(alreadyInFavesCounter < 1) {
                        Snackbar.make(mGraphicOverlay, "\"" + framesName + " (" + framesColor + ")" +
                                        "\" already in Favorites!",
                                Snackbar.LENGTH_LONG)
                                .show();
                        alreadyInFavesCounter++;
                    }
                    if(TAKE_PHOTO_BY_SMILE_PERMISSION_GRANTED)
                        takeScreenshot();
                }
            } else {
                startTime = 0;
                //Log.d(TAG, "TIMER RESTARTED");
            }

        }

        /**
         * Hide the graphic when the corresponding face was not detected.  This can happen for
         * intermediate frames temporarily (e.g., if the face was momentarily blocked from
         * view).
         */
        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mGlassesGraphic);
        }

        /**
         * Called when the face is assumed to be gone for good. Remove the graphic annotation from
         * the overlay.
         */
        @Override
        public void onDone() {
            mOverlay.remove(mGlassesGraphic);
        }
    }
}

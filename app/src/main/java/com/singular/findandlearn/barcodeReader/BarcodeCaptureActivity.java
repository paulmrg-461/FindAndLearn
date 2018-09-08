package com.singular.findandlearn.barcodeReader;

import android.app.Dialog;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.singular.findandlearn.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by Paul Realpe on 24/03/2018.
 */

public class BarcodeCaptureActivity extends AppCompatActivity {
    private static final int RC_HANDLE_GMS = 9001;
    private static final String TAG = "MaterialBarcodeScanner";
    private BarcodeCapture mBarcodeCapture;
    private BarcodeCaptureBuilder mBarcodeCaptureBuilder;
    private BarcodeDetector barcodeDetector;
    private CameraSourcePreview mCameraSourcePreview;
    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    private SoundPoolPlayer mSoundPoolPlayer;

    private boolean mDetectionConsumed = false;
    private boolean mFlashOn = false;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if(getWindow() != null){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else{
            Log.e(TAG, "El lector de código de barras no pudo ejecutarse en pantalla completa");
        }
        setContentView(R.layout.activity_barcode_capture);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onBarcodeCapture(BarcodeCapture materialBarcodeCapture){
        this.mBarcodeCapture = materialBarcodeCapture;
        mBarcodeCaptureBuilder = mBarcodeCapture.getBarcodeCaptureBuilder();
        barcodeDetector = mBarcodeCapture.getBarcodeCaptureBuilder().getBarcodeDetector();
        startCameraSource();
        setupLayout();
    }
    private void setupLayout() {
        final TextView topTextView = (TextView) findViewById(R.id.topText);
        assertNotNull(topTextView);
        String topText = mBarcodeCaptureBuilder.getText();
        if(!mBarcodeCaptureBuilder.getText().equals("")){
            topTextView.setText(topText);
        }
        setupButtons();
        setupCenterTracker();
    }
    private void setupCenterTracker() {
        if(mBarcodeCaptureBuilder.getScannerMode() == BarcodeCapture.SCANNER_MODE_CENTER){
            final ImageView centerTracker  = (ImageView) findViewById(R.id.barcode_square);
            centerTracker.setImageResource(mBarcodeCaptureBuilder.getTrackerResourceID());
            mGraphicOverlay.setVisibility(View.INVISIBLE);
        }
    }
    private void updateCenterTrackerForDetectedState() {
        if(mBarcodeCaptureBuilder.getScannerMode() == BarcodeCapture.SCANNER_MODE_CENTER){
            final ImageView centerTracker  = (ImageView) findViewById(R.id.barcode_square);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    centerTracker.setImageResource(mBarcodeCaptureBuilder.getTrackerDetectedResourceID());
                }
            });
        }
    }
    private void setupButtons() {
        final LinearLayout flashOnButton = (LinearLayout)findViewById(R.id.flashIconButton);
        final ImageView flashToggleIcon = (ImageView)findViewById(R.id.flashIcon);
        assertNotNull(flashOnButton);
        flashOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFlashOn) {
                    flashToggleIcon.setBackgroundResource(R.drawable.ic_flash_on);
                    disableTorch();
                } else {
                    flashToggleIcon.setBackgroundResource(R.drawable.ic_flash_off_white_24dp);
                    enableTorch();
                }
                mFlashOn ^= true;
            }
        });
        if(mBarcodeCaptureBuilder.isFlashEnabledByDefault()){
            flashToggleIcon.setBackgroundResource(R.drawable.ic_flash_off_white_24dp);
        }
    }

    /**
     * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() throws SecurityException {
        // check that the device has play services available.
        mSoundPoolPlayer = new SoundPoolPlayer(this);
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dialog.show();
        }
        mGraphicOverlay = (GraphicOverlay<BarcodeGraphic>)findViewById(R.id.graphicOverlay);
        BarcodeGraphicTracker.NewDetectionListener listener =  new BarcodeGraphicTracker.NewDetectionListener() {
            @Override
            public void onNewDetection(Barcode barcode) {
                if(!mDetectionConsumed){
                    mDetectionConsumed = true;
                    Log.d(TAG, "Barcode detected! - " + barcode.displayValue);
                    EventBus.getDefault().postSticky(barcode);
                    updateCenterTrackerForDetectedState();
                    if(mBarcodeCaptureBuilder.isBleepEnabled()){
                        mSoundPoolPlayer.playShortResource(R.raw.bleep);
                    }
                    mGraphicOverlay.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },50);
                }
            }
        };
        BarcodeTrackerFactory barcodeFactory = new BarcodeTrackerFactory(mGraphicOverlay, listener, mBarcodeCaptureBuilder.getTrackerColor());
        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(barcodeFactory).build());
        CameraSource mCameraSource = mBarcodeCaptureBuilder.getCameraSource();
        if (mCameraSource != null) {
            try {
                mCameraSourcePreview = (CameraSourcePreview) findViewById(R.id.preview);
                mCameraSourcePreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }
    private void enableTorch() throws SecurityException{
        mBarcodeCaptureBuilder.getCameraSource().setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        try {
            mBarcodeCaptureBuilder.getCameraSource().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void disableTorch() throws SecurityException{
        mBarcodeCaptureBuilder.getCameraSource().setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        try {
            mBarcodeCaptureBuilder.getCameraSource().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraSourcePreview != null) {
            mCameraSourcePreview.stop();
        }
    }
    /**
     * Releases the resources associated with the camera source, the associated detectors, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isFinishing()){
            clean();
        }
    }

    private void clean() {
        EventBus.getDefault().removeStickyEvent(BarcodeCapture.class);
        if (mCameraSourcePreview != null) {
            mCameraSourcePreview.release();
            mCameraSourcePreview = null;
        }
        if(mSoundPoolPlayer != null){
            mSoundPoolPlayer.release();
            mSoundPoolPlayer = null;
        }
    }
}

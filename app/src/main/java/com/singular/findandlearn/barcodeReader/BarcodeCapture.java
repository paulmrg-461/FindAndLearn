package com.singular.findandlearn.barcodeReader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.vision.barcode.Barcode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import com.google.android.gms.vision.barcode.Barcode;
import com.singular.findandlearn.R;

/**
 * Created by Paul Realpe on 08/09/2018.
 */

public class BarcodeCapture {
    /**
     * Request codes
     */
    public static final int RC_HANDLE_CAMERA_PERM = 2;

    /**
     * Scanner modes
     */
    public static final int SCANNER_MODE_FREE = 1;
    public static final int SCANNER_MODE_CENTER = 2;

    protected final BarcodeCaptureBuilder mBarcodeCaptureBuilder;

    private FrameLayout mContentView; //Content frame for fragments

    private OnResultListener onResultListener;

    public BarcodeCapture(@NonNull BarcodeCaptureBuilder materialBarcodeCaptureBuilder) {
        this.mBarcodeCaptureBuilder = materialBarcodeCaptureBuilder;
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onBarcodeScannerResult(Barcode barcode){
        onResultListener.onResult(barcode);
        EventBus.getDefault().removeStickyEvent(barcode);
        EventBus.getDefault().unregister(this);
        mBarcodeCaptureBuilder.clean();
    }

    /**
     * Interface definition for a callback to be invoked when a view is clicked.
     */
    public interface OnResultListener {
        void onResult(Barcode barcode);
    }

    /**
     * Start a scan for a barcode
     *
     * This opens a new activity with the parameters provided by the MaterialBarcodeScannerBuilder
     */
    public void startScan(){
        EventBus.getDefault().register(this);
        if(mBarcodeCaptureBuilder.getActivity() == null){
            throw new RuntimeException("Could not start scan: Activity reference lost (please rebuild the MaterialBarcodeScanner before calling startScan)");
        }
        int mCameraPermission = ActivityCompat.checkSelfPermission(mBarcodeCaptureBuilder.getActivity(), Manifest.permission.CAMERA);
        if (mCameraPermission != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        }else{
            //Open activity
            EventBus.getDefault().postSticky(this);
            Intent intent = new Intent(mBarcodeCaptureBuilder.getActivity(), BarcodeCaptureActivity.class);
            mBarcodeCaptureBuilder.getActivity().startActivity(intent);
        }
    }

    private void requestCameraPermission() {
        final String[] mPermissions = new String[]{Manifest.permission.CAMERA};
        if (!ActivityCompat.shouldShowRequestPermissionRationale(mBarcodeCaptureBuilder.getActivity(), Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(mBarcodeCaptureBuilder.getActivity(), mPermissions, RC_HANDLE_CAMERA_PERM);
            return;
        }
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(mBarcodeCaptureBuilder.getActivity(), mPermissions, RC_HANDLE_CAMERA_PERM);
            }
        };
        Snackbar.make(mBarcodeCaptureBuilder.mRootView, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, listener)
                .show();
    }

    public BarcodeCaptureBuilder getBarcodeCaptureBuilder() {
        return mBarcodeCaptureBuilder;
    }
}

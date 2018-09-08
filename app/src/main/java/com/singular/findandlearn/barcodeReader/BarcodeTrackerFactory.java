package com.singular.findandlearn.barcodeReader;

import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by Paul Realpe on 24/03/2018.
 */

public class BarcodeTrackerFactory implements MultiProcessor.Factory<Barcode> {

    private GraphicOverlay<BarcodeGraphic> mGraphicOverlay;
    private BarcodeGraphicTracker.NewDetectionListener mDetectionListener;
    private int mTrackerColor;

    BarcodeTrackerFactory(GraphicOverlay<BarcodeGraphic> barcodeGraphicOverlay, BarcodeGraphicTracker.NewDetectionListener listener, int trackerColor) {
        mGraphicOverlay = barcodeGraphicOverlay;
        mDetectionListener = listener;
        mTrackerColor = trackerColor;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        BarcodeGraphic graphic = new BarcodeGraphic(mGraphicOverlay, mTrackerColor);
        BarcodeGraphicTracker tracker = new BarcodeGraphicTracker(mGraphicOverlay, graphic);
        if (mDetectionListener != null){
            tracker.setListener(mDetectionListener);
        }
        return tracker;
    }

}


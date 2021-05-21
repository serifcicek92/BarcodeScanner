package com.example.barcodescanner;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;


public class fragmentBarcodeScanner extends Fragment {

    SurfaceView surfaceView;
    CameraSource cameraSource;
    TextView okunan;
    Button buton;
    BarcodeDetector barcodeDetector;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);

        surfaceView = (SurfaceView) view.findViewById(R.id.camPreview);
        okunan = (TextView) view.findViewById(R.id.okunan);
        buton = (Button) view.findViewById(R.id.buton);
        surfaceView.setVisibility(View.GONE);
        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                barcodeDetector = new BarcodeDetector.Builder(getActivity())
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();
                cameraSource = new CameraSource.Builder(getActivity(), barcodeDetector)
                        .setRequestedPreviewSize(640, 480).setAutoFocusEnabled(true).build();


                if (surfaceView.getVisibility() == View.GONE){
                    surfaceView.setVisibility(View.VISIBLE);
                    buton.setText("Kamerayı Gizle");

                    surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                        @Override
                        public void surfaceCreated(@NonNull SurfaceHolder holder) {
                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            try {
                                cameraSource.start(holder);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

                        }

                        @Override
                        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                            cameraSource.stop();
                        }
                    });

                }else {
                    cameraSource.stop();
                    surfaceView.setVisibility(View.GONE);
                    buton.setText("Kamerayı Göster");
                }




                barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<Barcode> detections) {
                        SparseArray<Barcode> qrCodes = detections.getDetectedItems();
                        if (qrCodes.size()!=0){
                            okunan.post(new Runnable() {
                                @Override
                                public void run() {
                                    Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                    vibrator.vibrate(1000);
                                    okunan.setText(qrCodes.valueAt(0).displayValue);
                                }
                            });
                        }
                    }
                });
            }
        });



        return view;
    }
}

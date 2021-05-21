package com.example.barcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.*;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentBarcodeScanner fragmentBS = new fragmentBarcodeScanner();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.activity_main, fragmentBS,"Fragment BarcodeScanner");
        transaction.commit();
    }
}
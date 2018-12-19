package com.lixicode.flutterrouterexample;

import android.os.Bundle;

import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.view.FlutterMain;

public class RouteActivity extends FlutterActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FlutterMain.startInitialization(getApplicationContext());
        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);
    }

}

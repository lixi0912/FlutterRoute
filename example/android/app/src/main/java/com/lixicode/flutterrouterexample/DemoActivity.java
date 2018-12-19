package com.lixicode.flutterrouterexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.lixicode.flutterrouter.FlutterRouterPlugin;


public class DemoActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
//
//        FlutterRouterPlugin.build("/main/demo")
//                .withString("test", "fuck")
//                .navigation();


        Intent intent = new Intent(this, RouteActivity.class);
        intent.setAction(Intent.ACTION_RUN);
        intent.putExtra("route", "/demo?test=fuck");
        startActivity(intent);
    }
}

package com.lixicode.flutterrouterexample;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.lixicode.flutterrouter.FlutterRouterPlugin;


public class RouteInitializeProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        Context context = getContext();
        FlutterRouterPlugin.shareInstance().init(context);
        FlutterRouterPlugin.shareInstance().registerProvider(new NativeNavigationProvider());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}

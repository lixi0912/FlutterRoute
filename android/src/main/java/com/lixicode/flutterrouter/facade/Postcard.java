package com.lixicode.flutterrouter.facade;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.lixicode.flutterrouter.FlutterRouterPlugin;
import com.lixicode.flutterrouter.facade.callback.NavigationCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * <>
 *
 * @author 陈晓辉
 * @date 2018/12/14
 */
public class Postcard {
    static Postcard of(Map<String, Object> arguments) {
        return new Postcard(arguments);
    }

    private Map<String, Object> arguments;

    private Postcard(Map<String, Object> map) {
        this.arguments = map;
    }


    public Postcard(String path, String group) {
        this(path, group, null, new HashMap<String, Object>());

    }

    public Postcard(String path, String group, Uri uri, Map<String, Object> arguments) {
        arguments.put("path", path);
        arguments.put("group", group);
        if (null != uri) {
            arguments.put("uri", uri.toString());
        }
        this.arguments = arguments;
    }

    public String getPath() {
        Object value = arguments.get("path");
        if (null == value) {
            return null;
        }
        return String.valueOf(value);
    }

    public String getGroup() {
        Object value = arguments.get("group");
        if (null == value) {
            return null;
        }
        return String.valueOf(value);
    }


    public <T> T get(String key) {
        Object value = arguments.get(key);
        if (null == value) {
            return null;
        }
        try {
            //noinspection unchecked
            return (T) value;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public boolean isCallFromFlutter() {
        Object value = arguments.get("callFromFlutter");
        return null != value && Boolean.valueOf(String.valueOf(value));
    }

    public Postcard withString(String key, String value) {
        return with(key, value);
    }

    public Postcard withInt(String key, int value) {
        return with(key, value);
    }

    public Postcard withBoolean(String key, int value) {
        return with(key, value);
    }


    private Postcard with(String key, Object value) {
        arguments.put(key, value);
        return this;
    }


    Map<String, Object> getArguments() {
        return arguments;
    }

    public void navigation() {
        navigation(null, null);
    }

    public void navigation(NavigationCallback callback) {
        navigation(null, callback);
    }

    public void navigation(Context context, NavigationCallback callback) {
        FlutterRouterPlugin.shareInstance().navigation(context, this, callback);
    }


    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        Map<String, Object> map = arguments;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String)
                bundle.putString(entry.getKey(), (String) entry.getValue());
            else if (entry.getValue() instanceof Double) {
                bundle.putDouble(entry.getKey(), ((Double) entry.getValue()));
            } else if (entry.getValue() instanceof Integer) {
                bundle.putInt(entry.getKey(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof Float) {
                bundle.putFloat(entry.getKey(), ((Float) entry.getValue()));
            }
        }
        return bundle;
    }

}

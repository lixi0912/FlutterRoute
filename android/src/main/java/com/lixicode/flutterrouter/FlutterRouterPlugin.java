package com.lixicode.flutterrouter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import com.lixicode.flutterrouter.core.NavigationManager;
import com.lixicode.flutterrouter.facade.FlutterNavigationProvider;
import com.lixicode.flutterrouter.facade.Postcard;
import com.lixicode.flutterrouter.facade.callback.NavigationCallback;
import com.lixicode.flutterrouter.facade.template.NavigationProvider;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * FlutterRouterPlugin
 */
public class FlutterRouterPlugin {


    private static class SingletonHolder {
        @SuppressLint("StaticFieldLeak")
        private static final FlutterRouterPlugin instance = new FlutterRouterPlugin();
    }

    public static FlutterRouterPlugin shareInstance() {
        return SingletonHolder.instance;
    }


    private Context applicationContext;

    private FlutterRouterPlugin() {
    }

    public void init(Context context) {
        applicationContext = context.getApplicationContext();
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public void registerProvider(NavigationProvider provider) {
        NavigationManager.registerProvider(provider);
    }

    public static Postcard build(String path) {
        return build(path, extraGroup(path));
    }

    public static Postcard build(String path, String group) {
        return new Postcard(path, group);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Postcard build(Uri uri) {
        Map<String, Object> query = new LinkedHashMap<>();
        for (String name : uri.getQueryParameterNames()) {
            List<String> parameters = uri.getQueryParameters(name);
            if (!parameters.isEmpty()) {
                query.put(name, parameters.get(parameters.size() - 1));
            }
        }
        String path = uri.getPath();
        return new Postcard(path, extraGroup(path), uri, query);
    }


    private static String extraGroup(String path) {
        return null;
    }


    public void navigation(Context context, Postcard postcard, NavigationCallback callback) {
        NavigationManager.navigation(wrappedContext(context), postcard, wrappedNavigationCallback(callback));
    }

    private Context wrappedContext(Context context) {
        if (null == context) {
            return applicationContext;
        } else {
            return context;
        }
    }

    private NavigationCallback wrappedNavigationCallback(NavigationCallback callback) {
        if (null == callback) {
            return NavigationCallback.NO_OP;
        } else {
            return callback;
        }
    }


    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        FlutterNavigationProvider.registrarCallback.onRegister(registrar);
    }


}

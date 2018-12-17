package com.lixicode.flutterrouter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.lixicode.flutterrouter.core.NavigationManager;
import com.lixicode.flutterrouter.facade.FlutterNavigationProvider;
import com.lixicode.flutterrouter.facade.Postcard;
import com.lixicode.flutterrouter.facade.callback.NavigationCallback;
import com.lixicode.flutterrouter.facade.template.NavigationProvider;

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

package com.lixicode.flutterrouter.facade;

import android.content.Context;
import android.util.Log;

import com.lixicode.flutterrouter.Constants;
import com.lixicode.flutterrouter.facade.callback.RegistrarCallback;
import com.lixicode.flutterrouter.facade.template.NavigationProvider;
import com.lixicode.flutterrouter.facade.callback.NavigationCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry;

import static com.lixicode.flutterrouter.Constants.ID_FLUTTER_NAVIGATION_PROVIDER;

/**
 * <>
 *
 * @author 陈晓辉
 * @date 2018/12/14
 */
public class FlutterNavigationProvider implements NavigationProvider {


    private static final List<MethodChannel> channels = new ArrayList<>();
    public static final RegistrarCallback registrarCallback = new RegistrarCallback() {
        @Override
        public void onRegister(PluginRegistry.Registrar registrar) {
            final MethodChannel channel = new MethodChannel(registrar.messenger(), Constants.CHANNEL_NAME);
            channel.setMethodCallHandler(new RouteMethodCallHandler());
            channels.add(channel);
        }
    };


    @Override
    public String getId() {
        return ID_FLUTTER_NAVIGATION_PROVIDER;
    }


    @Override
    public boolean handleNavigation(Context context, Postcard postcard, NavigationCallback callback) {
        List<MethodChannel> methodChannels = channels;
        if (!methodChannels.isEmpty()) {
            MethodChannel channel = methodChannels.get(methodChannels.size() - 1);
            channel.invokeMethod(Constants.ROUTER_CHANNEL_NAVIGATION_OF,
                    postcard.getArguments(), new MethodChannel.Result() {
                        @Override
                        public void success(Object o) {

                        }

                        @Override
                        public void error(String s, String s1, Object o) {

                        }

                        @Override
                        public void notImplemented() {

                        }
                    });
        }
        return false;
    }

    private static class RouteMethodCallHandler implements MethodChannel.MethodCallHandler {


        @Override
        public void onMethodCall(MethodCall call, final MethodChannel.Result result) {
            // call from flutter
            if (Constants.ROUTER_CHANNEL_NAVIGATION_OF.equals(call.method)) {
                Postcard.of(call.<Map<String, Object>>arguments()).navigation(new NavigationCallbackDelegate(result));
            } else if (Constants.ROUTER_CHANNEL_NAVIGATION_ON_RESULT.equals(call.method)) {

            } else if (call.method.equals("getPlatformVersion")) {
                result.success("Android " + android.os.Build.VERSION.RELEASE);
            } else {
                result.notImplemented();
            }
        }
    }

    private static class NavigationCallbackDelegate implements NavigationCallback {


        private final MethodChannel.Result delegate;

        NavigationCallbackDelegate(MethodChannel.Result callback) {
            this.delegate = callback;
        }

        @Override
        public void onLost(Postcard postcard) {
            if (null != delegate) {
                delegate.notImplemented();
            }
            Log.e("lixicode", "no_find");
        }

        @Override
        public void onFounded(Postcard postcard) {

        }

        @Override
        public void onArrival(Postcard postcard) {
            if (null != delegate) {
                postcard.getArguments().put("arrival", true);
                delegate.success(postcard.getArguments());
            }
        }

        @Override
        public void onInterrupt(Postcard postcard) {
            if (null != delegate) {
                postcard.getArguments().put("interrupt", true);
                delegate.success(postcard.getArguments());
            }
        }
    }


}

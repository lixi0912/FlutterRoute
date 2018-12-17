package com.lixicode.flutterrouter.core;

import android.content.Context;
import android.util.Log;

import com.lixicode.flutterrouter.facade.Postcard;
import com.lixicode.flutterrouter.facade.callback.NavigationCallback;
import com.lixicode.flutterrouter.facade.template.NavigationProvider;

import java.util.Iterator;
import java.util.List;

/**
 * <>
 *
 * @author 陈晓辉
 * @date 2018/12/14
 */
public class NavigationManager {

    public static void registerProvider(NavigationProvider provider) {
        List<NavigationProvider> providers = Warehouse.providers;
        for (NavigationProvider cache : providers) {
            if (cache.getId().equals(provider.getId())) {
                return;
            }
        }
        Warehouse.providers.add(provider);
    }

    public static void navigation(Context context, Postcard postcard, NavigationCallback callback) {
        Iterator<NavigationProvider> iterator = Warehouse.providers.iterator();
        onNavigation(iterator, context, postcard, callback);
    }

    private static void onNavigation(final Iterator<NavigationProvider> providerIterable, final Context context, Postcard postcard, final NavigationCallback callback) {
        if (null == providerIterable || !providerIterable.hasNext()) {
            Log.e("lixicode", "onLost1");
            callback.onLost(postcard);
            return;
        }
        NavigationProvider provider = providerIterable.next();
        provider.handleNavigation(context, postcard, new NavigationCallback() {
            @Override
            public void onLost(Postcard postcard) {
                Log.e("lixicode", "onLost12");
                onNavigation(providerIterable, context, postcard, callback);
            }

            @Override
            public void onFounded(Postcard postcard) {
                callback.onFounded(postcard);
            }

            @Override
            public void onArrival(Postcard postcard) {
                callback.onArrival(postcard);
            }

            @Override
            public void onInterrupt(Postcard postcard) {
                callback.onInterrupt(postcard);
            }
        });
    }

}

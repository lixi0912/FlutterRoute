package com.lixicode.flutterrouterexample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lixicode.flutterrouter.Constants;
import com.lixicode.flutterrouter.facade.Postcard;
import com.lixicode.flutterrouter.facade.callback.NavigationCallback;
import com.lixicode.flutterrouter.facade.template.NavigationProvider;

public class NativeNavigationProvider implements NavigationProvider {
    @Override
    public String getId() {
        return Constants.ID_NATIVE_NAVIGATION_PROVIDER;
    }

    @Override
    public boolean handleNavigation(Context context, Postcard postcard, NavigationCallback callback) {
        if ("/main/demo2".equals(postcard.getPath())) {
            callback.onFounded(postcard);
            Intent intent = new Intent(context, DemoActivity.class);
            intent.putExtras(postcard.toBundle());
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            callback.onArrival(postcard);
        } else {
            callback.onLost(postcard);
        }
        return false;
    }
}

package com.lixicode.flutterrouter.facade.template;

import android.content.Context;

import com.lixicode.flutterrouter.facade.Postcard;
import com.lixicode.flutterrouter.facade.callback.NavigationCallback;

/**
 * <>
 *
 * @author 陈晓辉
 * @date 2018/12/14
 */
public interface NavigationProvider {

    String getId();

    boolean handleNavigation(Context context, Postcard postcard, NavigationCallback callback);
}

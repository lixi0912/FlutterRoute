package com.lixicode.flutterrouter.facade.callback;

import com.lixicode.flutterrouter.facade.Postcard;

/**
 * <>
 *
 * @author 陈晓辉
 * @date 2018/12/14
 */
public interface NavigationCallback {
    void onLost(Postcard postcard);

    void onFounded(Postcard postcard);

    void onArrival(Postcard postcard);

    void onInterrupt(Postcard postcard);

    NavigationCallback NO_OP = new NavigationCallback() {
        @Override
        public void onLost(Postcard postcard) {

        }

        @Override
        public void onFounded(Postcard postcard) {

        }

        @Override
        public void onArrival(Postcard postcard) {

        }

        @Override
        public void onInterrupt(Postcard postcard) {

        }
    };
}

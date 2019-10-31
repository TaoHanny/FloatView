package com.shutuo.floatview.service;



import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import com.shutuo.floatview.windowmanager.AssistMenuWindowManager;



public class FloatWindowService extends Service {

    /**
     * 用于在线程中创建或移除悬浮窗。
     */
    private Handler handler = new Handler();

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new RefreshTask()).start();
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        //有悬浮窗显示，则移除悬浮窗。
        if (AssistMenuWindowManager.isWindowShowing()) {

            AssistMenuWindowManager.removeSmallWindow(getApplicationContext());
            AssistMenuWindowManager.removeBigWindow(getApplicationContext());

        }

    }

    class RefreshTask implements Runnable {

        @Override
        public void run() {
            //没有悬浮窗显示，则创建悬浮窗。
            if (!AssistMenuWindowManager.isWindowShowing()) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AssistMenuWindowManager.createSmallWindow(getApplicationContext());
                    }
                });
            }
        }
    }

}

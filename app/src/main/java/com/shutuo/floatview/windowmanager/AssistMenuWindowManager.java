package com.shutuo.floatview.windowmanager;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.shutuo.floatview.view.AssistTouchViewLayout;
import com.shutuo.floatview.view.FloatWindowMenuView;
import com.shutuo.floatview.view.SpeakerViewLayout;


public class AssistMenuWindowManager {

	/**
	 * 小悬浮窗View的实例
	 */
	private static AssistTouchViewLayout smallWindow;

	/**
	 * 大悬浮窗View的实例
	 */
	private static FloatWindowMenuView bigWindow;

	/**
	 * 小悬浮窗View的参数
	 */
	private static LayoutParams smallWindowParams;

	/**
	 * 大悬浮窗View的参数
	 */
	private static LayoutParams bigWindowParams;
    /**
     * 喇叭View的实例
     */
    private static SpeakerViewLayout speakerWindow;

    /**
     * 喇叭View的参数
     */
    private static LayoutParams speakerWindowParams;

    /**
	 * 用于控制在屏幕上添加或移除悬浮窗
	 */
	private static WindowManager mWindowManager;

	/**
	 * 用于获取手机可用内存
	 */
	private static ActivityManager mActivityManager;


	/**
	 * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
	 * @param context
	 *  必须为应用程序的Context.
	 */
	public static void createSmallWindow(Context context) {
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();
		if (smallWindow == null) {
			smallWindow = new AssistTouchViewLayout(context);
			if (smallWindowParams == null) {
				smallWindowParams = new LayoutParams();
				smallWindowParams.type = LayoutParams.TYPE_PHONE;
				smallWindowParams.format = PixelFormat.RGBA_8888;
				smallWindowParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				smallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				smallWindowParams.width = AssistTouchViewLayout.viewWidth;
				smallWindowParams.height = AssistTouchViewLayout.viewHeight;
				smallWindowParams.x = screenWidth;
				smallWindowParams.y = screenHeight / 2;
			}
			smallWindow.setParams(smallWindowParams);
			windowManager.addView(smallWindow, smallWindowParams);
		}
	}

	/**
	 * 将小悬浮窗从屏幕上移除。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public static void removeSmallWindow(Context context) {
		if (smallWindow != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(smallWindow);
			smallWindow = null;
		}
	}


	/**
	 * 创建一个大悬浮窗。位置为屏幕正中间。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public static void createBigWindow(Context context,Location location){
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();
		int screenHeight = windowManager.getDefaultDisplay().getHeight();

		if(location==null){
			location = new Location(smallWindowParams.x,smallWindowParams.y);
		}

		if (bigWindow == null) {
			bigWindow = new FloatWindowMenuView(context,true);
			 int y = FloatWindowMenuView.viewHeight;
			 int x = FloatWindowMenuView.viewWidth;

				bigWindowParams = new LayoutParams();
				bigWindowParams.x = location.viewX;
				bigWindowParams.y = location.ViewY - y / 2 + smallWindowParams.height/2;
				Log.d("Manager", "createBigWindow: changedlocation = "+location.viewX+" , "+(location.ViewY - y / 2));
				bigWindowParams.type = LayoutParams.TYPE_PHONE;
				bigWindowParams.format = PixelFormat.RGBA_8888;
				bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
				bigWindowParams.width = FloatWindowMenuView.viewWidth;
				bigWindowParams.height = FloatWindowMenuView.viewHeight;

			windowManager.addView(bigWindow, bigWindowParams);
			Log.d("Manager", "createBigWindow: Params = "+bigWindowParams.x+" , "+bigWindowParams.y);
			windowManager.updateViewLayout(bigWindow,bigWindowParams);
		}
	}

	/**
	 * 将大悬浮窗从屏幕上移除。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 */
	public static void removeBigWindow(Context context) {
		if (bigWindow != null) {
			WindowManager windowManager = getWindowManager(context);
			windowManager.removeView(bigWindow);
			bigWindow = null;
		}
	}



	public static void createSpeakerWindow(Context context){
		WindowManager windowManager = getWindowManager(context);
		int screenWidth = windowManager.getDefaultDisplay().getWidth();

        if (speakerWindow == null) {
        	if(smallWindowParams.x>(screenWidth/2)){
				speakerWindow = new SpeakerViewLayout(context,true);
			}else {
				speakerWindow = new SpeakerViewLayout(context,false);
			}
            int y = SpeakerViewLayout.viewHeight;

            speakerWindowParams = new LayoutParams();
            speakerWindowParams.x =smallWindowParams.x;
            speakerWindowParams.y = smallWindowParams.y - y / 2 + smallWindowParams.height/2;
            Log.d("Manager", "createSpeakerWindow: changedlocation = "+smallWindowParams.x+" , "+(smallWindowParams.y - y / 2));
            speakerWindowParams.type = LayoutParams.TYPE_PHONE;
            speakerWindowParams.format = PixelFormat.RGBA_8888;
            speakerWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
            speakerWindowParams.width = SpeakerViewLayout.viewWidth;
            speakerWindowParams.height = SpeakerViewLayout.viewHeight;

            windowManager.addView(speakerWindow, speakerWindowParams);
            Log.d("Manager", "createSpeakerWindow: Params = "+speakerWindowParams.x+" , "+speakerWindowParams.y);
            windowManager.updateViewLayout(speakerWindow,speakerWindowParams);
        }
    }

    public static void removeSpeakerWindow(Context context){
	    if(speakerWindow != null){
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(speakerWindow);
            speakerWindow = null;
        }
    }

	/**
	 * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
	 * 
	 * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
	 */
	public static boolean isWindowShowing() {
		return smallWindow != null || bigWindow != null || speakerWindow != null;
	}

	/**
	 * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
	 * 
	 * @param context
	 *            必须为应用程序的Context.
	 * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
	 */
	private static WindowManager getWindowManager(Context context) {
		if (mWindowManager == null) {
			mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		}
		return mWindowManager;
	}

	/**
	 * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。
	 * 
	 * @param context
	 *            可传入应用程序上下文。
	 * @return ActivityManager的实例，用于获取手机可用内存。
	 */
	private static ActivityManager getActivityManager(Context context) {
		if (mActivityManager == null) {
			mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		}
		return mActivityManager;
	}



}

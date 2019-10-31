package com.shutuo.floatview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shutuo.floatview.R;
import com.shutuo.floatview.windowmanager.AssistMenuWindowManager;
import com.shutuo.floatview.windowmanager.Location;

import java.lang.reflect.Field;

public class AssistTouchViewLayout extends LinearLayout {

	//记录小悬浮窗的宽度
	public static int viewWidth;

	//记录小悬浮窗的高度
	public static int viewHeight;

	//记录系统状态栏的高度
	private static int statusBarHeight;

	//用于更新小悬浮窗的位置
	private WindowManager windowManager;

	//小悬浮窗的参数
	private WindowManager.LayoutParams mParams;

	//记录当前手指位置在屏幕上的横坐标值
	private float xInScreen;

	//记录当前手指位置在屏幕上的纵坐标值
	private float yInScreen;

	//记录手指按下时在屏幕上的横坐标的值
	private float xDownInScreen;

	//记录手指按下时在屏幕上的纵坐标的值
	private float yDownInScreen;

	//记录手指按下时在小悬浮窗的View上的横坐标的值
	private float xInView;

	//记录手指按下时在小悬浮窗的View上的纵坐标的值
	private float yInView;
	private Location location;
	//悬浮球自动靠边
	private Handler mMainHandler;
	private  static final String TAG = AssistTouchViewLayout.class.getName();
	private boolean canMoving = false;

	public AssistTouchViewLayout(Context context) {
		super(context);
		windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_assist, this);
		View view = findViewById(R.id.small_window_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		TextView percentView = (TextView) findViewById(R.id.percent);

		handlerInit();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:
				// 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
				xInView = event.getX();
				yInView = event.getY();
				xDownInScreen = event.getRawX();
				yDownInScreen = event.getRawY() - getStatusBarHeight();
				xInScreen = event.getRawX();
				yInScreen = event.getRawY() - getStatusBarHeight();
				break;
			case MotionEvent.ACTION_MOVE:
				xInScreen = event.getRawX();
				yInScreen = event.getRawY() - getStatusBarHeight();
				// 手指移动的时候更新小悬浮窗的位置
				updateViewPosition();
				break;
			case MotionEvent.ACTION_UP:
				// 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
				if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
//					openBigWindow();
					openSpeakerWindow();
				}else {
					actionUpMethod();
				}
				break;
			default:
				break;
		}
		return true;
	}

	/**
	 * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
	 *
	 * @param params
	 *            小悬浮窗的参数
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

	/**
	 * 更新小悬浮窗在屏幕中的位置。
	 */
	private void updateViewPosition() {
		mParams.x = (int) (xInScreen - xInView);
		mParams.y = (int) (yInScreen - yInView);
		windowManager.updateViewLayout(this, mParams);
	}

	/**
	 * 打开大悬浮窗，同时关闭小悬浮窗。
	 */
	private void openBigWindow() {
		location = new Location(mParams.x,mParams.y);
		Log.d(TAG, "openBigWindow: location.x = "+location.getViewX()+"location.y = "+location.getViewY());
		AssistMenuWindowManager.createBigWindow(getContext(),location);
		AssistMenuWindowManager.removeSmallWindow(getContext());
		AssistMenuWindowManager.removeBigWindow(getContext());
	}

	/**
	 * 用于获取状态栏的高度。
	 *
	 * @return 返回状态栏高度的像素值。
	 */
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}

	//手势抬起时，作位置判断
	private void actionUpMethod() {


		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenWidth = size.x;
		int screenHeight = size.y;

		//给子线程发送消息自动将按钮移动置屏幕边缘
		int destinationX;
		if (mParams.x > (screenWidth / 2)) {
			destinationX = screenWidth - viewWidth / 2;
		} else {
			destinationX = 3 - viewWidth / 2;
		}
		//将目的地和当前位置发送给自动移动位置线程
		Message childMsg = mMainHandler.obtainMessage();
		childMsg.arg1 = destinationX;
		childMsg.arg2 = mParams.x;
		childMsg.what = UPDATE_LOCATION;
		mMainHandler.sendMessage(childMsg);

		Log.i(TAG, "Send a message to the child thread - ");

	}

	private final int UPDATE_LOCATION = 1;
	private void handlerInit() {
		mMainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case UPDATE_LOCATION: {
						int destinationX = msg.arg1;
						int currentX = msg.arg2;
						updateLocation(destinationX, currentX);
						break;
					}
					default:
						break;
				}
			}
		};
	}

	private void updateLocation(int destinationX, int currentX) {
		ValueAnimator anim = ValueAnimator.ofFloat(currentX, destinationX);
		anim.setDuration(400);
		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float currentValue = (float) animation.getAnimatedValue();
				if (mParams != null && AssistTouchViewLayout.this != null) {
					mParams.x = (int) currentValue;
					windowManager.updateViewLayout(AssistTouchViewLayout.this, mParams);
				}
			}
		});
		anim.start();
	}

	private void openSpeakerWindow(){
		AssistMenuWindowManager.createSpeakerWindow(getContext());
		AssistMenuWindowManager.removeSmallWindow(getContext());
		AssistMenuWindowManager.removeBigWindow(getContext());
	}

}

package com.shutuo.floatview.view;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shutuo.floatview.R;
import com.shutuo.floatview.windowmanager.AssistMenuWindowManager;


public class FloatWindowMenuView extends LinearLayout {

	//记录大悬浮窗的宽度
	public static int viewWidth;

	//记录大悬浮窗的高度
	public static int viewHeight;

	private Context context;

	private static CircleMenuLayout mCircleMenuLayout;

	private String[] mItemTexts = new String[] { "安全中心 ", "我的账户", "信用卡" };
	private int[] mItemImgs = new int[]{
			R.mipmap.home_mbank_1_clicked,
			R.mipmap.home_mbank_5_clicked,
			R.mipmap.home_mbank_6_clicked };



	public FloatWindowMenuView(final Context context, boolean type) {
		super(context);
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.float_window_menu2, this);
		View view = findViewById(R.id.big_window_layout);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;

		mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
		mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
		mCircleMenuLayout.setFocusable(false);

		mCircleMenuLayout
				.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {

					@Override
					public void itemClick(View view, int pos) {
						Toast.makeText(context, mItemTexts[pos],
								Toast.LENGTH_SHORT).show();

						AssistMenuWindowManager.removeBigWindow(context);
						AssistMenuWindowManager.createSmallWindow(context);
					}

					@Override
					public void itemCenterClick(View view){
						Toast.makeText(context,	"关闭悬浮球",
								Toast.LENGTH_SHORT).show();

						AssistMenuWindowManager.removeBigWindow(context);
						AssistMenuWindowManager.createSmallWindow(context);
					}
				});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("FloatWindowMenuView", "onTouchEvent: "+event.getX()+" , "+event.getY());

		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:

				break;
			case MotionEvent.ACTION_MOVE:

				break;
			case MotionEvent.ACTION_UP:
				AssistMenuWindowManager.createSmallWindow(context);
				AssistMenuWindowManager.removeBigWindow(context);
				break;
			default:
				break;
		}

		return super.onTouchEvent(event);
	}
}

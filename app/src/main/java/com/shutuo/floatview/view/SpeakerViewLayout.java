package com.shutuo.floatview.view;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.LayoutInflater;
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

public class SpeakerViewLayout extends RelativeLayout {
    //记录小悬浮窗的宽度
    public static int viewWidth;

    //记录小悬浮窗的高度
    public static int viewHeight;


    //用于更新小悬浮窗的位置
    private WindowManager windowManager;
    //悬浮球自动靠边
    private Handler mMainHandler;
    private LinearLayout view;
    private boolean isRight;

    //小悬浮窗的参数
    private WindowManager.LayoutParams mParams;
    private Context context;

    public SpeakerViewLayout(Context context,boolean isRight) {
        super(context);
        this.context = context;
        this.isRight = isRight;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_speaker, this);
        view = findViewById(R.id.speaker_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        TextView percentView = (TextView) findViewById(R.id.percent);
        percentView.setText(" ");
        handlerInit();
        mMainHandler.sendEmptyMessageDelayed(UPDATE_LOCATION, 100);

    }



    public void notifyWindowOpen(boolean isRight){
        view.setVisibility(VISIBLE);
        // 尾气喷射设置动画效果并开启
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        ScaleAnimation scaleAnimation;
        if(isRight){
            scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,
                    1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
            view.setBackgroundResource(R.drawable.speaker_left);
        }else {
            scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
            view.setBackgroundResource(R.drawable.speaker_right);
        }
        scaleAnimation.setDuration(500);
        // 动画集中添加缩放和透明动画
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        view.startAnimation(animationSet);
        // 1秒之后关闭当期活动
        mMainHandler.sendEmptyMessageDelayed(2, 1000);


    }

    public void notifiyWindowClose(boolean isRight){
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenWidth = size.x;
        // 尾气喷射设置动画效果并开启
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        ScaleAnimation scaleAnimation;
        if(isRight){
            scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,
                    1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        }else {
            scaleAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        scaleAnimation.setDuration(500);
        // 动画集中添加缩放和透明动画
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        view.startAnimation(animationSet);
        //延时500MS,展示close动画
        mMainHandler.sendEmptyMessageDelayed(3, 500);
    }

    private final int UPDATE_LOCATION = 1;
    private void handlerInit() {
        mMainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_LOCATION:
                        notifyWindowOpen(isRight);
                        break;
                    case 2:
                        notifiyWindowClose(isRight);
                        break;
                    case 3:
                        view.setVisibility(GONE);
                        AssistMenuWindowManager.createSmallWindow(context);
                        AssistMenuWindowManager.removeSpeakerWindow(context);
                        break;
                    default:
                        break;
                }
            }
        };
    }



}

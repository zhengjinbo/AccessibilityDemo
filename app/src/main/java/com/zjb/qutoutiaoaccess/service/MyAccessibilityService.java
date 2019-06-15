package com.zjb.qutoutiaoaccess.service;

import android.annotation.TargetApi;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.zjb.qutoutiaoaccess.MainActivity;
import com.zjb.qutoutiaoaccess.config.Constants;
import com.zjb.qutoutiaoaccess.utils.AppUtil;
import com.zjb.qutoutiaoaccess.utils.LogUtil;
import com.zjb.qutoutiaoaccess.utils.RootCmd;
import com.zjb.qutoutiaoaccess.utils.SPUtil;

import java.util.List;

/**
 * @author ZJB
 * @date 2019/6/13 15:12
 */
public class MyAccessibilityService extends BaseAccessibilityService {
    private static String TAG = getInstance().getClass().getSimpleName();
    private String packageName, className;
    private boolean startSmallVideoReading;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogUtil.e(TAG, "---------------------onServiceConnected---------------------");
    }

    @Override
    public void onInterrupt() {
        super.onInterrupt();
        LogUtil.e(TAG, "---------------------onInterrupt---------------------");
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        super.onAccessibilityEvent(event);

        //头条  d(resourceId="com.jifen.qukan:id/jy"-->textView  com.jifen.qukan:id/a4i
        //视频  d(resourceId="com.jifen.qukan:id/jz")-->item中ImageView(播放按钮)  com.jifen.qukan:id/a6v  -->RecycleView com.jifen.qukan:id/od
        //小视频  d(resourceId="com.jifen.qukan:id/k0")
        //任务  d(resourceId="com.jifen.qukan:id/k2")
        //我的  d(resourceId="com.jifen.qukan:id/k4")


        packageName = event.getPackageName().toString();
        className = event.getClassName().toString();
        LogUtil.e("className----->", className);

        if (className.equals("com.jifen.qukan.growth.card.dialog")) {
            RootCmd.execRootCmd("input tap 350 1100");
        }

        if (className.equals("com.jifen.qukan.growth.redbag.dialog")) {
            RootCmd.execRootCmd("input tap 615 385");
        }


        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {

            if ("com.jifen.qkbase.main.MainActivity".equals(className)) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //===================小视频 begin=================
                startSmallVideoReading = SPUtil.getBoolean(getApplicationContext(), Constants.START_SMALL_VIDEO_READING, false);
                //开始观看小视频
                if (startSmallVideoReading) {
                    SPUtil.putBoolean(getApplicationContext(), Constants.START_SMALL_VIDEO_READING, false);
                    while (true) {
                        try {
                            //5秒刷新一次
                            Thread.sleep(1000 * 5);
                            RootCmd.execRootCmd("input swipe 500 1000 500 100 200");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                //====================小视频 ending==================================

                //====================视频 begin==================================

                try {
                    if (SPUtil.getBoolean(getApplicationContext(), Constants.START_VIDEO_READING, false)) {
                        SPUtil.putBoolean(getApplicationContext(), Constants.START_VIDEO_READING, false);
                        List<AccessibilityNodeInfo> recycleViewNodeInfos = event.getSource().findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/od");
                        while (true) {
                            List<AccessibilityNodeInfo> imageViewNodeInfos = event.getSource().findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/a6v");
                            LogUtil.e("imageViewNodeInfos.size() ", imageViewNodeInfos.size() + "///");
                            if (imageViewNodeInfos != null) {
                                for (int i = 0; i < imageViewNodeInfos.size(); i++) {
                                    LogUtil.e("imageViewNodeInfos.size() =" + i, AppUtil.getGson(getApplicationContext()).toJson(imageViewNodeInfos.get(i)));
                                    performViewClick(imageViewNodeInfos.get(i));
                                    Thread.sleep(1000 * 60);

                                }
                            }
                            //模拟上滑
                            performScrollForward(recycleViewNodeInfos.get(0));
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //====================视频 ending==================================

                //====================头条 begin==================================
                if (SPUtil.getBoolean(getApplicationContext(), Constants.CLICK, false)) {
                    SPUtil.putBoolean(getApplicationContext(), Constants.CLICK, false);
                    List<AccessibilityNodeInfo> textViewNodeInfos;
                    textViewNodeInfos = event.getSource().findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/a4i");
                    LogUtil.e("TextViewNodeInfos.size=", textViewNodeInfos.size() + "  //");
                    AccessibilityNodeInfo recycleViewNodeInfo = getRecycleViewNodeInfo(textViewNodeInfos.get(0));
                    performScrollForward(recycleViewNodeInfo);
//
//                    //模拟下滑5页
//                    for (int i = 0; i < 2; i++) {
//                        performScrollForward(recycleViewNodeInfo);
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }

//                    //模拟上滑3页
//                    for (int i = 0; i < 1; i++) {
//                        performScrollBackward(recycleViewNodeInfo);
//                        try {
//                            Thread.sleep(3000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    //模拟点击事件
                    textViewNodeInfos = event.getSource().findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/a4i");
                    performViewClick(textViewNodeInfos.get(0));

                }
                //====================头条 ending==================================

            } else if (className.equals("com.jifen.qukan.content.newsdetail.video.VideoNewsDetailNewActivity")) {
                LogUtil.e("视频界面", "          ------------");
                try {
                    Thread.sleep(1000 * 30);
                    SPUtil.putBoolean(getApplicationContext(), Constants.CLICK, true);
                    performBackClick();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            } else if (className.equals("com.jifen.qukan.content.newsdetail.news.NewsDetailNewActivity")) {//新闻界面
                //android.view.ViewGroup   com.jifen.qukan:id/e_
                LogUtil.e("新闻界面", "          ------------");
                try {
                    Thread.sleep(5000);
                    List<AccessibilityNodeInfo> viewGroupNodeInfo = event.getSource().findAccessibilityNodeInfosByViewId("com.jifen.qukan:id/e_");
                    for (int i = 0; i < 5; i++) {
                        dispatchScroolView(400, 1200, 0, 1000);
                        Thread.sleep(5000);
                    }
                    for (int i = 0; i < 3; i++) {
                        dispatchScroolView(400, 200, 0, -1200);
                        Thread.sleep(5000);
                    }
                    SPUtil.putBoolean(getApplicationContext(), Constants.CLICK, true);
                    performBackClick();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}

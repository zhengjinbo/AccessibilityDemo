package com.zjb.qutoutiaoaccess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zjb.qutoutiaoaccess.config.Constants;
import com.zjb.qutoutiaoaccess.service.BaseAccessibilityService;
import com.zjb.qutoutiaoaccess.utils.RootCmd;
import com.zjb.qutoutiaoaccess.utils.SPUtil;

public class MainActivity extends AppCompatActivity {
    public static boolean startReading = false;
    public static boolean startSmallVideoReading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseAccessibilityService.getInstance().init(this);
        setListener();
    }

    private void setListener() {

        //开启辅助功能
        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseAccessibilityService.getInstance().goAccess();
            }
        });

        //刷小视频
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task1();
            }
        });

        //刷视频
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task2();
            }
        });
        //刷头条
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backClick();
            }
        });

    }


    //刷小视频
    private void task1() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //打开趣头条主页
                    RootCmd.execRootCmd("am start com.jifen.qukan/com.jifen.qkbase.main.MainActivity");
                    Thread.sleep(5000);
//                    //点击视频
//                    RootCmd.execRootCmd("input tap 200 1300");
//                    Thread.sleep(1000);

                    //点击小视频
                    SPUtil.putBoolean(MainActivity.this, Constants.START_SMALL_VIDEO_READING, true);
                    RootCmd.execRootCmd("input tap 335 1300");

                    //点击任务
//                    RootCmd.execRootCmd("input tap 500 1300");
//                    Thread.sleep(3000);
                    //点击查看
//                    RootCmd.execRootCmd("input tap 620 785");
//                    Thread.sleep(1000);
                    //如何查看收益_立即查看
//                    RootCmd.execRootCmd("input tap 400 1000");
//                    Thread.sleep(1000);
//                    startReading = true;
                    //关闭（开启签到提醒）
//                    RootCmd.execRootCmd("input tap 616 388");
//                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //刷视频
    private void task2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //打开趣头条主页
                    RootCmd.execRootCmd("am start com.jifen.qukan/com.jifen.qkbase.main.MainActivity");
                    Thread.sleep(5000);
                    //点击视频
                    SPUtil.putBoolean(MainActivity.this, Constants.START_VIDEO_READING, true);
                    RootCmd.execRootCmd("input tap 200 1300");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //模拟界面返回事件
    private void backClick() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //打开趣头条主页
                    RootCmd.execRootCmd("am start com.jifen.qukan/com.jifen.qkbase.main.MainActivity");
                    Thread.sleep(5000);
                //点击头条
                SPUtil.putBoolean(MainActivity.this, Constants.CLICK, true);
                RootCmd.execRootCmd("input tap 65 1300");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}

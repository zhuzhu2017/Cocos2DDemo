package com.tongtong.cocos2ddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

public class MainActivity extends AppCompatActivity {

    private CCDirector ccDirector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        CCGLSurfaceView surfaceView = new CCGLSurfaceView(this);
        setContentView(surfaceView);
        //程序中只能有一个导演
        ccDirector = CCDirector.sharedDirector();
        //设置横屏
        ccDirector.setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);
        //是否展示帧率
        ccDirector.setDisplayFPS(true);
        //锁定帧率(向下锁定)
        ccDirector.setAnimationInterval(1.0f / 30);
        //设置屏幕大小,自动屏幕适配
        ccDirector.setScreenSize(1920, 1080);
        //开启线程
        ccDirector.attachInView(surfaceView);
        //创建场景
        CCScene ccScene = CCScene.node();
        //场景添加了图层
        ccScene.addChild(new DemoLayer());
        //运行场景
        ccDirector.runWithScene(ccScene);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ccDirector.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ccDirector.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ccDirector.end();
    }
}

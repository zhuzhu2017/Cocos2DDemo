package com.tongtong.cocos2ddemo;

import android.view.MotionEvent;

import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;

/**
 * 第一个图层
 * Created by allen on 2017/10/17.
 */

public class FirstLayer extends CCLayer {

    private CCSprite ccSprite;

    public FirstLayer() {
        //打开触摸事件开关
        setIsTouchEnabled(true);
        init();
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        //先把Android坐标系的点转换成cocos2d坐标系中的点
        CGPoint cgPoint = this.convertTouchToNodeSpace(event);
        //获取精灵的矩形
        CGRect boundingBox = ccSprite.getBoundingBox();
        //判断点是否在矩形中
        boolean containsPoint = CGRect.containsPoint(boundingBox, cgPoint);
        if (containsPoint) {
            ccSprite.setScale(ccSprite.getScale() + 0.2);
        } else {
            ccSprite.setScale(ccSprite.getScale() - 0.2);
        }
        return super.ccTouchesBegan(event);
    }

    private void init() {
        //设置背景
        CCSprite bgCCSprite = CCSprite.sprite("bbg_arena.jpg");
        bgCCSprite.setAnchorPoint(0, 0);
        this.addChild(bgCCSprite);
//        this.addChild(bgCCSprite, 1);    //如果第二个值越大，显示的越靠上，一样大谁先谁在下面
        //接受的参数就是精灵显示的图片
        ccSprite = CCSprite.sprite("z_1_attack_01.png");
        ccSprite.setAnchorPoint(0, 0);
        //设置坐标
        ccSprite.setPosition(100, 100);
        //设置缩放
        ccSprite.setScale(2);
        //设置水平翻转
        ccSprite.setFlipX(true);
        //垂直反转
//        ccSprite.setFlipY(true);
        //设置不透明度，越大越不透明
        ccSprite.setOpacity(250);
        //把精灵添加到图层上
        this.addChild(ccSprite);
    }
}

package com.tongtong.cocos2ddemo;

import android.view.MotionEvent;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.instant.CCCallFunc;
import org.cocos2d.actions.interval.CCAnimate;
import org.cocos2d.actions.interval.CCJumpBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.layers.CCTMXObjectGroup;
import org.cocos2d.layers.CCTMXTiledMap;
import org.cocos2d.nodes.CCAnimation;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.nodes.CCSpriteFrame;
import org.cocos2d.nodes.CCTextureCache;
import org.cocos2d.particlesystem.CCParticleSnow;
import org.cocos2d.particlesystem.CCParticleSystem;
import org.cocos2d.sound.SoundEngine;
import org.cocos2d.types.CGPoint;
import org.cocos2d.types.CGRect;
import org.cocos2d.types.CGSize;
import org.cocos2d.types.util.CGPointUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by allen on 2017/10/18.
 */

public class DemoLayer extends CCLayer {

    private CCTMXTiledMap map;
    private List<CGPoint> roadPoints;
    private CCSprite sprite;
    private CCParticleSystem system;

    public DemoLayer() {
        //打开触摸事件的开关
        this.setIsTouchEnabled(true);
        init();
    }

    private void init() {
        //加载地图
        loadMap();
        //解析地图
        parseMap();
        //展示雪花
        loadPartical();
        //展示僵尸
        loadZoombies();
    }

    private void loadPartical() {
        //创建粒子系统
        system = CCParticleSnow.node();
        //设置雪花样式
        system.setTexture(CCTextureCache.sharedTextureCache().addImage("f.png"));
        this.addChild(system, 1);
    }

    int position = 0;

    //展示僵尸
    private void loadZoombies() {
        sprite = CCSprite.sprite("z_1_01.png");
        sprite.setPosition(roadPoints.get(0));
        sprite.setFlipX(true);
        sprite.setScale(0.6f);
        sprite.setAnchorPoint(0.5f, 0.1f);
        map.addChild(sprite);  //通过地图添加僵尸
        //序列帧播放
        ArrayList<CCSpriteFrame> frames = new ArrayList<>();
//        //02d表示占位符，可以表示两位整数，如果不足两位，用0补足
        String format = "z_1_%02d.png";
        for (int i = 1; i <= 7; i++) {
            CCSpriteFrame ccSpriteFrame = CCSprite.sprite(String.format(format, i))
                    .displayedFrame();
            frames.add(ccSpriteFrame);
        }
//        //配置序列帧的信息 参数一：动作名字,参数二：每一帧播放的时间,参数三：所有用到的帧
        CCAnimation animation = CCAnimation.animation("walk", 0.2f, frames);
        CCAnimate ccAnimate = CCAnimate.action(animation);
        CCRepeatForever forever = CCRepeatForever.action(ccAnimate);
        sprite.runAction(forever);
        //移动到下一个点
        moveToNext();
    }

    /**
     * 移动到下一个点
     */
    int speed = 50;

    public void moveToNext() {
        position++;
        if (position < roadPoints.size()) {
            CGPoint cgPoint = roadPoints.get(position);
            //计算两点距离
            float t = CGPointUtil.distance(roadPoints.get(position - 1), cgPoint) / speed;
            CCMoveTo ccMoveTo = CCMoveTo.action(t, cgPoint);
            CCSequence ccSequence = CCSequence.actions(ccMoveTo,
                    CCCallFunc.action(this, "moveToNext"));   //调用一个对象的某一个方法
            sprite.runAction(ccSequence);
        } else { //移动完成
            system.stopSystem();//停止粒子系统
            sprite.stopAllActions();//停止所有动作
            //跳舞
            dance();
            //声音
            SoundEngine engine = SoundEngine.sharedEngine();
            engine.playSound(CCDirector.theApp, R.raw.psy, true);
        }
    }

    private void dance() {
        sprite.setAnchorPoint(0.5f, 0.5f);
        CCJumpBy ccJumpBy = CCJumpBy.action(2, ccp(-20, 10), 10, 2);
        CCRotateBy ccRotateBy = CCRotateBy.action(1, 360);
        CCSpawn ccSpawn = CCSpawn.actions(ccJumpBy, ccRotateBy);
        ccSpawn.reverse();
        CCSequence ccSequence = CCSequence.actions(ccSpawn, ccSpawn.reverse());
        CCRepeatForever forever = CCRepeatForever.action(ccSequence);
        sprite.runAction(forever);
    }

    private void parseMap() {
        roadPoints = new ArrayList<>();
        CCTMXObjectGroup objectGroupNamed = map.objectGroupNamed("road");
        ArrayList<HashMap<String, String>> objects = objectGroupNamed.objects;
        for (HashMap<String, String> hasMap : objects) {
            int x = Integer.parseInt(hasMap.get("x"));
            int y = Integer.parseInt(hasMap.get("y"));
            CGPoint cgPoint = ccp(x, y);
            roadPoints.add(cgPoint);
        }
    }

    private void loadMap() {
        map = CCTMXTiledMap.tiledMap("map.tmx");
        map.setAnchorPoint(0.5f, 0.5f);
        //因为修改了锚点，坐标也需要修改
        map.setPosition(map.getContentSize().width / 2, map.getContentSize().height / 2);
        this.addChild(map);
    }

    @Override
    public boolean ccTouchesMoved(MotionEvent event) {
        //地图随着手指的移动而移动，如果该方法生效，必须保证地图的锚点在中间位置
        map.touchMove(event, map);
        return super.ccTouchesMoved(event);
    }

    @Override
    public boolean ccTouchesBegan(MotionEvent event) {
        this.onExit();
        this.getParent().addChild(new PauseLayer());
        return super.ccTouchesBegan(event);
    }

    private CCSprite heart;

    private class PauseLayer extends CCLayer {
        public PauseLayer() {
            this.setIsTouchEnabled(true);
            heart = CCSprite.sprite("heart.png");
            //获取屏幕尺寸
            CGSize winSize = CCDirector.sharedDirector().getWinSize();
            heart.setPosition(winSize.width / 2, winSize.height / 2);
            this.addChild(heart);
        }

        @Override
        public boolean ccTouchesBegan(MotionEvent event) {
            CGRect boundingBox = heart.getBoundingBox();
            //把Android坐标系转换成Cocos2d坐标系
            CGPoint cgPoint = this.convertTouchToNodeSpace(event);
            if (CGRect.containsPoint(boundingBox, cgPoint)) {
                //关闭弹框
                this.removeSelf();
                //游戏继续
                DemoLayer.this.onEnter();
            }
            return super.ccTouchesBegan(event);
        }
    }

}

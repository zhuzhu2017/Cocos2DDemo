package com.tongtong.cocos2ddemo;

import android.support.annotation.NonNull;

import org.cocos2d.actions.base.CCRepeatForever;
import org.cocos2d.actions.ease.CCEaseOut;
import org.cocos2d.actions.interval.CCBlink;
import org.cocos2d.actions.interval.CCJumpBy;
import org.cocos2d.actions.interval.CCMoveBy;
import org.cocos2d.actions.interval.CCMoveTo;
import org.cocos2d.actions.interval.CCRotateBy;
import org.cocos2d.actions.interval.CCScaleBy;
import org.cocos2d.actions.interval.CCSequence;
import org.cocos2d.actions.interval.CCSpawn;
import org.cocos2d.actions.interval.CCTintBy;
import org.cocos2d.layers.CCLayer;
import org.cocos2d.nodes.CCLabel;
import org.cocos2d.nodes.CCNode;
import org.cocos2d.nodes.CCSprite;
import org.cocos2d.types.ccColor3B;

/**
 * Created by allen on 2017/10/18.
 */

public class ActionLayer extends CCLayer {

    public ActionLayer() {
        init();
    }

    private void init() {
//        moveTo();
//        moveBy();
        jumpBy();
//        scaleBy();
//        rotateBy();
//        ease(); //和加速度有关的动作
//        tintBy();
//        blink();
    }

    private void blink() {
        //三秒钟闪烁三次
        CCBlink ccBlink = CCBlink.action(3, 3);
        getCcSprite().runAction(ccBlink);
    }

    private void tintBy() {
        //专门显示文字的精灵
        /**
         * 参数一：显示的内容
         */
        CCLabel ccLabel = CCLabel.labelWithString("那些年我们一起追过的女孩",
                "hkbd.ttf", 66);
        ccLabel.setColor(ccc3(50, 0, 255));
        ccLabel.setPosition(500, 500);
        this.addChild(ccLabel);

        ccColor3B color = ccc3(100, 255, -100);
        CCTintBy tintBy = CCTintBy.action(1, color);
        CCTintBy reverse = tintBy.reverse();
        CCSequence sequence = CCSequence.actions(tintBy, reverse);
        CCRepeatForever forever = CCRepeatForever.action(sequence);
        ccLabel.runAction(forever);
    }

    private void ease() {
        CCMoveTo action = CCMoveTo.action(10, CCNode.ccp(500, 0));
        CCEaseOut easeOut = CCEaseOut.action(action, 5);
        getCcSprite().runAction(easeOut);
    }

    private void rotateBy() {
        CCRotateBy ccRotateBy = CCRotateBy.action(3, 60);
        CCRotateBy reverse = ccRotateBy.reverse();
        CCSequence sequence = CCSequence.actions(ccRotateBy, reverse);
        CCRepeatForever forever = CCRepeatForever.action(sequence);
        getHeartCcSprite().runAction(forever);
    }

    private void scaleBy() {
        /**
         * 参数一：时间
         * 参数二：缩放比例
         */
        CCScaleBy ccScaleBy = CCScaleBy.action(1, 0.7f);
        CCScaleBy reverse = ccScaleBy.reverse();
        CCSequence sequence = CCSequence.actions(ccScaleBy, reverse);
        CCRepeatForever forever = CCRepeatForever.action(sequence);
        getHeartCcSprite().runAction(forever);
    }


    private void jumpBy() {
        /**
         * 参数一：时间，单位秒
         * 参数二：目的地
         * 参数三：高出的高度
         * 参数四：跳跃的次数
         */
        CCJumpBy ccJumpBy = CCJumpBy.action(2, ccp(400, 400), 100, 2);
        CCRotateBy ccRotateBy = CCRotateBy.action(1, 360);
        //添加并行动作
        CCSpawn ccSpawn = CCSpawn.actions(ccJumpBy, ccRotateBy);
//        CCJumpBy reverse = ccJumpBy.reverse();
        CCSequence sequence = CCSequence.actions(ccSpawn, ccSpawn.reverse());
        CCRepeatForever forever = CCRepeatForever.action(sequence);
        CCSprite ccSprite = getCcSprite();
        ccSprite.setAnchorPoint(0.5f, 0.5f);
        ccSprite.setPosition(100, 100);
        ccSprite.runAction(forever);
    }

    private void moveBy() {
        CCSprite ccSprite = getCcSprite();
        ccSprite.setPosition(0, 200);
        CCMoveBy ccMoveBy = CCMoveBy.action(2, CCNode.ccp(200, 0));
        CCMoveBy reverse = ccMoveBy.reverse();
        CCSequence sequence = CCSequence.actions(ccMoveBy, reverse);
        ccSprite.runAction(sequence);
    }

    private void moveTo() {
        CCSprite ccSprite = getCcSprite();
        //移动到，第一个参数时间（秒），第二个参数，移动到对应的坐标
        CCMoveTo action = CCMoveTo.action(2, CCNode.ccp(200, 0));
        ccSprite.runAction(action);
    }

    @NonNull
    private CCSprite getCcSprite() {
        CCSprite ccSprite = CCSprite.sprite("z_1_attack_01.png");
        ccSprite.setAnchorPoint(0, 0);
        this.addChild(ccSprite);
        return ccSprite;
    }

    @NonNull
    private CCSprite getHeartCcSprite() {
        CCSprite ccSprite = CCSprite.sprite("heart.png");
        ccSprite.setPosition(200, 200);
        this.addChild(ccSprite);
        return ccSprite;
    }


}

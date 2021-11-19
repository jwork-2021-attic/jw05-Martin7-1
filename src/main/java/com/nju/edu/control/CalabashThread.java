package com.nju.edu.control;

import com.nju.edu.bullet.CalabashBullet;
import com.nju.edu.sprite.Calabash;

import java.util.List;

/**
 * @author Zyi
 */
public class CalabashThread implements Runnable {

    public Calabash calabash = Calabash.getCalabash();
    private GameController gameController = GameController.getGameController();
    private List<CalabashBullet> calabashBulletList = gameController.getCalabashBulletList();
    private boolean isExited = false;

    @Override
    public void run() {
        // TODO:葫芦娃的移动，葫芦娃发射子弹
        long time = GameController.getTime();
        while (!isExited) {
            calabash.onTick();
            calabashBulletMove(time);
        }
    }

    public void calabashBulletMove(long time) {
        for (CalabashBullet bullet : this.calabashBulletList) {
            bullet.move(time);
        }
    }
}

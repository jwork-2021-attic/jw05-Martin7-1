package com.nju.edu.control;

import com.nju.edu.bullet.MonsterBullet;
import com.nju.edu.screen.GameScreen;
import com.nju.edu.sprite.MonsterOne;
import com.nju.edu.sprite.MonsterThree;
import com.nju.edu.sprite.MonsterTwo;
import com.nju.edu.util.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Zyi
 */
public class MonsterThread implements Runnable {

    private static final int FIRE_INTERVAL_ONE = 2000;
    private static final int FIRE_INTERVAL_TWO = 3000;
    private static final int FIRE_INTERVAL_THREE = 1000;
    private static final int MONSTER_ONE_APPEAR = 400;
    private static final int MONSTER_TWO_APPEAR = 600;
    private static final int MONSTER_THREE_APPEAR = 1000;

    private GameController gameController = GameController.getGameController();
    private List<MonsterOne> monsterOneList = new ArrayList<>();
    private List<MonsterTwo> monsterTwoList = new ArrayList<>();
    private List<MonsterThree> monsterThreeList = new ArrayList<>();
    private List<MonsterBullet> monsterBulletList = new ArrayList<>();

    private boolean isExied = false;

    public MonsterThread() {
        this.monsterOneList = gameController.getMonsterOneList();
        this.monsterTwoList = gameController.getMonsterTwoList();
        this.monsterThreeList = gameController.getMonsterThreeList();
        this.monsterBulletList = gameController.getMonsterBulletList();
    }

    @Override
    public void run() {
        while (!isExied) {
            long time = GameController.getTime();
            if (GameController.getState() == GameState.RUNNING) {
                // 妖精的移动
                monsterMove(time);
                // 妖精子弹的移动
                monsterBulletMove(time);
                // 妖精出现
                monsterAppear(time);
                // 妖精发射子弹
                monsterFire(time);
            }
        }
    }

    private void monsterMove(long time) {
        for (MonsterOne monsterOne : monsterOneList) {
            monsterOne.move(time);
        }
        for (MonsterTwo monsterTwo : monsterTwoList) {
            monsterTwo.move(time);
        }
        for (MonsterThree monsterThree : monsterThreeList) {
            monsterThree.move(time);
        }
    }

    /**
     * 妖精子弹移动
     * @param time 游戏时间
     */
    private void monsterBulletMove(long time) {
        for (MonsterBullet monsterBullet : monsterBulletList) {
            monsterBullet.move(time);
        }
    }

    /**
     * 妖精发射子弹的时间
     * @param time 游戏时间
     */
    private void monsterFire(long time) {
        if (time % FIRE_INTERVAL_ONE == 0) {
            for (MonsterOne monsterOne : monsterOneList) {
                MonsterBullet monsterBullet = monsterOne.monsterFire();
                this.monsterBulletList.add(monsterBullet);
            }
        }
        if (time % FIRE_INTERVAL_TWO == 0) {
            for (MonsterTwo monsterTwo : monsterTwoList) {
                MonsterBullet monsterBullet = monsterTwo.monsterFire();
                this.monsterBulletList.add(monsterBullet);
            }
        }
        if (time % FIRE_INTERVAL_THREE == 0) {
            for (MonsterThree monsterThree : monsterThreeList) {
                MonsterBullet monsterBullet = monsterThree.monsterFire();
                this.monsterBulletList.add(monsterBullet);
            }
        }
    }

    /**
     * 妖精出现的时间
     * @param time 游戏时间
     */
    private void monsterAppear(long time) {
        Random random = new Random();
        // 妖精一出现的时间
        if (time % MONSTER_ONE_APPEAR == 0) {
            MonsterOne monsterOne = new MonsterOne(GameScreen.getWid(), random.nextInt(GameScreen.getHei()));
        }
        // 妖精二出现的时间
        if (time % MONSTER_TWO_APPEAR == 0) {
            MonsterTwo monsterTwo = new MonsterTwo(GameScreen.getWid(), random.nextInt(GameScreen.getHei()));
        }
        if (time % MONSTER_THREE_APPEAR == 0) {
            MonsterThree monsterThree = new MonsterThree(GameScreen.getWid(), random.nextInt(GameScreen.getHei()));
        }
    }
}

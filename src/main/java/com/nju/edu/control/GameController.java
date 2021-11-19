package com.nju.edu.control;

import com.nju.edu.bullet.CalabashBullet;
import com.nju.edu.bullet.MonsterBullet;
import com.nju.edu.screen.GameScreen;
import com.nju.edu.screen.RenderThread;
import com.nju.edu.sprite.Calabash;
import com.nju.edu.sprite.MonsterOne;
import com.nju.edu.sprite.MonsterThree;
import com.nju.edu.sprite.MonsterTwo;
import com.nju.edu.util.GameState;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Zyi
 */
public class GameController extends JPanel implements Runnable {

    public static GameController GAME_CONTROLLER = new GameController();

    /**
     * 游戏的时间
     */
    private static long TIME = 0;
    /**
     * 游戏的分数
     */
    private int score = 0;
    /**
     * 游戏的状态
     */
    private GameState state = GameState.START;
    /**
     * 用一个单独线程池来管理fps
     */
    private ExecutorService render = Executors.newSingleThreadExecutor();
    /**
     * 用一个线程池来管理妖精的出现
     */
    private ExecutorService monsters = Executors.newCachedThreadPool();

    private JLabel scoreLabel;
    private JLabel bombLabel;
    private JLabel HPLabel;

    private Calabash calabash;
    private List<MonsterOne> monsterOneList = new ArrayList<>();
    private List<MonsterTwo> monsterTwoList = new ArrayList<>();
    private List<MonsterThree> monsterThreeList = new ArrayList<>();
    private List<MonsterBullet> monsterBulletList = new ArrayList<>();
    private List<CalabashBullet> calabashBulletList = new ArrayList<>();

    private GameController() {
        this.render.submit(new RenderThread(GameScreen.getGameScreen()));
        this.monsters.submit(new MonsterThread());
        Input input = new Input();
        input.init();
        this.addKeyListener(input);


    }

    @Override
    public void run() {
        // 检测妖精的子弹是否碰到葫芦娃
        // 检测葫芦娃的子弹是否碰到妖精
    }

    public static GameController getGameController() {
        return GAME_CONTROLLER;
    }

    public void setMonsterOneList(MonsterOne monster) {
        this.monsterOneList.add(monster);
    }

    public void setMonsterTwoList(MonsterTwo monster) {
        this.monsterTwoList.add(monster);
    }

    public void setMonsterThreeList(MonsterThree monster) {
        this.monsterThreeList.add(monster);
    }

    public void setMonsterBulletList(MonsterBullet monsterBullet) {
        this.monsterBulletList.add(monsterBullet);
    }

    public void setCalabashBulletList(CalabashBullet calabashBullet) {
        this.calabashBulletList.add(calabashBullet);
    }

    public List<MonsterOne> getMonsterOneList() {
        return this.monsterOneList;
    }

    public List<MonsterTwo> getMonsterTwoList() {
        return this.monsterTwoList;
    }

    public List<MonsterThree> getMonsterThreeList() {
        return this.monsterThreeList;
    }

    public List<MonsterBullet> getMonsterBulletList() {
        return this.monsterBulletList;
    }

    public List<CalabashBullet> getCalabashBulletList() {
        return this.calabashBulletList;
    }

    public static Long getTime() {
        return TIME;
    }
}

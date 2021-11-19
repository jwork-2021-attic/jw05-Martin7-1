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
public class GameController extends JPanel {

    public static GameController GAME_CONTROLLER = new GameController();

    /**
     * 游戏的分数
     */
    private int score = 0;
    /**
     * 葫芦娃的血量
     */
    private int calabashHP = 100;
    /**
     * 游戏的状态
     */
    private GameState state = GameState.START;
    /**
     * 用一个单独线程池来管理fps
     */
    private ExecutorService render = Executors.newSingleThreadExecutor();

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
        Input input = new Input();
        input.init();
        this.addKeyListener(input);
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

    public List<MonsterOne> getMonsterOneList() {
        return this.monsterOneList;
    }

    public List<MonsterTwo> getMonsterTwoList() {
        return this.monsterTwoList;
    }

    public List<MonsterThree> getMonsterThreeList() {
        return this.monsterThreeList;
    }
}

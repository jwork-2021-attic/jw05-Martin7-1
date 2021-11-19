package com.nju.edu.control;

import com.nju.edu.bullet.CalabashBullet;
import com.nju.edu.bullet.MonsterBullet;
import com.nju.edu.screen.GameScreen;
import com.nju.edu.screen.RenderThread;
import com.nju.edu.sprite.*;
import com.nju.edu.util.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Zyi
 */
public class GameController extends JPanel implements Runnable {

    public static GameController GAME_CONTROLLER = new GameController();

    public static GameController getGameController() {
        return GAME_CONTROLLER;
    }

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
    private static GameState STATE = GameState.START;
    /**
     * 用一个单独线程池来管理fps
     */
    private ExecutorService render = Executors.newSingleThreadExecutor();
    /**
     * 用一个线程池来管理妖精的出现
     */
    private ExecutorService executor = Executors.newCachedThreadPool();

    private JLabel scoreLabel;
    private JLabel HPLabel;

    private Calabash calabash = Calabash.getCalabash();
    private List<MonsterOne> monsterOneList = new ArrayList<>();
    private List<MonsterTwo> monsterTwoList = new ArrayList<>();
    private List<MonsterThree> monsterThreeList = new ArrayList<>();
    private List<MonsterBullet> monsterBulletList = new ArrayList<>();
    private List<CalabashBullet> calabashBulletList = new ArrayList<>();

    private boolean isExited = false;
    Input input = new Input();

    private GameController() {
        input.init();
        this.addKeyListener(input);

        resetBoard();
        this.render.submit(new RenderThread(GameScreen.getGameScreen()));
        executor.submit(this);
        executor.submit(new MonsterThread());
        executor.submit(new CalabashThread());
    }

    @Override
    public void run() {
        // 检测妖精的子弹是否碰到葫芦娃
        // 检测葫芦娃的子弹是否碰到妖精
        while (!isExited) {
            // TODO: 检查碰撞, 检查分数上限
            if (STATE == GameState.RUNNING) {
                monsterCollision();
                calabashCollision();

                addTime();
                try {
                    TimeUnit.MILLISECONDS.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                repaint();
            }
        }
    }

    private void restart() {
        this.monsterOneList.clear();
        this.monsterTwoList.clear();
        this.monsterThreeList.clear();
        this.monsterBulletList.clear();
        this.calabashBulletList.clear();
        STATE = GameState.START;
        this.score = 0;
        calabash.resetHP();
        resetBoard();
    }

    private void resetBoard() {
        // 清空JPanel里的所有内容
        this.removeAll();
        this.addKeyListener(input);

        // 初始化一些Label
        scoreLabel = new JLabel("Score: " + this.score);
        scoreLabel.setForeground(Color.RED);
        HPLabel = new JLabel("HP: " + calabash.getHP());
        HPLabel.setForeground(Color.RED);

        // 游戏继续按钮
        final JButton goOnButton = new JButton("继续");
        goOnButton.setForeground(Color.RED);
        // 设置按钮为透明
        goOnButton.setContentAreaFilled(false);
        // 游戏暂停按钮
        JButton stopButton = new JButton("暂停");
        stopButton.setForeground(Color.RED);
        stopButton.setContentAreaFilled(false);
        // 继续按钮添加监听
        goOnButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                if (STATE == GameState.PAUSE) {
                    STATE = GameState.RUNNING;
                    /*
                     * 重新使JPanel成为焦点！一定需要从新请求焦点。不然暂停一次键盘就直接失灵了
                     */
                    GameController.this.requestFocusInWindow();
                }
            }
        });
        // 暂停按钮添加监听
        stopButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (STATE == GameState.RUNNING) {
                    STATE = GameState.PAUSE;
                }
            }
        });
        // 新建一个面板，把按钮和标签都加入到面板中间
        JPanel labelPanel = new JPanel();
        // 设置背景颜色为黑色
        labelPanel.setBackground(Color.BLACK);
        // 使用浮动布局管理器
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 30, 10));
        labelPanel.add(scoreLabel);
        labelPanel.add(HPLabel);
        labelPanel.add(goOnButton);
        labelPanel.add(stopButton);
        // JPanel 面板添加这个面板
        this.add(labelPanel);
    }

    private void monsterCollision() {
        // 检查妖精1是否被葫芦娃打到
        // 敌方没有血量，直接死亡
        // TODO: 这里三个循环可以抽象一个方法
        int calabashBulletLength = this.calabashBulletList.size();
        int monsterOneLength = this.monsterOneList.size();

        for (int i = 0; i < monsterOneLength; i++) {
            MonsterOne monsterOne = this.monsterOneList.get(i);
            for (int j = 0; j < calabashBulletLength; j++) {
                CalabashBullet bullet = this.calabashBulletList.get(j);
                if (GameObject.isCollide(monsterOne, bullet)) {
                    this.calabashBulletList.remove(bullet);
                    this.monsterOneList.remove(monsterOne);

                    // 第一类妖精的分数设置为10分
                    this.score += 10;
                    scoreLabel.setText("Score: " + this.score);
                    monsterOneLength--;
                    calabashBulletLength--;
                    // 返回妖精的上一个位置
                    i--;
                    // 碰撞了就可以跳出循环了
                    break;
                }
            }
        }

        int monsterTwoLength = this.monsterTwoList.size();

        for (int i = 0; i < monsterTwoLength; i++) {
            MonsterTwo monsterTwo = this.monsterTwoList.get(i);
            for (int j = 0; j < calabashBulletLength; j++) {
                CalabashBullet bullet = this.calabashBulletList.get(j);
                if (GameObject.isCollide(monsterTwo, bullet)) {
                    this.calabashBulletList.remove(bullet);
                    this.monsterTwoList.remove(monsterTwo);

                    // 第二类妖精的分数设置为20分
                    this.score += 20;
                    scoreLabel.setText("Score: " + this.score);
                    monsterTwoLength--;
                    calabashBulletLength--;
                    i--;
                    break;
                }
            }
        }

        int monsterThreeLength = this.monsterThreeList.size();

        for (int i = 0; i < monsterThreeLength; i++) {
            MonsterThree monsterThree = this.monsterThreeList.get(i);
            for (int j = 0; j < calabashBulletLength; j++) {
                CalabashBullet bullet = this.calabashBulletList.get(j);
                if (GameObject.isCollide(monsterThree, bullet)) {
                    this.calabashBulletList.remove(bullet);
                    this.monsterThreeList.remove(monsterThree);

                    // 第三类妖精的分数设置为30分
                    this.score += 30;
                    scoreLabel.setText("Score: " + this.score);
                    monsterThreeLength--;
                    calabashBulletLength--;
                    i--;
                    break;
                }
            }
        }
    }

    private void calabashCollision() {
        // 妖精子弹和葫芦娃的碰撞
        int monsterBulletLength = this.monsterBulletList.size();
        for (int i = 0; i < monsterBulletLength; i++) {
            MonsterBullet bullet = this.monsterBulletList.get(i);
            if (GameObject.isCollide(bullet, calabash)) {
                this.monsterBulletList.remove(bullet);
                monsterBulletLength--;
                // 一次造成的伤害为5点
                // TODO:改成不同的妖精有不同的伤害
                calabash.decreaseHP(5);
                if (calabash.getHP() >= 0) {
                    HPLabel.setText("HP: " + calabash.getHP());
                } else {
                    HPLabel.setText("HP: " + 0);
                }
                if (calabash.getHP() <= 0) {
                    // 死亡，结束
                    STATE = GameState.GAME_OVER;
                }
            }
        }
    }

    private void addTime() {
        TIME += 40;
    }

    @Override
    public void paint(Graphics g) {

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

    public static GameState getState() {
        return STATE;
    }
}

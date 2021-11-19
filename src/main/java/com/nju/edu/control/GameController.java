package com.nju.edu.control;

import com.nju.edu.bullet.CalabashBullet;
import com.nju.edu.bullet.MonsterBullet;
import com.nju.edu.screen.GameScreen;
import com.nju.edu.sprite.*;
import com.nju.edu.util.GameState;
import com.nju.edu.util.ReadImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Zyi
 */
public class GameController extends JPanel {

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
    public static GameState STATE = GameState.START;
    /**
     * 用一个线程池来管理妖精的出现
     */
    private ExecutorService executor = Executors.newCachedThreadPool();

    private JLabel scoreLabel;
    private JLabel HPLabel;

    private Calabash calabash;
    private List<MonsterOne> monsterOneList;
    private List<MonsterTwo> monsterTwoList;
    private List<MonsterThree> monsterThreeList;
    private List<MonsterBullet> monsterBulletList;
    private List<CalabashBullet> calabashBulletList;

    private boolean isExited = false;
    Input input;

    public GameController() {
        this.input = new Input();
        this.monsterOneList = new ArrayList<>();
        this.monsterTwoList = new ArrayList<>();
        this.monsterThreeList = new ArrayList<>();
        this.monsterBulletList = new ArrayList<>();
        this.calabashBulletList = new ArrayList<>();
        this.calabash = Calabash.getCalabash();

        input.init();
        this.addKeyListener(input);

        resetBoard();

        Thread gameThread = new Thread(new GameThread());
        gameThread.start();

//        executor.execute(new CalabashThread());
//        executor.execute(new MonsterThread());
//        executor.execute(this);
//
//        executor.shutdown();
    }

    private class GameThread implements Runnable {

        private Thread thread = Thread.currentThread();

        public GameThread() {
            System.out.println("[GameThread]created");
        }

        @Override
        public void run() {
            System.out.println(thread.getName() + " start executing.");
            while (!isExited) {
                moving();
                if (STATE == GameState.RUNNING) {
                    // 敌机的移动
                    monsterMove(TIME);
                    // 英雄机子弹的移动
                    calabashBulletMove(TIME);
                    // 敌机子弹的移动
                    monsterBulletMove(TIME);
                    // 生成敌机
                    monsterAppear(TIME);
                    // 生成敌机子弹
                    monsterFire(TIME);
                    monsterCollision();
                    calabashCollision();

                    // 时间的递增
                    addTime();
                    try {
                        TimeUnit.MILLISECONDS.sleep(40);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    repaint();
                }
            }
        }

        public void moving() {
            // 监听一下键盘的事件
            if (Input.getKeyDown(KeyEvent.VK_W) || Input.getKeyDown(KeyEvent.VK_UP)) {
                // 向上走y值减小
                // 判断会不会走出边界
                calabash.moveUp();
            } else if (Input.getKeyDown(KeyEvent.VK_S) || Input.getKeyDown(KeyEvent.VK_DOWN)) {
                // 向下走y值增大
                // 判断会不会走出边界
                calabash.moveDown();
            } else if (Input.getKeyDown(KeyEvent.VK_A) || Input.getKeyDown(KeyEvent.VK_LEFT)) {
                // 向左走x值减小
                // 判断会不会走出边界
                calabash.moveLeft();
            } else if (Input.getKeyDown(KeyEvent.VK_D) || Input.getKeyDown(KeyEvent.VK_RIGHT)) {
                // 向右走x值增大
                // 判断会不会走出边界
                calabash.moveRight();
            } else if (Input.getKeyDown(KeyEvent.VK_J)) {
                // 按j发射子弹
                CalabashBullet bullet = calabash.calabashFire();
                if (TIME % 200 == 0) {
                    calabashBulletList.add(bullet);
                }
            } else if (Input.getKeyDown(KeyEvent.VK_ENTER)) {
                if (GameController.STATE == GameState.START) {
                    STATE = GameState.RUNNING;
                } else if (GameController.STATE == GameState.GAME_OVER) {
                    STATE = GameState.RUNNING;
                    restart();
                }
            }
        }

        public void restart() {
            monsterOneList.clear();
            monsterTwoList.clear();
            monsterThreeList.clear();
            monsterBulletList.clear();
            calabashBulletList.clear();
            STATE = GameState.START;
            score = 0;
            calabash.resetHP();
            resetBoard();
        }

        private void monsterCollision() {
            // 检查妖精1是否被葫芦娃打到
            // 敌方没有血量，直接死亡
            // TODO: 这里三个循环可以抽象一个方法
            int calabashBulletLength = calabashBulletList.size();
            int monsterOneLength = monsterOneList.size();

            for (int i = 0; i < monsterOneLength; i++) {
                MonsterOne monsterOne = monsterOneList.get(i);
                for (int j = 0; j < calabashBulletLength; j++) {
                    CalabashBullet bullet = calabashBulletList.get(j);
                    if (GameObject.isCollide(monsterOne, bullet)) {
                        calabashBulletList.remove(bullet);
                        monsterOneList.remove(monsterOne);

                        // 第一类妖精的分数设置为10分
                        score += 10;
                        scoreLabel.setText("Score: " + score);
                        monsterOneLength--;
                        calabashBulletLength--;
                        // 返回妖精的上一个位置
                        i--;
                        // 碰撞了就可以跳出循环了
                        break;
                    }
                }
            }

            int monsterTwoLength = monsterTwoList.size();

            for (int i = 0; i < monsterTwoLength; i++) {
                MonsterTwo monsterTwo = monsterTwoList.get(i);
                for (int j = 0; j < calabashBulletLength; j++) {
                    CalabashBullet bullet = calabashBulletList.get(j);
                    if (GameObject.isCollide(monsterTwo, bullet)) {
                        calabashBulletList.remove(bullet);
                        monsterTwoList.remove(monsterTwo);

                        // 第二类妖精的分数设置为20分
                        score += 20;
                        scoreLabel.setText("Score: " + score);
                        monsterTwoLength--;
                        calabashBulletLength--;
                        i--;
                        break;
                    }
                }
            }

            int monsterThreeLength = monsterThreeList.size();

            for (int i = 0; i < monsterThreeLength; i++) {
                MonsterThree monsterThree = monsterThreeList.get(i);
                for (int j = 0; j < calabashBulletLength; j++) {
                    CalabashBullet bullet = calabashBulletList.get(j);
                    if (GameObject.isCollide(monsterThree, bullet)) {
                        calabashBulletList.remove(bullet);
                        monsterThreeList.remove(monsterThree);

                        // 第三类妖精的分数设置为30分
                        score += 30;
                        scoreLabel.setText("Score: " + score);
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
            int monsterBulletLength = monsterBulletList.size();
            for (int i = 0; i < monsterBulletLength; i++) {
                MonsterBullet bullet = monsterBulletList.get(i);
                if (GameObject.isCollide(bullet, calabash)) {
                    monsterBulletList.remove(bullet);
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

        private static final int FIRE_INTERVAL_ONE = 2000;
        private static final int FIRE_INTERVAL_TWO = 3000;
        private static final int FIRE_INTERVAL_THREE = 1000;
        private static final int MONSTER_ONE_APPEAR = 400;
        private static final int MONSTER_TWO_APPEAR = 600;
        private static final int MONSTER_THREE_APPEAR = 1000;

        /**
         * 妖精发射子弹的时间
         * @param time 游戏时间
         */
        private void monsterFire(long time) {
            if (time % FIRE_INTERVAL_ONE == 0) {
                for (MonsterOne monsterOne : monsterOneList) {
                    MonsterBullet monsterBullet = monsterOne.monsterFire();
                    monsterBulletList.add(monsterBullet);
                }
            }
            if (time % FIRE_INTERVAL_TWO == 0) {
                for (MonsterTwo monsterTwo : monsterTwoList) {
                    MonsterBullet monsterBullet = monsterTwo.monsterFire();
                    monsterBulletList.add(monsterBullet);
                }
            }
            if (time % FIRE_INTERVAL_THREE == 0) {
                for (MonsterThree monsterThree : monsterThreeList) {
                    MonsterBullet monsterBullet = monsterThree.monsterFire();
                    monsterBulletList.add(monsterBullet);
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
                monsterOneList.add(monsterOne);
            }
            // 妖精二出现的时间
            if (time % MONSTER_TWO_APPEAR == 0) {
                MonsterTwo monsterTwo = new MonsterTwo(GameScreen.getWid(), random.nextInt(GameScreen.getHei()));
                monsterTwoList.add(monsterTwo);
            }
            if (time % MONSTER_THREE_APPEAR == 0) {
                MonsterThree monsterThree = new MonsterThree(GameScreen.getWid(), random.nextInt(GameScreen.getHei()));
                monsterThreeList.add(monsterThree);
            }
        }

        public void calabashBulletMove(long time) {
            for (CalabashBullet bullet : calabashBulletList) {
                bullet.move(time);
            }
        }
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
        goOnButton.addActionListener(e -> {
            if (STATE == GameState.PAUSE) {
                STATE = GameState.RUNNING;
                /*
                 * 重新使JPanel成为焦点！一定需要从新请求焦点。不然暂停一次键盘就直接失灵了
                 */
                GameController.this.requestFocusInWindow();
            }
        });
        // 暂停按钮添加监听
        stopButton.addActionListener(e -> {
            if (STATE == GameState.RUNNING) {
                STATE = GameState.PAUSE;
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

    private void addTime() {
        TIME += 40;
    }

    @Override
    public void paint(Graphics g) {
        // TODO: 绘制所有物体
        super.paint(g);
        // 如果为初始状态
        if (STATE == GameState.START) {
            paintStart(g);
            // 如果为游戏运行状态
        } else if (STATE == GameState.RUNNING) {
            // 绘制英雄机
            paintCalabash(g);
            // 绘制敌机
            paintMonster(g);
            // 绘制一组敌机子弹
            paintMonsterBullets(g);
            // 绘制英雄机子弹
            paintCalabashBullets(g);
        } else if (STATE == GameState.PAUSE) {
            g.setFont(new Font("黑体", Font.BOLD, 50));
            g.setColor(Color.RED);
            g.drawString("游戏暂停!!!", 120, 330);
        }
    }

    public void paintStart(Graphics g) {
        Font font = new Font("黑体", Font.PLAIN, 20);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString("按ENTER键开始游戏", 50, 500);
        g.drawString("J:发射子弹", 50, 550);
        g.drawString("方向键:↑,↓,←,→", 50, 600);
        g.drawString("作者:Martin", 50, 650);
    }

    private void paintCalabash(Graphics g) {
        g.drawImage(ReadImage.Calabash, calabash.getX(), calabash.getY(), 100, 100, null);
    }

    private void paintMonster(Graphics g) {
        for (MonsterOne monsterOne : this.monsterOneList) {
            monsterOne.draw(g);
        }

        for (MonsterTwo monsterTwo : this.monsterTwoList) {
            monsterTwo.draw(g);
        }

        for (MonsterThree monsterThree : this.monsterThreeList) {
            monsterThree.draw(g);
        }
    }

    private void paintCalabashBullets(Graphics g) {
        for (CalabashBullet bullet : this.calabashBulletList) {
            bullet.draw(g);
        }
    }

    private void paintMonsterBullets(Graphics g) {
        for (MonsterBullet bullet : this.monsterBulletList) {
            bullet.draw(g);
        }
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

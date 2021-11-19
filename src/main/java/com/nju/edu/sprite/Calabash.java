package com.nju.edu.sprite;

import com.nju.edu.bullet.CalabashBullet;
import com.nju.edu.control.GameController;
import com.nju.edu.control.Input;
import com.nju.edu.screen.GameScreen;
import com.nju.edu.util.ReadImage;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * @author Zyi
 */
public class Calabash extends Sprite {

    public static Calabash player = new Calabash(0, GameScreen.getWid());

    public static Calabash getCalabash() {
        // 单例模式，只有一个player
        return player;
    }

    private Calabash(int x, int y) {
        super(x, y, 100, 100, ReadImage.Calabash);
        this.speed = 10;
    }

    private Calabash(int x, int y, int speed) {
        super(x, y, 100, 100, ReadImage.Calabash);
        this.speed = speed;
    }

    /**
     * 葫芦娃的血量
     */
    public int HP = 100;

    @Override
    public void onTick() {
        // 监听一下键盘的事件
        if (Input.getKeyDown(KeyEvent.VK_W) || Input.getKeyDown(KeyEvent.VK_UP)) {
            // 向上走y值减小
            // 判断会不会走出边界
            if (this.y - speed >= 0) {
                this.transfer(0, -1 * speed);
            }
        } else if (Input.getKeyDown(KeyEvent.VK_S) || Input.getKeyDown(KeyEvent.VK_DOWN)) {
            // 向下走y值增大
            // 判断会不会走出边界
            if (this.y + speed + height <= GameScreen.getHei()) {
                this.transfer(0, speed);
            }
        } else if (Input.getKeyDown(KeyEvent.VK_A) || Input.getKeyDown(KeyEvent.VK_LEFT)) {
            // 向左走x值减小
            // 判断会不会走出边界
            if (this.x - speed >= 0) {
                this.transfer(-1 * speed, 0);
            }
        } else if (Input.getKeyDown(KeyEvent.VK_D) || Input.getKeyDown(KeyEvent.VK_RIGHT)) {
            // 向右走x值增大
            // 判断会不会走出边界
            if (this.x + speed + width <= GameScreen.getWid()) {
                this.transfer(speed, 0);
            }
        } else if (Input.getKeyDown(KeyEvent.VK_J)) {
            // 按j发射子弹
            addBullet(calabashFire());
        }
    }

    public CalabashBullet calabashFire() {
        CalabashBullet bullet = new CalabashBullet(this.x + width, this.y + height / 2);
        return bullet;
    }

    /**
     * 获得当前Calabash的血量
     * @return 血量
     */
    public int getHP() {
        return this.HP;
    }

    /**
     * 受到伤害减少血量
     * @param damage 伤害
     */
    public void decreaseHP(int damage) {
        this.HP -= damage;
    }

    public void resetHP() {
        this.HP = 100;
    }

    private void addBullet(CalabashBullet bullet) {
        GameController gameController = GameController.getGameController();
        List<CalabashBullet> calabashBulletList = gameController.getCalabashBulletList();
        calabashBulletList.add(bullet);
    }
}

package com.nju.edu.sprite;

import com.nju.edu.bullet.CalabashBullet;
import com.nju.edu.control.Input;
import com.nju.edu.screen.GameScreen;
import com.nju.edu.util.ReadImage;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * @author Zyi
 */
public class Calabash extends Sprite {

    public static Calabash player = new Calabash(0, GameScreen.getWid());

    public static Calabash getInstance() {
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
        }
    }

    public CalabashBullet calabashFire() {
        CalabashBullet bullet = new CalabashBullet(this.x + width, this.y + height / 2);
        return bullet;
    }

    public int getHP() {
        return this.HP;
    }

    public void decreaseHP(int damage) {
        this.HP -= damage;
    }
}

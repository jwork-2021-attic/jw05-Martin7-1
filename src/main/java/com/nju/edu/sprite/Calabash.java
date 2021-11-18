package com.nju.edu.sprite;

import com.nju.edu.control.Input;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * @author Zyi
 */
public class Calabash extends Sprite {

    /**
     * 移动速度
     */
    private final int speed;

    public Calabash(int x, int y, int width, int height, BufferedImage image) {
        super(x, y, width, height, image);
        this.speed = 4;
    }

    public Calabash(int x, int y, int width, int height, BufferedImage image, int speed) {
        super(x, y, width, height, image);
        this.speed = speed;
    }


    @Override
    public void onTick() {
        // 监听一下键盘的事件
        if (Input.getKeyDown(KeyEvent.VK_W) || Input.getKeyDown(KeyEvent.VK_UP)) {
            // 向上走y值减小
            this.transfer(0, -1 * speed);
        } else if (Input.getKeyDown(KeyEvent.VK_S) || Input.getKeyDown(KeyEvent.VK_DOWN)) {
            // 向下走y值增大
            this.transfer(0, speed);
        } else if (Input.getKeyDown(KeyEvent.VK_A) || Input.getKeyDown(KeyEvent.VK_LEFT)) {
            // 向左走x值减小
            this.transfer(-1 * speed, 0);
        } else if (Input.getKeyDown(KeyEvent.VK_D) || Input.getKeyDown(KeyEvent.VK_RIGHT)) {
            // 向右走x值增大
            this.transfer(speed, 0);
        }
    }
}

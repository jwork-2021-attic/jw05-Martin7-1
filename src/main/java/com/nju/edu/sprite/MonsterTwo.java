package com.nju.edu.sprite;

import com.nju.edu.bullet.MonsterBullet;
import com.nju.edu.screen.GameScreen;
import com.nju.edu.util.ReadImage;

import java.awt.image.BufferedImage;

/**
 * @author Zyi
 */
public class MonsterTwo extends Sprite implements Monster {

    private boolean isGoAhead = true;

    public MonsterTwo(int x, int y) {
        super(x, y, 40, 40, ReadImage.MonsterTwo);
        this.speed = 10;
    }

    public MonsterTwo(int x, int y, int speed) {
        super(x, y, 40, 40, ReadImage.MonsterTwo);
        this.speed = speed;
    }

    @Override
    public void move(long time) {
        // MonsterTwo的设定是会上下移动
        this.x -= speed;
        if (isGoAhead) {
            if (this.y + speed < GameScreen.getHei()) {
                this.y += speed;
            } else {
                isGoAhead = false;
                this.y -= speed;
            }
        } else {
            if (this.y - speed >= 0) {
                this.y -= speed;
            } else {
                isGoAhead = true;
                this.y += speed;
            }
        }
    }

    @Override
    public MonsterBullet monsterFire() {
        MonsterBullet bullet = new MonsterBullet(this.x, this.y + height / 2);

        return bullet;
    }


}
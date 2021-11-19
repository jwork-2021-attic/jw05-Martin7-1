package com.nju.edu.sprite;

import com.nju.edu.bullet.MonsterBullet;
import com.nju.edu.util.ReadImage;

/**
 * @author Zyi
 */
public class MonsterThree extends Sprite implements Monster {

    public MonsterThree(int x, int y) {
        super(x, y, 50, 50, ReadImage.MonsterThree);
        this.speed = 2;
    }

    public MonsterThree(int x, int y, int speed) {
        super(x, y, 50, 50, ReadImage.MonsterThree);
        this.speed = speed;
    }

    @Override
    public MonsterBullet monsterFire() {
        MonsterBullet bullet = new MonsterBullet(this.x, this.y + height / 2);

        return bullet;
    }
}

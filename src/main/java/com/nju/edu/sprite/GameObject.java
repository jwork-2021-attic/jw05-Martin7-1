package com.nju.edu.sprite;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Zyi
 */
public abstract class GameObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    BufferedImage image;

    public GameObject(int x, int y, int width, int height, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    /**
     * 游戏物体的移动
     */
    public abstract void move();

    /**
     * 渲染此GameObject
     * @param g 目标graphics
     */
    public void draw(Graphics g) {

    }

    public static boolean isDead(GameObject g1, GameObject g2) {
        //step1: 求出两个矩形的中心点
        int f1x = g1.x + g1.width / 2;
        int f1y = g1.y + g1.height / 2;
        int f2x = g2.x + g2.width / 2;
        int f2y = g2.y + g2.height / 2;

        //step2: 横向和纵向碰撞检测
        boolean H = Math.abs(f1x - f2x) < (g1.width + g2.width)/2;
        boolean V = Math.abs(f1y -f2y) < (g1.height + g2.height)/2;

        //step3: 必须两个方向同时碰撞
        return H && V;
    }

    /**
     * 设置坐标
     * @param x 新的x坐标
     * @param y 新的y坐标
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 移动指定偏移的坐标
     * @param dx x坐标的偏移量
     * @param dy y坐标的偏移量
     */
    public void transfer(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * 在游戏的每帧被调用
     */
    public void onTick() {

    }

    public int getX() {
        return this.x;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int newY) {
        this.y = newY;
    }
}

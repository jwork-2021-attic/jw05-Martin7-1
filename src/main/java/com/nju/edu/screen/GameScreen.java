package com.nju.edu.screen;

import com.nju.edu.control.GameController;
import com.nju.edu.control.Input;
import com.nju.edu.sprite.GameObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zyi
 */
public class GameScreen extends JFrame {

    public static GameScreen GAME_SCREEN = new GameScreen("CalabashGame", 30, Color.BLACK);

    private GameScreen(String windowTitle, int fps, Color bgColor) {
        this.windowTitle = windowTitle;
        this.fps = fps;
        this.bgColor = bgColor;

        createScreen();
        gameController.setFocusable(true);
        gameController.requestFocus();
        this.add(gameController, BorderLayout.CENTER);
    }

    /**
     * 单例模式
     * @return 唯一的游戏屏幕对象
     */
    public static GameScreen getGameScreen() {
        return GAME_SCREEN;
    }

    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    private final String windowTitle;
    private GameController gameController = GameController.getGameController();
    private final Color bgColor;

    /**
     * frame per second
     */
    private int fps;

    private void createScreen() {
        setSize(WIDTH, HEIGHT);
        setTitle(this.windowTitle);
        setLocationRelativeTo(null);
        // setIconImage()
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

//    public void addGameObject(GameObject gameObject) {
//        this.gameObjects.add(gameObject);
//    }

    public void exit() {
        System.exit(1);
    }

    public int getFps() {
        return this.fps;
    }

    public static int getWid() {
        return WIDTH;
    }

    public static int getHei() {
        return HEIGHT;
    }

    /**
     * 设置新的fps
     * @param fps 新的fps
     * @return true if the fps has been set successfully, false otherwise
     */
    public Boolean setFps(int fps) {
        if (fps <= 0) {
            // 防御式编程
            return false;
        } else {
            this.fps = fps;
        }

        return true;
    }

//    @Override
//    public void paint(Graphics g) {
//        // 每次绘制前我们需要将当前画布上的所有内容先清除
//        // 双缓冲
//        // 在内存里创建一个和窗口长宽一样的图片(画布)
//        Image img = this.createImage(WIDTH, HEIGHT);
//        // 获得画布的Graphics
//        Graphics tempGraphics = img.getGraphics();
//        // 将画布清除为背景色
//        clear(tempGraphics);
//
//        // 渲染所有的sprite
//        for (GameObject gameObject : gameObjects) {
//            gameObject.onTick();
//            //在画布上画出每个物体
//            gameObject.draw(tempGraphics);
//        }
//
//        //将内存画布的内容复制回窗口上
//        g.drawImage(img, 0, 0, null);
//    }
//
//    public void clear(Graphics g) {
//        g.setColor(bgColor);
//        g.fillRect(0, 0, WIDTH, HEIGHT);
//    }

}

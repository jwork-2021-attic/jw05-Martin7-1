package com.nju.edu.screen;

import com.nju.edu.control.Input;
import com.nju.edu.sprite.GameObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Zyi
 */
public class GameScreen extends JFrame {

    private final int width;
    private final int height;
    private final String windowTitle;
    private List<GameObject> gameObjects;
    private final Color bgColor;
    /**
     * 用一个单独线程池来管理fps
     */
    private ExecutorService render = Executors.newSingleThreadExecutor();

    /**
     * frame per second
     */
    private int fps;

    public GameScreen(int windowWidth, int windowHeight, String windowTitle, int fps, Color bgColor) {
        this.width = windowWidth;
        this.height = windowHeight;
        this.windowTitle = windowTitle;
        this.fps = fps;
        this.bgColor = bgColor;

        gameObjects = new ArrayList<>();
        createScreen();
        this.render.submit(new RenderThread(this));
        Input input = new Input();
        input.init();
        this.addKeyListener(input);
    }

    private void createScreen() {
        setSize(this.width, this.height);
        setTitle(this.windowTitle);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void addGameObject(GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }

    public void exit() {
        System.exit(1);
    }

    public int getFps() {
        return this.fps;
    }

    /**
     * 设置新的fps
     * @param fps 新的fps
     * @return true if the fps has been set successfully, false otherwise
     */
    public Boolean setFps(int fps) {
        if (fps <= 0) {
            // 防御性编程
            return false;
        } else {
            this.fps = fps;
        }

        return true;
    }

    @Override
    public void paint(Graphics g) {
        // 每次绘制前我们需要将当前画布上的所有内容先清除
        // 双缓冲
        // 在内存里创建一个和窗口长宽一样的图片(画布)
        Image img = this.createImage(width, height);
        // 获得画布的Graphics
        Graphics tempGraphics = img.getGraphics();
        // 将画布清除为背景色
        clear(tempGraphics);

        // 渲染所有的sprite
        for (GameObject gameObject : gameObjects) {
            gameObject.onTick();
            //在画布上画出每个物体
            gameObject.draw(tempGraphics);
        }

        //将内存画布的内容复制回窗口上
        g.drawImage(img, 0, 0, null);
    }

    public void clear(Graphics g) {
        g.setColor(bgColor);
        g.fillRect(0, 0, this.width, this.height);
    }

}

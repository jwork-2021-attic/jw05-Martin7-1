package com.nju.edu.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Zyi
 */
public class Input implements KeyListener {

    /**
     * 用一个HashMap来存储某个按键是否被按下
     */
    private static Map<Integer, Boolean> keys;
    /**
     * 记录存放的按键数量
     */
    public final static int KEY_COUNTS = 300;

    public void init() {
        keys = new HashMap<>(KEY_COUNTS);
        for (int i = 0; i < KEY_COUNTS; i++) {
            keys.put(i, false);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent key) {
        // 当有某个按键被按下时
        keys.put(key.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent key) {
        // 当有某个按键松开时
        keys.put(key.getKeyCode(), false);
    }

    public static boolean getKeyDown(int keyCode) {
        return keys.get(keyCode);
    }
}

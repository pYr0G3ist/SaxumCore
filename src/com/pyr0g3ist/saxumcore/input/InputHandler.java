package com.pyr0g3ist.saxumcore.input;

import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputHandler {

    private final ArrayList<Integer> keyQueue = new ArrayList<>();
    private int TOP_INSET = 0;
    private int LEFT_INSET = 0;
    private boolean mouseDown = false;
    private Point mouse = new Point(-1, -1);

    public InputHandler(Component inputComponent) {
        inputComponent.addKeyListener(keyListener);
        inputComponent.addMouseListener(mouseListener);
        inputComponent.addMouseMotionListener(mouseListener);
        if (inputComponent instanceof Container) {
            Insets insets = ((Container) inputComponent).getInsets();
            TOP_INSET = insets.top;
            LEFT_INSET = insets.left;
        }
    }

    private final KeyAdapter keyListener = new KeyAdapter() {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent evt) {
            if (!keyQueue.contains(evt.getKeyCode())) {
                keyQueue.add(evt.getKeyCode());
            }
        }

        @Override
        public void keyReleased(KeyEvent evt) {
            if (keyQueue.contains(evt.getKeyCode())) {
                keyQueue.remove((Integer) evt.getKeyCode());
            }
        }
    };

    private final MouseAdapter mouseListener = new MouseAdapter() {

        @Override
        public void mouseDragged(MouseEvent evt) {
            mouse.x = evt.getPoint().x;
            mouse.y = evt.getPoint().y;
        }

        @Override
        public void mouseMoved(MouseEvent evt) {
            mouse.x = evt.getPoint().x;
            mouse.y = evt.getPoint().y;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseDown = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            mouseDown = false;
        }

    };

    public boolean isKeyDown(Integer keyCode) {
        return keyQueue.contains(keyCode);
    }

    public boolean isKeysDown(Integer... keyCodes) {
        return keyQueue.containsAll(Arrays.asList(keyCodes));
    }

    public boolean isKeyDownConsume(Integer keyCode) {
        if (keyQueue.contains(keyCode)) {
            keyQueue.remove(keyCode);
            return true;
        }
        return false;
    }

    public boolean isKeysDownConsume(Integer... keyCodes) {
        List codes = Arrays.asList(keyCodes);
        if (keyQueue.containsAll(codes)) {
            keyQueue.removeAll(codes);
            return true;
        }
        return false;
    }

    public Point getMouse() {
        return new Point(mouse.x - LEFT_INSET, mouse.y - TOP_INSET);
    }

    public Point getRawMouse() {
        return (Point) mouse.clone();
    }

    public boolean isMouseDown() {
        return mouseDown;
    }

    public String viewPressedKeys() {
        return keyQueue.toString();
    }
}

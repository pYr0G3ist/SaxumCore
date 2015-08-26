package com.pyr0g3ist.saxumcore;

import com.pyr0g3ist.saxumcore.draw.DrawRenderer;
import com.pyr0g3ist.saxumcore.draw.Drawable;
import com.pyr0g3ist.saxumcore.draw.RenderFrame;
import com.pyr0g3ist.saxumcore.draw.Renderer;
import com.pyr0g3ist.saxumcore.input.ScalingInputHandler;
import com.pyr0g3ist.saxumcore.util.ColorUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Core {

//===== Properties =============================================================
    protected int updateRate = 60;
    private int updateInterval = 1000000000 / updateRate;
    protected boolean running = true;
    protected boolean showDebug = false;
    // Debug
    private long updates = 0;
    private long updateRateMeasureStartTime = 0;
    private long measuredUpdateRate = 0;
    private long draws = 0;
    private long drawRateMeasureStartTime = 0;
    private long fps = 0;

//===== Components =============================================================
    private final DrawRenderer renderer = new DrawRenderer();
    protected final RenderFrame frame;
    protected final ScalingInputHandler inputHandler;
//===== Constructors ===========================================================

    public Core(int width, int height) {
        frame = new RenderFrame(
                new Dimension(width, height),
                renderer);
        inputHandler = new ScalingInputHandler(frame);
    }

    public Core(int displayWidth, int displayHeight, int contentWidth, int contentHeight) {
        frame = new RenderFrame(
                new Dimension(contentWidth, contentHeight),
                new Dimension(displayWidth, displayHeight),
                renderer);
        inputHandler = new ScalingInputHandler(frame);
    }

//===== Core Methods ===========================================================
    public final void startLoop() {
        frame.display(true, false);

        long loopStartTime = System.nanoTime();
        long accumulator = 0;

        while (running) {
            long curTime = System.nanoTime();
            long elapsedTime = curTime - loopStartTime;
            if (elapsedTime > updateInterval) {
                elapsedTime = updateInterval;
            }
            loopStartTime = curTime;

            accumulator += elapsedTime;

            if (accumulator >= updateInterval) {
                update();
                accumulator -= updateInterval;
                updates++;
                curTime = System.nanoTime();
                if (curTime - updateRateMeasureStartTime >= 1000000000) {
                    measuredUpdateRate = updates;
                    updates = 0;
                    updateRateMeasureStartTime = curTime;
                }
            }

            handleCoreKeyEvents();

            renderer.draw(
                    (Drawable) (Graphics2D g2) -> {
                        draw(g2);
                    },
                    (Drawable) (Graphics2D g2) -> {
                        drawDebug(g2);
                    });

            draws++;
            curTime = System.nanoTime();
            if (curTime - drawRateMeasureStartTime >= 1000000000) {
                fps = draws;
                draws = 0;
                drawRateMeasureStartTime = curTime;
            }
        }
    }

    protected void update() {

    }

    protected void draw(Graphics2D g2) {

    }

    private void drawDebug(Graphics2D g2) {
        if (showDebug) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, 160, frame.contentResolution.height);
            g2.setColor(Color.WHITE);
            g2.drawString("Update Interval: " + (1000 / measuredUpdateRate) + "ms", 8, 60);
            g2.drawString("Update Rate: " + measuredUpdateRate, 8, 80);
            g2.drawString("Mouse X: " + inputHandler.getMouse().x, 8, 120);
            g2.drawString("Mouse Y: " + inputHandler.getMouse().y, 8, 140);
            g2.drawString("Mouse Down: " + inputHandler.isMouseDown(), 8, 160);
            g2.drawString("Keys: " + inputHandler.viewPressedKeys(), 8, 180);
            g2.setFont(new Font("Serif", Font.BOLD, 14));
            g2.setColor(ColorUtil.getFractionColor(Color.RED, Color.GREEN, ((double) fps) / 60));
            g2.drawString("FPS: " + fps, 8, 20);
        }
    }

    private void handleCoreKeyEvents() {
        if (inputHandler.isKeyDownConsume(KeyEvent.VK_0)) {
            showDebug = !showDebug;
        }
        if (inputHandler.isKeysDownConsume(
                KeyEvent.VK_CONTROL,
                KeyEvent.VK_F)) {
            frame.display(true, !frame.isFullscreen());
        }
    }

//===== Member Access ==========================================================
    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
        updateInterval = 1000000000 / updateRate;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

}

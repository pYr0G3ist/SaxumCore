package com.pyr0g3ist.saxumcore;

import com.pyr0g3ist.saxumcore.draw.DrawRenderer;
import com.pyr0g3ist.saxumcore.draw.Drawable;
import com.pyr0g3ist.saxumcore.draw.RenderFrame;
import com.pyr0g3ist.saxumcore.input.ScalingInputHandler;
import com.pyr0g3ist.saxumcore.util.ColorUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

public class Core {

//===== Properties =============================================================
//    
    protected int updateRate = 60;
    private int updateInterval = 1000000000 / updateRate;
    protected boolean running = true;
    protected boolean showDebug = false;
    // Debug
    private long debugMeasureStart = 0;
    private long updatesThisSecond = 0;
    private long draws = 0;
    private long fps = 0;
    private long avgDrawTime = 0;
    private long totalDrawTime = 0;
    private long avgUpdateTime = 0;
    private long totalUpdateTime = 0;
    private double avgUpdatesPerLoop = 0;
    private long totalLoops = 0;
    private long avgLoopTime = 0;
    private long totalLoopTime = 0;
    private long avgUpdatesPerSecond = 0;

//===== Components =============================================================
//    
    private final DrawRenderer renderer = new DrawRenderer();
    protected final RenderFrame frame;
    protected final ScalingInputHandler inputHandler;
//===== Constructors ===========================================================
//    

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
//    
    protected void init() {

    }

    public final void startLoop() {
        init();

        frame.display(true, false);

        long loopStartTime;
        long accumulator = 0;
        long elapsedTime = 0;
        long curTime;

        while (running) {
            loopStartTime = System.nanoTime();
            accumulator += elapsedTime;

            while (accumulator >= updateInterval) {
                curTime = System.nanoTime();
                double deltaFraction = updateInterval / 1000000000d;
                update(deltaFraction);
                accumulator -= updateInterval;
                updatesThisSecond++;
                totalUpdateTime += System.nanoTime() - curTime;
            }

            handleCoreKeyEvents();

            curTime = System.nanoTime();
            renderer.draw(
                    (Drawable) (Graphics2D g2) -> {
                        draw(g2);
                    },
                    (Drawable) (Graphics2D g2) -> {
                        drawDebug(g2);
                    });
            totalDrawTime += System.nanoTime() - curTime;
            draws++;

            processDebugInfo();
            curTime = System.nanoTime();
            elapsedTime = curTime - loopStartTime;
            totalLoopTime += elapsedTime;
            totalLoops++;
        }
    }

    private void processDebugInfo() {
        long curTime = System.nanoTime();

        if (totalLoops > 0) {
            avgLoopTime = totalLoopTime / totalLoops;
        }

        if (updatesThisSecond > 0) {
            avgUpdateTime = totalUpdateTime / updatesThisSecond;
            avgUpdatesPerLoop = updatesThisSecond * 1d / totalLoops;
        }

        if (curTime - debugMeasureStart >= 1000000000) {
            avgUpdatesPerSecond = updatesThisSecond;
            updatesThisSecond = 0;
            totalLoops = 0;
            totalLoopTime = 0;
            totalUpdateTime = 0;
            debugMeasureStart = curTime;
            fps = draws;
            draws = 0;
            totalDrawTime = 0;
        }

        if (draws > 0) {
            avgDrawTime = totalDrawTime / draws;
        }
    }

    protected void update(double deltaFraction) {

    }

    protected void draw(Graphics2D g2) {

    }

    private void drawDebug(Graphics2D g2) {
        if (showDebug) {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, 160, frame.contentResolution.height);
            g2.setColor(Color.WHITE);
            g2.drawString("Avg Draw", 8, 40);
            g2.drawString(": " + avgDrawTime / 1000 + "µs", 100, 40);

            g2.drawString("Avg Update", 8, 60);
            g2.drawString(": " + avgUpdateTime / 1000 + "µs", 100, 60);

            g2.drawString("Avg Loop", 8, 80);
            g2.drawString(": " + avgLoopTime / 1000 + "µs", 100, 80);

            g2.drawString("Updates/s", 8, 120);
            g2.drawString(": " + avgUpdatesPerSecond, 100, 120);

            g2.drawString("Updates/loop", 8, 140);
            g2.drawString(": " + new DecimalFormat("#.##").format(avgUpdatesPerLoop), 100, 140);

            g2.drawString("Mouse X: " + inputHandler.getMouse().x, 8, 220);
            g2.drawString("Mouse Y: " + inputHandler.getMouse().y, 8, 240);
            g2.drawString("Mouse Down: " + inputHandler.isMouseDown(), 8, 260);
            g2.drawString("Keys: " + inputHandler.viewPressedKeys(), 8, 280);
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

//===== Getters & Setters ==========================================================
//    
    public void setUpdateRate(int updateRate) {
        this.updateRate = updateRate;
        updateInterval = 1000000000 / updateRate;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public long getFps() {
        return fps;
    }

}

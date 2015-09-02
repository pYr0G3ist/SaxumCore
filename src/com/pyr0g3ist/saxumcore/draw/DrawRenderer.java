package com.pyr0g3ist.saxumcore.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.List;

public class DrawRenderer implements DrawAgent, Renderer {

    private Graphics2D g2;

    private Dimension targetResolution;
    private Dimension contentResolution;
    private BufferStrategy buffer;

    private boolean renderEnabled = false;

    @Override
    public void draw(Drawable... drawables) {
        draw(Arrays.asList(drawables));
    }

    @Override
    public void draw(List<Drawable> drawables) {
        if (renderEnabled) {
            try {
                if (buffer != null) {
                    g2 = (Graphics2D) buffer.getDrawGraphics();
                    g2.setRenderingHint(
                            RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

                    double ratio = targetResolution.width / (double) contentResolution.width;
                    g2.scale(ratio, ratio);

                    g2.setColor(Color.black);
                    g2.fillRect(0, 0, contentResolution.width, contentResolution.height);

                    drawables.stream().forEach((drawable) -> {
                        drawable.draw(g2);
                    });

                    if (!buffer.contentsLost()) {
                            buffer.show();
                    } else {
                        System.out.println("Buffer contents lost");
                    }
                }
            } catch (NullPointerException ex) {
                // Try to go on...
            } finally {
                if (g2 != null) {
                    g2.dispose();
                }
            }
        }
    }

    @Override
    public void enableRendering(Dimension targetResolution, Dimension contentResolution, BufferStrategy buffer) {
        this.targetResolution = targetResolution;
        this.contentResolution = contentResolution;
        this.buffer = buffer;
        renderEnabled = true;
    }

    @Override
    public boolean renderingEnabled() {
        return renderEnabled;
    }

    @Override
    public void stopRendering() {
        renderEnabled = false;
    }

}

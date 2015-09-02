package com.pyr0g3ist.saxumcore.draw;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class RenderFrame extends javax.swing.JFrame {

    private Canvas drawCanvas;
    private final GraphicsDevice screenDevice
            = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice();
    private Renderer renderer;
    private boolean fullscreen = false;

    public Dimension contentResolution;
    public Dimension targetResolution;
    public Dimension fullscreenResolution;

    public RenderFrame(Dimension contentResolution, Dimension targetResolution, Renderer renderer) {
        this.contentResolution = contentResolution;
        this.targetResolution = targetResolution;
        this.renderer = renderer;

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        drawCanvas = new Canvas();
        drawCanvas.setIgnoreRepaint(true);
        drawCanvas.setPreferredSize(targetResolution);

        setLayout(new BorderLayout());
        add(drawCanvas, BorderLayout.CENTER);
        pack();

        setIgnoreRepaint(true);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public RenderFrame(Dimension contentResolution, Renderer renderer) {
        this(contentResolution, new Dimension(contentResolution), renderer);
    }

    public void display(boolean visible, boolean makeFullscreen) {
        if (renderer == null) {
            return;
        }
        if (!visible) {
            renderer.stopRendering();
            setVisible(false);
            return;
        }
        this.fullscreen = makeFullscreen;
        renderer.stopRendering();
        setVisible(false);
        dispose();
        Dimension drawResolution;
        if (makeFullscreen) {
            setUndecorated(true);
            drawCanvas.setPreferredSize(screenDevice.getDefaultConfiguration().getBounds().getSize());
            pack();
            setState(Frame.MAXIMIZED_BOTH);
            if (fullscreenResolution != null) {
                drawResolution = fullscreenResolution;
            } else {
                drawResolution = getSize();
            }
        } else {
            setUndecorated(false);
            drawCanvas.setPreferredSize(targetResolution);
            pack();
            setState(Frame.NORMAL);
            drawResolution = targetResolution;
        }
        setLocationRelativeTo(null);
        setVisible(true);
        drawCanvas.createBufferStrategy(2);
        drawCanvas.requestFocus();
        toFront();
        renderer.enableRendering(drawResolution, contentResolution, drawCanvas.getBufferStrategy());
    }

    public void setMouseVisible(boolean mouseVisible) {
        if (mouseVisible) {
            BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(0, 0), "blank cursor");
            getContentPane().setCursor(blankCursor);
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public Canvas getDrawCanvas() {
        return drawCanvas;
    }

}

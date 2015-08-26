package com.pyr0g3ist.saxumcore.draw;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class RenderFrame extends javax.swing.JFrame {

    private final GraphicsDevice screenDevice
            = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice();
    private Renderer renderer;
    private boolean fullscreen = false;

    private final Insets defaultInsets;

    public Dimension contentResolution;
    public Dimension targetResolution;
    public Dimension fullscreenResolution;

//    private long resizeStarted;
//    private boolean selfTriggeredResize = false;
    public RenderFrame(Dimension contentResolution, Dimension targetResolution, Renderer renderer) {
        this.contentResolution = contentResolution;
        this.targetResolution = targetResolution;
        this.renderer = renderer;

        initComponents();
        drawCanvas.setIgnoreRepaint(true);
        drawCanvas.setPreferredSize(targetResolution);

        setIgnoreRepaint(true);
        setLocationRelativeTo(null);
        setResizable(false);

        defaultInsets = getInsets();
    }

    public RenderFrame(Dimension contentResolution, Renderer renderer) {
        this(contentResolution, new Dimension(contentResolution), renderer);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        drawCanvas = new java.awt.Canvas();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(drawCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(drawCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
//        if (fullscreen || renderThread == null || selfTriggeredResize) {
//            selfTriggeredResize = false;
//            return;
//        }
//        if (resizeStarted == 0) {
//            resizeStarted = System.nanoTime();
//            renderThread.stopRendering();
//            new Thread() {
//
//                @Override
//                public void run() {
//                    while (System.nanoTime() - resizeStarted < 250000000) {
//                        // wait
//                    }
//                    renderThread = new RenderThread(false, getSize(), contentResolution,
//                            getBufferStrategy(), getInsets(), drawable);
//                    renderThread.start();
//                    resizeStarted = 0;
//                    double ratio = contentResolution.height / (double) contentResolution.width;
//                    selfTriggeredResize = true;
//                    setSize(getWidth(), (int) (getWidth() * ratio));
//                }
//
//            }.start();
//        }
//        resizeStarted = System.nanoTime();
    }//GEN-LAST:event_formComponentResized

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Canvas drawCanvas;
    // End of variables declaration//GEN-END:variables
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
//            screenDevice.setFullScreenWindow(this);
            setSize(screenDevice.getDefaultConfiguration().getBounds().getSize());
            setState(Frame.MAXIMIZED_BOTH);
            if (fullscreenResolution != null) {
                drawResolution = fullscreenResolution;
            } else {
                drawResolution = getSize();
            }
        } else {
            setUndecorated(false);
            setSize(targetResolution.width + defaultInsets.left - 1,
                    targetResolution.height + defaultInsets.top - 1);
//            screenDevice.setFullScreenWindow(null);
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

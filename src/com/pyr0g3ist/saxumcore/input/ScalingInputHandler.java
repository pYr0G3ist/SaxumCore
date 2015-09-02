package com.pyr0g3ist.saxumcore.input;

import com.pyr0g3ist.saxumcore.draw.RenderFrame;
import java.awt.Point;

public class ScalingInputHandler extends InputHandler {

    private final RenderFrame frame;

    public ScalingInputHandler(RenderFrame inputFrame) {
        super(inputFrame.getDrawCanvas());
        this.frame = inputFrame;
    }

    @Override
    public Point getMouse() {
        Point mouse = super.getRawMouse();
        if (frame.isFullscreen()) {
            mouse.x *= (frame.contentResolution.width / (double) frame.targetResolution.width);
            mouse.y *= (frame.contentResolution.height / (double) frame.targetResolution.height);
        }
        return mouse;
    }
}

package com.pyr0g3ist.saxumcore.input;

import com.pyr0g3ist.saxumcore.entity.Entity;
import com.pyr0g3ist.saxumcore.intersect.Intersectable;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

public abstract class MouseProxy extends Entity {

    private final JFrame frame;
    private boolean mouseModified = false;
    private boolean mouseWasDown = false;
    private MouseInteractable mouseModifier;
    private final List<MouseInteractable> itemsUnderMouse = new ArrayList<>();

//    ===== Init ===============================================================
//    
    public MouseProxy(InputHandler inputHandler, JFrame frame) {
        super(0, 0, 1, 1);
        this.frame = frame;
        this.inputHandler = inputHandler;
    }

//    ===== Entity =============================================================
//    
    @Override
    protected void applyLogic(double deltaFraction) {
        processItemsUnderMouse();
        if (inputHandler != null) {
            Point mouse = inputHandler.getMouse();
            x = mouse.x;
            y = mouse.y;
        }
        boolean mouseDown = inputHandler.isMouseDown();
        boolean mouseDownChanged = !(mouseDown && mouseWasDown);
        if (mouseModifier != null) {
            boolean mouseIsOverModifier = mouseModifier.getBounds().intersects(getBounds());
            if ((mouseDownChanged || !mouseModified) && mouseIsOverModifier) {
                modifyMouse(mouseModifier, frame, mouseDown);
                mouseModified = true;
            } else if (mouseModified && !mouseIsOverModifier) {
                mouseModified = false;
                mouseModifier = null;
                resetMouse();
            }
        }
        mouseWasDown = mouseDown;
    }

    @Override
    public void processIntersectionWith(Intersectable intersectable) {
        if (intersectable instanceof MouseInteractable) {
            itemsUnderMouse.add((MouseInteractable) intersectable);
        }
    }

//    ===== MouseProxy =========================================================
//    
    private void processItemsUnderMouse() {
        if (itemsUnderMouse.isEmpty()) {
            mouseModifier = null;
            return;
        }
        MouseInteractable topMouseInteractable = itemsUnderMouse.get(0);
        for (MouseInteractable interactable : itemsUnderMouse) {
            interactable.setClickOccurred(MouseEvent.NOBUTTON);
            if (interactable.getZIndex() > topMouseInteractable.getZIndex()) {
                topMouseInteractable = interactable;
            }
        }
        if (inputHandler != null) {
            MouseEvent lastMouseRelease = inputHandler.getLastMouseRelease();
            if (lastMouseRelease != null && mouseModifier != null) {
                mouseModifier.setClickOccurred(lastMouseRelease.getButton());
            }
        }
    }

    private void resetMouse() {
        frame.setCursor(Cursor.getDefaultCursor());
    }

    protected abstract void modifyMouse(MouseInteractable mouseModifier, JFrame jFrame, boolean mouseDown);
}

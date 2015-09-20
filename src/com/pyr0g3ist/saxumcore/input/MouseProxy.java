package com.pyr0g3ist.saxumcore.input;

import com.pyr0g3ist.saxumcore.entity.Entity;
import com.pyr0g3ist.saxumcore.intersect.Intersectable;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;

public abstract class MouseProxy extends Entity {

    private final JFrame frame;
    private boolean mouseModified = false;
    private boolean mouseWasDown = false;
    private MouseInteractable mouseModifier;
    private final List<MouseInteractable> itemsUnderMouse = new ArrayList<>();

//    ===== Init ===============================================================
//    
    public MouseProxy(JFrame frame) {
        super(0, 0, 1, 1);
        this.frame = frame;
    }

//    ===== Entity =============================================================
//    
    @Override
    protected void applyLogic(double deltaFraction) {
        if (inputHandler == null) {
            return;
        }
        processItemsUnderMouse();
        refreshMouseLocation();
        if (mouseModifier == null) {
            return;
        }
        boolean mouseDown = inputHandler.isMouseDown();
        boolean mouseDownChanged = !(mouseDown && mouseWasDown);
        boolean mouseIsOverModifier = mouseModifier.getBounds().intersects(getBounds());
        if (mouseIsOverModifier) {
            if (!mouseModified || mouseDownChanged) {
                modifyMouse(mouseModifier, frame, mouseDown);
                mouseModified = true;
            }
        } else {
            if (mouseModified) {
                mouseModified = false;
                mouseModifier = null;
                resetMouse();
            }
        }
        mouseWasDown = mouseDown;
    }

    @Override
    protected void processIntersections(Set<Intersectable> intersectables) {
        intersectables.stream()
                .filter((intersectable) -> (intersectable instanceof MouseInteractable))
                .forEach((intersectable) -> {
                    itemsUnderMouse.add((MouseInteractable) intersectable);
                });
    }

    @Override
    public void setInputHandler(InputHandler inputHandler) {
        super.setInputHandler(inputHandler);
        refreshMouseLocation();
    }

//    ===== MouseProxy =========================================================
//    
    private void refreshMouseLocation() {
        Point mouse = inputHandler.getMouse();
        x = mouse.x;
        y = mouse.y;
    }

    private void processItemsUnderMouse() {
        if (itemsUnderMouse.isEmpty()) {
            return;
        }
        MouseInteractable topMouseInteractable = itemsUnderMouse.get(0);
        for (MouseInteractable interactable : itemsUnderMouse) {
            interactable.setClickOccurred(MouseEvent.NOBUTTON);
            interactable.setMouseOver(true);
            if (interactable.getClickIndex() > topMouseInteractable.getClickIndex()) {
                topMouseInteractable = interactable;
            }
        }
        mouseModifier = topMouseInteractable;
        MouseEvent lastMouseRelease = inputHandler.consumeLastMouseRelease();
        if (lastMouseRelease != null && mouseModifier != null) {
            mouseModifier.setClickOccurred(lastMouseRelease.getButton());
        }
        itemsUnderMouse.clear();
    }

    private void resetMouse() {
        frame.setCursor(Cursor.getDefaultCursor());
    }

    protected abstract void modifyMouse(MouseInteractable mouseModifier, JFrame jFrame, boolean mouseDown);
}

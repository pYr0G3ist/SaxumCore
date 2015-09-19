package com.pyr0g3ist.saxumcore.ui;

import java.awt.event.MouseEvent;

public class Button extends Component {

    private Runnable action;
    private ButtonState state;

//    ===== Init ===============================================================
//    
    public Button(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

//    ===== Entity =============================================================
//    
    @Override
    protected void applyLogic(double deltaFraction) {
        if (clickButton == MouseEvent.BUTTON1) {
            performAction();
        }
        if (mouseOver && inputHandler.isMouseDown()) {
            state = ButtonState.DOWN;
        } else if (mouseOver) {
            state = ButtonState.HOVER;
        } else {
            state = ButtonState.DEFAULT;
        }
    }

//    ===== Button =============================================================
//    
    private void performAction() {
        if (action != null) {
            action.run();
        }
    }

//    ===== Getters & Setters ==================================================
//    
    public ButtonState getState() {
        return state;
    }

}

package com.pyr0g3ist.saxumcore.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Container extends Component {

    private final List<Component> components = new ArrayList<>();

    public Container(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

//    ===== Entity =============================================================
    @Override
    protected void applyLogic(double deltaFraction) {
        components.stream().forEach((component) -> {
            component.update(deltaFraction);
        });
    }

    @Override
    protected void drawSelf(Graphics2D g2) {
        super.drawSelf(g2);
        components.stream().forEach((component) -> {
            component.draw(g2, 0, 0);
        });
    }

}

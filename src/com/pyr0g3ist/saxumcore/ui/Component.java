package com.pyr0g3ist.saxumcore.ui;

import com.pyr0g3ist.saxumcore.entity.Entity;
import com.pyr0g3ist.saxumcore.input.ClickIndex;
import com.pyr0g3ist.saxumcore.input.MouseInteractable;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Component extends Entity implements MouseInteractable {

    public Color backgroundColor;
    public BufferedImage backgroundImage;

    public ClickIndex clickIndex = ClickIndex.UI_INDEX;

    protected int clickButton = 0;
    protected boolean mouseOver = false;

    public Component(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

//    ===== Entity =============================================================
//    
    @Override
    protected void drawSelf(Graphics2D g2) {
        if (backgroundColor != null) {
            g2.setBackground(backgroundColor);
            g2.fillRect(0, 0, width, height);
        }
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, null, 0, 0);
        }
    }

//    ===== MouseInteractable ==================================================
//    
    @Override
    public ClickIndex getClickIndex() {
        return clickIndex;
    }

    @Override
    public void setClickOccurred(int button) {
        clickButton = button;
    }

    @Override
    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

}

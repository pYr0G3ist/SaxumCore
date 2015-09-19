package com.pyr0g3ist.saxumcore.ui;

import com.pyr0g3ist.saxumcore.resource.ResourceManager;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageButton extends Button {

    private String normalImageResourceKey;
    private String hoverImageResourceKey;
    private String downImageResourceKey;

//    ===== Init ===============================================================
//    
    public ImageButton(double x, double y, int width, int height) {
        super(x, y, width, height);
    }

    public ImageButton(String normalImageResourceKey, String hoverImageResourceKey,
            String downImageResourceKey, double x, double y, int width, int height) {
        super(x, y, width, height);
        this.normalImageResourceKey = normalImageResourceKey;
        this.hoverImageResourceKey = hoverImageResourceKey;
        this.downImageResourceKey = downImageResourceKey;
    }

//    ===== Component ==========================================================
//    
    @Override
    protected void drawSelf(Graphics2D g2) {
        super.drawSelf(g2);
        if (getState() == ButtonState.DOWN) {
            drawButtonFromKey(g2, downImageResourceKey);
        } else if (getState() == ButtonState.HOVER) {
            drawButtonFromKey(g2, hoverImageResourceKey);
        } else if (getState() == ButtonState.DEFAULT) {
            drawButtonFromKey(g2, normalImageResourceKey);
        }
    }

//    ===== ImageButton ========================================================
//    
    private void drawButtonFromKey(Graphics2D g2, String key) {
        if (key != null) {
            BufferedImage normalButtonImage = ResourceManager.getInstance().getImage(key);
            g2.drawImage(normalButtonImage, null, 0, 0);
        } else if (normalImageResourceKey != null) {
            BufferedImage normalButtonImage = ResourceManager.getInstance().getImage(normalImageResourceKey);
            g2.drawImage(normalButtonImage, null, 0, 0);
        }
    }

}

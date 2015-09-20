package com.pyr0g3ist.saxumcore.input;

import java.awt.Rectangle;

public interface MouseInteractable {

    public Rectangle getBounds();
    
    public int getClickIndex();
    
    public void setClickOccurred(int button);
    
    public void setMouseOver(boolean mouseOver);

}

package com.pyr0g3ist.saxumcore.input;

import java.awt.Rectangle;

public interface MouseInteractable {

    public Rectangle getBounds();
    
    public ClickIndex getClickIndex();
    
    public void setClickOccurred(int button);
    
    public void setMouseOver(boolean mouseOver);

}

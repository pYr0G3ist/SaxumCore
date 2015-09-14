package com.pyr0g3ist.saxumcore.input;

import java.awt.Rectangle;

public interface MouseInteractable {

    public Rectangle getBounds();
    
    public int getZIndex();
    
    public void setClickOccurred(int button);

}

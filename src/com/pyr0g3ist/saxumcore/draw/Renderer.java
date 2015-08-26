package com.pyr0g3ist.saxumcore.draw;

import java.awt.Dimension;
import java.awt.image.BufferStrategy;

public interface Renderer {
    
    public void enableRendering(Dimension targetResolution, Dimension contentResolution, BufferStrategy buffer);
    
    public boolean renderingEnabled();
    
    public void stopRendering();
    
}

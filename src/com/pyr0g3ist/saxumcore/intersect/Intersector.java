package com.pyr0g3ist.saxumcore.intersect;

import java.util.List;

public interface Intersector {
    
    public void checkIntersections(List<? extends Intersectable> intersectables);
    
}

package com.pyr0g3ist.saxumcore.intersect;

import java.util.List;

public class LinearIntersector implements Intersector {

    @Override
    public void checkIntersections(List<? extends Intersectable> intersectables) {

        for (int i = 0; i < intersectables.size(); i++) {
            Intersectable intersectableI = intersectables.get(i);
            for (int j = 0; j < intersectables.size(); j++) {
                if (i != j) {
                    Intersectable intersectableJ = intersectables.get(j);
                    if (intersectableI.getBounds().intersects(
                            intersectableJ.getBounds())) {
                        intersectableI.processIntersectionWith(intersectableJ);
                        intersectableJ.processIntersectionWith(intersectableI);
                    }
                }
            }
        }

    }

}

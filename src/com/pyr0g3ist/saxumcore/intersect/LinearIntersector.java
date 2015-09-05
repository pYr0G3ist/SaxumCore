package com.pyr0g3ist.saxumcore.intersect;

import java.util.ArrayList;
import java.util.List;

public class LinearIntersector implements Intersector {

    public final List<IntersectionProcessor> intersectionProcessors = new ArrayList<>();

    public LinearIntersector() {
        intersectionProcessors.add(this::processIntercectionBetween);
    }

    public void processIntercectionBetween(Intersectable intersectableA, Intersectable intersectableB) {
        intersectableA.processIntersectionWith(intersectableB);
        intersectableB.processIntersectionWith(intersectableA);
    }

    @Override
    public void processIntersections(List<? extends Intersectable> intersectables) {
        int intersectableCount = intersectables.size();
        boolean[] checked = new boolean[intersectableCount];
        for (int i = 0; i < intersectableCount; i++) {
            Intersectable intersectableI = intersectables.get(i);
            for (int j = 0; j < intersectableCount; j++) {
                if (i != j && !checked[j]) {
                    Intersectable intersectableJ = intersectables.get(j);
                    if (intersectableI.equals(intersectableJ)) {
                        break;
                    }
                    if (intersectableI.getBounds().intersects(
                            intersectableJ.getBounds())) {
                        intersectionProcessors.stream().forEach((intersectionProcessor) -> {
                            intersectionProcessor.processIntercectionBetween(intersectableI, intersectableJ);
                        });
                    }
                }
            }
            
            checked[i] = true;
        }
    }

}

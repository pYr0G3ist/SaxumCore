package com.pyr0g3ist.saxumcore.intersect;

import com.pyr0g3ist.saxumcore.entity.Entity;
import java.util.ArrayList;
import java.util.List;

public class LinearEntityIntersector implements Intersector {

    public final List<IntersectionProcessor> intersectionProcessors = new ArrayList<>();

    public LinearEntityIntersector() {
        intersectionProcessors.add(this::processIntercectionBetween);
    }

    public void processIntercectionBetween(Intersectable intersectableA, Intersectable intersectableB) {
        intersectableA.processIntersectionWith(intersectableB);
        intersectableB.processIntersectionWith(intersectableA);
    }

    @Override
    public void processIntersections(List<? extends Intersectable> intersectables) {
        int intersectableCount = intersectables.size();
        boolean[] skip = new boolean[intersectableCount];
        for (int i = 0; i < intersectableCount; i++) {
            Intersectable intersectable = intersectables.get(i);
            doIntersectWithList(intersectable, intersectables, skip);
            if (intersectable instanceof Entity) {
                List<Intersectable> entityIntersectables = ((Entity) intersectable).subIntersectables;
                entityIntersectables.stream().forEach((entityIntersectable) -> {
                    doIntersectWithList(entityIntersectable, intersectables, new boolean[intersectables.size()]);
                });
            }
            skip[i] = true;
        }
    }

    private void doIntersectWithList(Intersectable intersectable, List<? extends Intersectable> intersectables, boolean[] skip) {
        for (int j = 0; j < intersectables.size(); j++) {
            if (!skip[j]) {
                Intersectable compareIntersectable = intersectables.get(j);
                if (intersectable.equals(compareIntersectable)) {
                    continue;
                }
                if (intersectable.getBounds().intersects(compareIntersectable.getBounds())) {
                    intersectionProcessors.stream().forEach((intersectionProcessor) -> {
                        intersectionProcessor.processIntercectionBetween(intersectable, compareIntersectable);
                    });
                }
            }
        }
    }

}

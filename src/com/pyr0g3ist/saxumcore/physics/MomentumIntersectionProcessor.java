package com.pyr0g3ist.saxumcore.physics;

import com.pyr0g3ist.saxumcore.entity.Entity;
import com.pyr0g3ist.saxumcore.intersect.Intersectable;
import com.pyr0g3ist.saxumcore.intersect.IntersectionProcessor;

public class MomentumIntersectionProcessor implements IntersectionProcessor {

    private final MomentumProcessor momentumProcessor = MomentumProcessor.getInstance();

    @Override
    public void processIntercectionBetween(Intersectable intersectableA, Intersectable intersectableB) {
        if (intersectableA instanceof Entity
                && intersectableB instanceof Entity) {
            momentumProcessor.processMomentumCollision(
                    (Entity) intersectableA,
                    (Entity) intersectableB);
        }
    }

}

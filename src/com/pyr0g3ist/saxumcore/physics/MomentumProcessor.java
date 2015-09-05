package com.pyr0g3ist.saxumcore.physics;

import com.pyr0g3ist.saxumcore.entity.Entity;

public final class MomentumProcessor {

    private static MomentumProcessor instance;

    private MomentumProcessor() {
    }

    public void processMomentumCollision(Entity entityA, Entity entityB) {
        double m1 = entityA.mass;
        double m2 = entityB.mass;

        if (m1 == 0 || m2 == 0) {
            return;
        }

//        Undo the movement that caused the intersection
        if (entityA.xVelocity + entityA.yVelocity
                + entityB.xVelocity + entityB.yVelocity > 0) {
            while (entityA.getBounds().intersects(entityB.getBounds())) {
                entityA.x -= entityA.xVelocity;
                entityA.y -= entityA.yVelocity;
                entityB.x -= entityB.xVelocity;
                entityB.y -= entityB.yVelocity;
            }
        }

//        Adjust speeds based on momentum exchange
        double thisNewXSpeed = (((m1 - m2) / (m1 + m2)) * entityA.xVelocity)
                + (((m2 * 2) / (m1 + m2)) * entityB.xVelocity);
        double thisNewYSpeed = (((m1 - m2) / (m1 + m2)) * entityA.yVelocity)
                + (((m2 * 2) / (m1 + m2)) * entityB.yVelocity);

        double otherNewXSpeed = (((m2 - m1) / (m1 + m2)) * entityB.xVelocity)
                + (((m1 * 2) / (m1 + m2)) * entityA.xVelocity);
        double otherNewYSpeed = (((m2 - m1) / (m1 + m2)) * entityB.yVelocity)
                + (((m1 * 2) / (m1 + m2)) * entityA.yVelocity);

        entityA.xVelocity = thisNewXSpeed;
        entityA.yVelocity = thisNewYSpeed;

        entityB.xVelocity = otherNewXSpeed;
        entityB.yVelocity = otherNewYSpeed;
    }

    public static MomentumProcessor getInstance() {
        if (instance == null) {
            instance = new MomentumProcessor();
        }
        return instance;
    }

}

package com.pyr0g3ist.saxumcore.world;

import com.pyr0g3ist.saxumcore.entity.Entity;
import com.pyr0g3ist.saxumcore.input.ScalingInputHandler;
import com.pyr0g3ist.saxumcore.intersect.Intersectable;
import com.pyr0g3ist.saxumcore.intersect.IntersectionHandler;
import com.pyr0g3ist.saxumcore.intersect.Intersector;
import com.pyr0g3ist.saxumcore.intersect.LinearIntersector;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World {

    private final Rectangle bounds;
    private final ViewPort viewPort;
    private final ScalingInputHandler inputHandler;
    private Intersector intersector = new LinearIntersector();

    private final List<Entity> entities = new ArrayList<>();
    private final List<Entity> visibleEntities = new ArrayList<>();

    public int scrollSpeed = 8;

    public boolean threadsafe = false;

    public World(int worldWidth, int worldHeight, Rectangle viewPortBounds, ScalingInputHandler inputHandler) {
        this.bounds = new Rectangle(worldWidth, worldHeight);
        this.viewPort = new ViewPort(
                viewPortBounds.x,
                viewPortBounds.y,
                viewPortBounds.width,
                viewPortBounds.height);
        entities.add(viewPort);
        viewPort.intersectionHandlers.add(this::addVisibleEntity);
        this.inputHandler = inputHandler;
    }

    public void update() {
        checkInput();
        visibleEntities.clear();
        intersector.checkIntersections(entities);
        List<Entity> removeList = new ArrayList<>();
        entities.stream().forEach((entity) -> {
            if (entity.needsDisposal()) {
                removeList.add(entity);
            } else {
                entity.update();
            }
        });
        removeList.stream().forEach(entities::remove);
    }

    private void checkInput() {
        if (inputHandler != null) {
            Point mouse = inputHandler.getMouse();
            boolean hasSet = false;
            if (inputHandler.isKeyDown(KeyEvent.VK_LEFT)
                    || mouse.x < 2) {
                panView(ScrollDirection.LEFT, hasSet);
                hasSet = true;
            }
            if (inputHandler.isKeyDown(KeyEvent.VK_RIGHT)
                    || mouse.x > viewPort.width - 2) {
                panView(ScrollDirection.RIGHT, hasSet);
                hasSet = true;
            }
            if (inputHandler.isKeyDown(KeyEvent.VK_UP)
                    || mouse.y < 2) {
                panView(ScrollDirection.UP, hasSet);
                hasSet = true;
            }
            if (inputHandler.isKeyDown(KeyEvent.VK_DOWN)
                    || mouse.y > viewPort.height - 2) {
                panView(ScrollDirection.DOWN, hasSet);
            }
        }
    }

    public void panView(ScrollDirection direction, boolean additiveVelocity) {
        switch (direction) {
            case LEFT:
                if (viewPort.x < bounds.x) {
                    return;
                }
                viewPort.setVelocity(-1, 0, scrollSpeed, additiveVelocity);
                return;
            case RIGHT:
                if ((viewPort.x + viewPort.width) > bounds.width) {
                    return;
                }
                viewPort.setVelocity(1, 0, scrollSpeed, additiveVelocity);
                return;
            case UP:
                if (viewPort.y < bounds.y) {
                    return;
                }
                viewPort.setVelocity(0, -1, scrollSpeed, additiveVelocity);
                return;
            case DOWN:
                if ((viewPort.y + viewPort.height) > bounds.height) {
                    return;
                }
                viewPort.setVelocity(0, 1, scrollSpeed, additiveVelocity);
        }
    }

    private void addVisibleEntity(Intersectable intersectable) {
        visibleEntities.add((Entity) intersectable);
    }

    public void draw(Graphics2D g2, int xOffset, int yOffset) {
        visibleEntities.stream().forEach((Entity entity) -> {
            entity.draw(g2, (int) (xOffset - viewPort.x),
                    (int) (yOffset - viewPort.y));
        });
    }

//===== Getters & Setters ====================================================//
    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void addEntities(Entity... entities) {
        this.entities.addAll(Arrays.asList(entities));
    }

    public void setIntersector(Intersector intersector) {
        this.intersector = intersector;
    }

    public Entity getViewEntity() {
        return viewPort;
    }

//============================================================================//
    private class ViewPort extends Entity {

        private final List<IntersectionHandler> intersectionHandlers = new ArrayList<>();

        public ViewPort(double x, double y, int width, int height) {
            super(x, y, width, height);
            drag = 0.5;
        }

        @Override
        public void processIntersectionWith(Intersectable intersectable) {
            intersectionHandlers.stream().forEach((intersectionHandler) -> {
                intersectionHandler.processIntersectionWith(intersectable);
            });
        }
    }

}

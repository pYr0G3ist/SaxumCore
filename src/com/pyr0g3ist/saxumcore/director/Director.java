package com.pyr0g3ist.saxumcore.director;

import com.pyr0g3ist.saxumcore.entity.Entity;
import com.pyr0g3ist.saxumcore.entity.EntityRegistrar;
import com.pyr0g3ist.saxumcore.input.InputHandler;
import com.pyr0g3ist.saxumcore.input.MouseProxy;
import com.pyr0g3ist.saxumcore.input.ScalingInputHandler;
import com.pyr0g3ist.saxumcore.intersect.Intersectable;
import com.pyr0g3ist.saxumcore.intersect.IntersectionHandler;
import com.pyr0g3ist.saxumcore.intersect.Intersector;
import com.pyr0g3ist.saxumcore.intersect.LinearEntityIntersector;
import com.pyr0g3ist.saxumcore.physics.MomentumIntersectionProcessor;
import com.pyr0g3ist.saxumcore.ui.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Director implements EntityRegistrar {

    private final Rectangle worldBounds;
    private final ViewPort viewPort;
    private final ScalingInputHandler inputHandler;

    private WorldSpaceInputHandler worldSpaceInputHandler;
    private LinearEntityIntersector intersector;
    private MouseProxy mouseProxy;

    private final List<Entity> entities = new ArrayList<>();
    private final List<Entity> visibleEntities = new ArrayList<>();
    private final List<Entity> newEntities = new ArrayList<>();

    private final List<Component> components = new ArrayList<>();

    public int scrollSpeed = 500;
    public boolean paused = false;

    public Director(int worldWidth, int worldHeight, Rectangle viewPortBounds, ScalingInputHandler inputHandler) {
        this.worldBounds = new Rectangle(worldWidth, worldHeight);
        this.viewPort = new ViewPort(
                viewPortBounds.x,
                viewPortBounds.y,
                viewPortBounds.width,
                viewPortBounds.height);
        this.inputHandler = inputHandler;

        entities.add(viewPort);
        viewPort.intersectionHandlers.add(this::addVisibleEntity);

        LinearEntityIntersector linearIntersector = new LinearEntityIntersector();
        linearIntersector.intersectionProcessors.add(new MomentumIntersectionProcessor());
        intersector = linearIntersector;
    }

    public void update(double deltaFraction) {
        checkInput();
//        Add all new Entities.
        entities.addAll(newEntities);
        newEntities.clear();
//        Let viewPort populate visible entities.
        visibleEntities.clear();
        viewPort.update(deltaFraction);
//        Intersections with mouseProxy.
        if (mouseProxy != null) {
            mouseProxy.setInputHandler(inputHandler);
            intersector.doIntersectWithList(mouseProxy, components, null);
            mouseProxy.setInputHandler(getWorldSpaceInputHandler());
            intersector.doIntersectWithList(mouseProxy, entities, null);
            mouseProxy.update(deltaFraction);
        }
//        Intersections between entities.
        intersector.processIntersections(entities);
//        Update entities.
        List<Entity> removeList = new ArrayList<>();
        entities.stream().forEach((entity) -> {
            if (entity.needsDisposal()) {
                removeList.add(entity);
            } else {
                if (!paused) {
                    entity.update(deltaFraction);
                }
            }
        });
        entities.removeAll(removeList);
//        Update components.
        components.stream().forEach((component) -> {
            component.update(deltaFraction);
        });
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
                if (viewPort.x < worldBounds.x) {
                    return;
                }
                viewPort.setVelocity(-1, 0, scrollSpeed, additiveVelocity);
                return;
            case RIGHT:
                if ((viewPort.x + viewPort.width) > worldBounds.width) {
                    return;
                }
                viewPort.setVelocity(1, 0, scrollSpeed, additiveVelocity);
                return;
            case UP:
                if (viewPort.y < worldBounds.y) {
                    return;
                }
                viewPort.setVelocity(0, -1, scrollSpeed, additiveVelocity);
                return;
            case DOWN:
                if ((viewPort.y + viewPort.height) > worldBounds.height) {
                    return;
                }
                viewPort.setVelocity(0, 1, scrollSpeed, additiveVelocity);
        }
    }

    private void addVisibleEntity(Intersectable intersectable) {
        if (intersectable instanceof Entity) {
            visibleEntities.add((Entity) intersectable);
        }
    }

    public void draw(Graphics2D g2, int xOffset, int yOffset) {
        visibleEntities.stream().forEach((Entity entity) -> {
            entity.draw(g2, (int) (xOffset - viewPort.x),
                    (int) (yOffset - viewPort.y));
        });
        components.stream().forEach((component) -> {
            component.draw(g2, xOffset, yOffset);
        });
    }

    @Override
    public void registerEntity(Entity entity) {
        newEntities.add(entity);
    }

    public void registerComponent(Component component) {
        components.add(component);
    }

// ===== Getters & Setters =====================================================
//    
    public void setMouseProxy(MouseProxy mouseProxy) {
        this.mouseProxy = mouseProxy;
    }

    public void setIntersector(LinearEntityIntersector intersector) {
        this.intersector = intersector;
    }

    public Entity getViewEntity() {
        return viewPort;
    }

    public Rectangle getBounds() {
        return worldBounds;
    }

    public InputHandler getWorldSpaceInputHandler() {
        if (worldSpaceInputHandler == null) {
            worldSpaceInputHandler = new WorldSpaceInputHandler(inputHandler);
        }
        return worldSpaceInputHandler;
    }

// ===== ViewPort ==============================================================
//    
    private class ViewPort extends Entity {

        private final List<IntersectionHandler> intersectionHandlers = new ArrayList<>();

        public ViewPort(double x, double y, int width, int height) {
            super(x, y, width, height);
            drag = 0.5;
        }

        @Override
        protected void processIntersections(Set<Intersectable> intersectables) {
            intersectables.stream()
                    .filter((intersectable) -> (!(intersectable instanceof Director)
                            && !(intersectable instanceof Director.ViewPort)))
                    .forEach((intersectable) -> {
                        intersectionHandlers.stream().forEach((intersectionHandler) -> {
                            intersectionHandler.processIntersectionWith(intersectable);
                        });
                    });
        }
    }
// ===== WorldSpaceInputHandler ================================================

    private class WorldSpaceInputHandler extends ScalingInputHandler {

        public WorldSpaceInputHandler(ScalingInputHandler sourceHandler) {
            super(inputHandler);
        }

        @Override
        public Point getMouse() {
            Point mouse = super.getMouse();
            mouse.x += viewPort.x;
            mouse.y += viewPort.y;
            return mouse;
        }

    }

}

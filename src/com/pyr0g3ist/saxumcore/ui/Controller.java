package com.pyr0g3ist.saxumcore.ui;

import com.pyr0g3ist.saxumcore.entity.ComponentManager;

public class Controller {

    private final ComponentManager componentManager;

    public Controller(ComponentManager componentManager) {
        this.componentManager = componentManager;
    }

    public void add(Component component) {
        if (componentManager != null) {
            componentManager.registerComponent(component);
        }
    }

    public void remove(Component component) {
        if (componentManager != null) {
            componentManager.removeComponent(component);
        }
    }

}

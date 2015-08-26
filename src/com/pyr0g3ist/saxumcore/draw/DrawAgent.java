package com.pyr0g3ist.saxumcore.draw;

import java.util.List;

public interface DrawAgent {

    public void draw(Drawable... drawables);

    public void draw(List<Drawable> drawables);

}

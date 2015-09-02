package com.pyr0g3ist.saxumcore.entity;

public interface Disposable {

    public boolean needsDisposal();

    public void disposeLater();

}

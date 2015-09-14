package com.pyr0g3ist.saxumcore.input;

public enum ClickIndex {

    UI_INDEX(2),
    BASE_INDEX(1),
    CLICK_THROUGH_INDEX(0);

    private final int clickCode;

    private ClickIndex(int clickCode) {
        this.clickCode = clickCode;
    }
    
    public boolean isHigherThan(ClickIndex clickIndex) {
        return this.clickCode > clickIndex.clickCode;
    }
}

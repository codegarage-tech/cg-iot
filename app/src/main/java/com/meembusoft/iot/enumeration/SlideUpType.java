package com.meembusoft.iot.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum SlideUpType {

    CHOOSE_DEVICE(1), CONNECT_DEVICE(2);

    private final int slideUpType;

    private SlideUpType(int value) {
        slideUpType = value;
    }

    public boolean equalsValue(int otherValue) {
        return slideUpType == otherValue;
    }

    public int getValue() {
        return this.slideUpType;
    }

    public static SlideUpType getSlideUpType(int value) {
        for (SlideUpType mSlideUpType : SlideUpType.values()) {
            if (mSlideUpType.getValue() == value) {
                return mSlideUpType;
            }
        }
        return null;
    }
}
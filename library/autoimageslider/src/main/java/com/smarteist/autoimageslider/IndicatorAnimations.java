package com.smarteist.autoimageslider;

import java.util.Random;

public enum IndicatorAnimations {
    WORM,
    THIN_WORM,
    COLOR,
    DROP,
    FILL,
    NONE,
    SCALE,
    SCALE_DOWN,
    SLIDE,
    SWAP;

    public static IndicatorAnimations getRandomIndicatorAnimation() {
        Random random = new Random();
        int x = random.nextInt(IndicatorAnimations.class.getEnumConstants().length);
        return IndicatorAnimations.class.getEnumConstants()[x];
    }
}

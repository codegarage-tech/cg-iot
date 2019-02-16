package tech.codegarage.iot.enumeration;

import tech.codegarage.iot.BuildConfig;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum FlavorType {

    USER("user");

    private final String flavorType;

    private FlavorType(String value) {
        flavorType = value;
    }

    public boolean equalsName(String otherName) {
        return flavorType.equals(otherName);
    }

    public String toString() {
        return this.flavorType;
    }

    public static FlavorType getFlavor() {
        for (FlavorType flavorType : FlavorType.values()) {
            if (flavorType.toString().equalsIgnoreCase(BuildConfig.FLAVOR_appType)) {
                return flavorType;
            }
        }
        return null;
    }
}
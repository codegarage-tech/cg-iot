package com.meembusoft.iot.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum DeviceChooseType {

    MANUAL("Manual"),
    SCAN("Scan");

    private final String chooseType;

    private DeviceChooseType(String value) {
        chooseType = value;
    }

    public boolean equalsName(String otherName) {
        return chooseType.equals(otherName);
    }

    public String getValue() {
        return this.chooseType;
    }

    public static DeviceChooseType getDeviceChooseType(String value) {
        for (DeviceChooseType deviceChooseType : DeviceChooseType.values()) {
            if (deviceChooseType.getValue().equalsIgnoreCase(value)) {
                return deviceChooseType;
            }
        }
        return null;
    }
}
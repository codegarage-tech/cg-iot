package com.meembusoft.iot.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum DeviceType {

    WIFI_MOTOR_ONE("wifi_motor_one"),
    WIFI_MOTOR_TWO("wifi_motor_two"),
    WIFI_GANG_BOARD_FIVE("wifi_gang_board_five"),
    WIFI_2_PIN_SOCKET_ONE("wifi_2_pin_socket_one"),
    WIFI_HOLDER_PIN("wifi_holder_pin"),
    WIFI_HOLDER_PATCH("wifi_holder_patch");

    private final String deviceType;

    private DeviceType(String value) {
        deviceType = value;
    }

    public boolean equalsName(String otherName) {
        return deviceType.equals(otherName);
    }

    public String getValue() {
        return this.deviceType;
    }

    public static DeviceType getDeviceType(String value) {
        for (DeviceType deviceType : DeviceType.values()) {
            if (deviceType.getValue().equalsIgnoreCase(value)) {
                return deviceType;
            }
        }
        return null;
    }
}
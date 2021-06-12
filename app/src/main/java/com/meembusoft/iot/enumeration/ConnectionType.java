package com.meembusoft.iot.enumeration;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum ConnectionType {

    BLUETOOTH("Bluetooth"),
    WIFI("Wifi"),
    MOBILE_DATA("Mobile Data");

    private final String connectionType;

    private ConnectionType(String value) {
        connectionType = value;
    }

    public boolean equalsName(String otherName) {
        return connectionType.equals(otherName);
    }

    public String getValue() {
        return this.connectionType;
    }

    public static ConnectionType getConnectionType(String value) {
        for (ConnectionType mConnectionType : ConnectionType.values()) {
            if (mConnectionType.getValue().equalsIgnoreCase(value)) {
                return mConnectionType;
            }
        }
        return null;
    }
}
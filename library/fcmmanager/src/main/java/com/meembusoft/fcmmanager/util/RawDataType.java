package com.meembusoft.fcmmanager.util;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public enum RawDataType {

    PRODUCT_DETAIL("product"),
    ORDER_DETAIL("order"),
    ANNOUNCEMENT_DETAIL("announcement");

    private final String detailType;

    private RawDataType(String value) {
        detailType = value;
    }

    public boolean equalsName(String otherName) {
        return detailType.equals(otherName);
    }

    public String getValue() {
        return this.detailType;
    }

    public static RawDataType getDetailType(String value) {
        for (RawDataType mDetailType : RawDataType.values()) {
            if (mDetailType.getValue().equalsIgnoreCase(value)) {
                return mDetailType;
            }
        }
        return null;
    }
}
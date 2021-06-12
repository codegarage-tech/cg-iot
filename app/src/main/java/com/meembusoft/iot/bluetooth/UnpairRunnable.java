package com.meembusoft.iot.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.lang.reflect.Method;

/**
 * The Class is used to paired the selected device. It should be done before
 * starting making connection. So, since the process may take time to be
 * done, it is carried out by a new thread and a delay is imposed for these
 * purpose.
 *
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class UnpairRunnable implements Runnable {

    private BluetoothDevice myDevice;

    private UnpairRunnable() {
    }

    public UnpairRunnable(BluetoothDevice device) {
        myDevice = device;
    }

    @Override
    public void run() {
        unpairDevice(myDevice);
    }

    /**
     * The method is used to unpaired the selected already-paired device
     */
    public static void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
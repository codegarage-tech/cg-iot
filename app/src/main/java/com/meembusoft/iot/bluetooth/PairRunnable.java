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
public class PairRunnable implements Runnable {

    private BluetoothDevice myDevice;

    private PairRunnable() {
    }

    public PairRunnable(BluetoothDevice device) {
        myDevice = device;
    }

    @Override
    public void run() {
        pairDevice(myDevice);
    }

    /**
     * The method is used to paired the selected device. It should be done before
     * starting making connection. So, since the process may take time to be
     * done, it is carried out by a new thread and a delay is imposed for these
     * purpose.
     */
    public static void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
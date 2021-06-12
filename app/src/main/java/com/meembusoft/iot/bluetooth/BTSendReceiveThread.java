package com.meembusoft.iot.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import com.meembusoft.iot.util.Logger;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The class used to send and receive data from a device (uC) via Bluetooth. The
 * connection between device and MC was created before.
 *
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class BTSendReceiveThread extends Thread {

    private BluetoothSocket mmSocket;
    private InputStream mmInStream;
    private OutputStream mmOutStream;
    public static final int READ_SUCCESS = 1;
    private Handler mHandler;
    private String TAG = BTSendReceiveThread.class.getSimpleName();

    public BTSendReceiveThread(BluetoothSocket socket, Handler handler) {

        mHandler = handler;
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        /* Get the input and output streams, using "tmp" objects because member streams are final */
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (Exception e) {
            Logger.d(TAG, "BTSendReceiveThread()>>Exception: " + e.getMessage());
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {

        byte[] buffer = new byte[1024]; // buffer store for the stream
        int begin = 0;
        int bytes = 0;

        /* Keep listening to the InputStream until an exception occurs. */
        while (true) {
            try {
                /* Read number of array from input buffer */
                bytes = mmInStream.read(buffer);
                for (int i = begin; i <= bytes; i++) {

                    /* The data sent from uC ends with # sign */
                    if (buffer[i] == "#".getBytes()[0] || i == bytes) {

                        /* Send the obtained bytes to the UI activity */
                        mHandler.obtainMessage(READ_SUCCESS, begin, i, buffer).sendToTarget();
                        begin = i + 1;
                        if (i == bytes - 1) {
                            bytes = 0;
                            begin = 0;
                        }
                    }
                }
            } catch (Exception e) {
                Logger.d(TAG, "run()>>Exception: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * @data Send
     * @brief Call this from the main activity to send string data to the remote device.
     */
    public void Send(String data) {
        try {
            mmOutStream.write(data.getBytes());
            for (int i = 0; i < data.getBytes().length; i++)
                Logger.d(TAG, "outStream>>" + Integer.toString(i) + ">>" + Character.toString((char) (Integer.parseInt(Byte.toString(data.getBytes()[i])))));
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Logger.d(TAG, "Send()>>Exception: " + e.getMessage());
        }
    }

    /**
     * @bytes Send
     * @brief Call this from the main activity to send byte to the remote device.
     */
    public void Send(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Logger.d(TAG, "Send()>>Exception: " + e.getMessage());
        }
    }

    /**
     * @brief Call this from the main activity to shutdown the connection.
     */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (Exception e) {
            Logger.d(TAG, "cancel()>>Exception: " + e.getMessage());
        }
    }
}
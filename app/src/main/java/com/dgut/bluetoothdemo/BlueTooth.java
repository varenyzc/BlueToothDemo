package com.dgut.bluetoothdemo;

import android.content.Context;
import android.os.Bundle;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BlueTooth {
    private static final int MSG_CLIENT_READ = 1;
    private static final int CONNECT_SUCCESS = 2;
    private final Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private static final UUID SPP_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    public ConnectedThread connectedThread;

    public BlueTooth(Context context, Handler handler) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = handler;
    }


    public void connect(BluetoothDevice bluetoothDevice) {
        ConnectThread connectThread = new ConnectThread(bluetoothDevice);
        connectThread.start();
    }


    public class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(SPP_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {


            mBluetoothAdapter.cancelDiscovery();

            try {

                mmSocket.connect();
            } catch (IOException connectException) {

                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }
            Message msg = new Message();
            Bundle deviceName = new Bundle();
            deviceName.putString("device_name",mmDevice.getName());
            msg.setData(deviceName);
            msg.what = CONNECT_SUCCESS;
            mHandler.sendMessage(msg);
            manageConnectedSocket(mmSocket);

        }


        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }


    public void manageConnectedSocket(BluetoothSocket mmSocket) {
        connectedThread = new ConnectedThread(mmSocket);
        connectedThread.start();
    }


    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;


            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {

            byte[] buffer = new byte[1];
            int bytes;
            String content = new String();
            while (true) {
                try {

                    bytes = mmInStream.read(buffer);
                    String temp=new String(buffer,0,bytes);
                    content+=temp;
                    if (content.endsWith("\r\n")) {
                        content=content.replace("\r\n","");
                        /*
                        mHandler.obtainMessage(MSG_CLIENT_READ, content.length(), -1, content)
                                .sendToTarget();
                        */

                        Message msg = new Message();
                        Bundle data=new Bundle();
                        data.putString("BTdata",content);
                        msg.what=MSG_CLIENT_READ;
                        msg.setData(data);
                        mHandler.sendMessage(msg);


                        content="";
                    }

                } catch (IOException e) {
                    break;
                }
            }


        }

        public void write(String data){
            if (data ==null){
                return;
            }
            try {

                mmOutStream.write(data.getBytes("utf-8"));
            }catch (IOException e){
                e.printStackTrace();
            }
        }


        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }
}

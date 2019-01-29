package com.bannuranurag.android.augv.SharingDataLocation.Bluetooth;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnectionServ";
    private static final String appname="AUGV";

    private static final java.util.UUID UUID = java.util.UUID.fromString("8bb3d3de-fd78-4cf3-b3c5-ae09518d7dba");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;
    private AcceptThread mInsecureAcceptthread;
    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private java.util.UUID deviceUUID;
    ProgressDialog mProgressDialog;
    private ConnectedThread mConnectedthread;

    public BluetoothConnectionService(Context context) {
        mContext=context;
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    }


    //This thread runs while listening for incoming connections. It is like a server-side client. It run until a connection is accepted or cancelled
    private class AcceptThread extends Thread{
        //local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            //Create a new listening server socket
            try {
                tmp= mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appname,UUID);
                Log.e(TAG,"Accept thread exception"+UUID);
            } catch (IOException e) {
                Log.e(TAG,"Accept thread exception"+e);
            }

            Log.d(TAG,"Setting up the server using: "+ UUID);
            mmServerSocket=tmp;
        }

        public void run(){
            Log.d(TAG,"run:Accept thread running");
            BluetoothSocket socket = null;
            try {
                Log.d(TAG,"run: RFCOM server socket start ...");
                //This is a blocking call and will return only on a successful connection or an exception
                socket=mmServerSocket.accept();
                Log.d(TAG,"Server socket accepted connection");
            }
            catch (IOException e){
                Log.e(TAG,"Accept thread: Io exception"+e.getMessage());
            }

            if(socket!=null){
                connected(socket,mmDevice);
            }

            Log.i(TAG,"End accept thread");

        }

        public void cancel(){
            try{
                mmServerSocket.close();
            }
            catch (IOException e){
                Log.d(TAG,"Cancel: Close accept thread failed"+e);
            }
        }
    }

    private class ConnectThread extends Thread{
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            Log.d(TAG,"Connect Thread : Started");
            mmDevice=device;
            deviceUUID=uuid;
        }

        public void run(){
            BluetoothSocket tmp= null;
            Log.i(TAG,"RUN mConnectThread");

            //Get a Bluetooth Socket for a connection with the given BT device

            try {
                Log.d(TAG,"Connect Thread trying to create insecureRFCOMM socket using UUID");
                tmp= mmDevice.createInsecureRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.d(TAG,"ConnectThread: Could not create insecure RFCOMM"+e);
                e.printStackTrace();
            }
            mmSocket=tmp;

            //Cancel Discovery after connection made
            mBluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
                Log.d(TAG,"COnnect Thread : Connection Successfull");
            } catch (IOException e) {
                try {
                    mmSocket.close();
                    Log.d(TAG,"run: Close Socket");
                }
                catch (IOException i){
                    Log.e(TAG,"Unable to close connection in connect thread"+i);
                }
                Log.d(TAG,"Connect Thread: Could not make connection");
            }
            connected(mmSocket,mmDevice);
        }
        public void cancel(){
            try{
                mmSocket.close();
                Log.d(TAG,"Cancel: Closing client socket");
            }
            catch (IOException e){
                Log.e(TAG,"Cancel: Close Connect thread failed"+e);
            }
        }
    }


    //Initiate start thread
    public  synchronized void start(){
        Log.d(TAG,"Start");

        //Cancel threads already attempting to make connection

        if(mConnectThread!=null){
            mConnectThread.cancel();
            mConnectThread=null;
        }
        if(mInsecureAcceptthread==null){
            mInsecureAcceptthread= new AcceptThread();
            mInsecureAcceptthread.start();
        }
    }

    //This initiates the coonect thread

    public void startClient(BluetoothDevice device, UUID uuid){
        Log.d(TAG,"StartClient: Started");

        //iniprocess dialog
        mProgressDialog= ProgressDialog.show(mContext,"Connecting Bluetooth","Please wait",true);

        mConnectThread= new ConnectThread(device,uuid);
        mConnectThread.start();
    }

    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInputStream;
        private final OutputStream mmOutputStream;

        public ConnectedThread(BluetoothSocket socket){
            Log.d(TAG,"connected thread: Starting");

            mmSocket=socket;
            InputStream tmpIn=null;
            OutputStream tmpOut=null;

            //dismiss the dialog box as conndection is succesfull
            mProgressDialog.dismiss();
            try {
                tmpIn=mmSocket.getInputStream();
                tmpOut=mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmInputStream=tmpIn;
            mmOutputStream=tmpOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while(true){
                try{
                    bytes= mmInputStream.read(buffer);
                    String incomingMessages= new String(buffer,0,bytes);
                    Log.d(TAG,"InputStream:"+incomingMessages);
                }catch (IOException e){
                    Log.e(TAG,"Earror reading input stream"+e);
                    e.printStackTrace();
                    break;
                }
            }
        }

        //Call this to send data to the remote device
        public void write(byte[] bytes){
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG,"Writing to outputstream"+text);

            try {
                mmOutputStream.write(bytes);
            }catch (IOException e){
                e.printStackTrace();
                Log.e(TAG,"Write: Could not write to outputStream"+e);
            }
        }

        public void cancel(){
            try{
                mmSocket.close();
            }catch (IOException e){

            }
        }
    }
    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG,"connected: Starting ");

        //Start the thread to manage the connection and perform transmissions
        mConnectedthread=new ConnectedThread(mmSocket);
        mConnectedthread.start();

    }

    public void write(byte[] out){
        Log.d(TAG,"write : Write called");
        mConnectedthread.write(out);
    }
}

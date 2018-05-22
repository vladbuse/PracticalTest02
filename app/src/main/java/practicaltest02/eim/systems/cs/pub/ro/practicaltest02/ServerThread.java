package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.provider.SyncStateContract;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.client.ClientProtocolException;

/**
 * Created by vladb on 5/22/2018.
 */

public class ServerThread extends Thread
{
    ServerSocket serverSocket;
    HashMap<String,ArrayList<String>> data;


    ServerThread(int port)
    {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ioException) {
            Log.e("Tag", "An exception has occurred: " + ioException.getMessage());

        }
        data = new HashMap<String,ArrayList<String>>();
    }

    public synchronized void setData(String city, ArrayList<String> response) {
        this.data.put(city,response);
    }

    public synchronized HashMap<String,ArrayList<String>> getData(String city, String response) {
        return this.data;
    }


    public ServerSocket getServerSocket()
    {
        return serverSocket;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i("TAG", "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i("TAG", "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }
        } catch (ClientProtocolException clientProtocolException) {
            Log.e("TAG", "[SERVER THREAD] An exception has occurred: " + clientProtocolException.getMessage());

        } catch (IOException ioException) {
            Log.e("TAG", "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());

        }
    }

}

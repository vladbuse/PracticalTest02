package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


/**
 * Created by vladb on 5/22/2018.
 */

public class ClientThread extends Thread
{

    Socket socket;
    String address;
    int port;
    String city;
    TextView textView;

    public ClientThread( String address, int port, String city, TextView textView)
    {

        this.address = address;
        this.port = port;
        this.city = city;
        this.textView = textView;
    }

    public void run() {

        try {

            socket = new Socket(address, port);

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            printWriter.println(city);
            printWriter.flush();
            String weatherInformation;
            while ((weatherInformation = bufferedReader.readLine()) != null) {
                final String finalizedWeateherInformation = weatherInformation;
               textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(finalizedWeateherInformation);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e("TAG", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("TAG", "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());

                }
            }
        }
    }
    
    
}

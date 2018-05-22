package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by vladb on 5/22/2018.
 */



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

class Utilities {
    public static BufferedReader getReader(Socket socket) throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static PrintWriter getWriter(Socket socket) throws IOException {
        return new PrintWriter(socket.getOutputStream(), true);
    }
}


public class CommunicationThread extends Thread
{
    ServerThread serverThread;
    Socket socket;
    public CommunicationThread(ServerThread serverThread, Socket socket){
        this.serverThread = serverThread;
        this.socket = socket;

    }

    public void run() {
        if (socket == null) {
            Log.e("TAG", "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            String city = bufferedReader.readLine();

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://autocomplete.wunderground.com/aq?query="+city);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String pageSourceCode = httpClient.execute(httpPost, responseHandler);
            try {
                JSONObject content = new JSONObject(pageSourceCode);
                String t = "";
                JSONArray list = content.getJSONArray("RESULTS");
                for (int i = 0; i < list.length(); i++)
                {
                    JSONObject jsonobject = list.getJSONObject(i);
                    String name = jsonobject.getString("name");
                    t += name + " \n";
                }
                Log.e("TAG",t);
                printWriter.write(t);
                printWriter.flush();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //Log.e("TAG",t);

        } catch (IOException ioException) {
            Log.e("TAG", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("TAG", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());

                }
            }
        }
    }
}

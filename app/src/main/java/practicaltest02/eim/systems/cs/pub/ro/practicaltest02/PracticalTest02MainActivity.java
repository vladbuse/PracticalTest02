package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    Button startServer;
    Button getQuery;
    EditText serverPortEditText;
    EditText clientPortEditText;
    EditText query;
    TextView result;
    ServerThread serverThread;
    ClientThread clientThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        startServer = (Button) findViewById(R.id.connect_button);
        startServer.setOnClickListener(new ConnectButtonClickListener());

        getQuery = (Button) findViewById(R.id.go_client);
        getQuery.setOnClickListener(new ClientButtonClickListener());
        serverPortEditText = (EditText) findViewById(R.id.server_port);

        clientPortEditText = (EditText) findViewById(R.id.client_port);
        result = (TextView) findViewById(R.id.query_info);
        query = (EditText) findViewById(R.id.client_address);

    }


    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e("TAG", "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            Log.e("TAG", "[MAIN ACTIVITY] Server Started!");
            serverThread.start();
        }

    }

    private class ClientButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = "localhost";
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String city = query.getText().toString();
            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), city, result
            );
            clientThread.start();
        }

    }
}



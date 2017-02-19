package edu.buffalo.cse.cse486586.groupmessenger1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * GroupMessengerActivity is the main Activity for the assignment.
 *
 * @author stevko
 *
 */
public class GroupMessengerActivity extends Activity {
    private Uri mUri=Uri.parse("content://edu.buffalo.cse.cse486586.groupmessenger1.provider");
    static final String TAG = GroupMessengerActivity.class.getSimpleName();
    static final String REMOTE_PORT0 = "11108";
    static final String REMOTE_PORT1 = "11112";
    int incr=0;
    static final String REMOTE_PORT2 = "11116";
    static final String REMOTE_PORT3 = "11120";
    static final String REMOTE_PORT4 = "11124";
    static final int SERVER_PORT = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);

        TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        final String myPort = String.valueOf((Integer.parseInt(portStr) * 2));
        try {

            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
        } catch (IOException e) {
            Log.e(TAG, "Can't create a ServerSocket");
            return;
        }



        /*
         * TODO: Use the TextView to display your messages. Though there is no grading component
         * on how you display the messages, if you implement it, it'll make your debugging easier.
         */
        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());
        final EditText editText = (EditText) findViewById(R.id.editText1);

        /*
         * Registers OnPTestClickListener for "button1" in the layout, which is the "PTest" button.
         * OnPTestClickListener demonstrates how to access a ContentProvider.
         */
        findViewById(R.id.button1).setOnClickListener(
                new OnPTestClickListener(tv, getContentResolver()));


        final Button rightButton = (Button) findViewById(R.id.button4);
        rightButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                EditText editText = (EditText) findViewById(R.id.editText1);
                String msg = editText.getText().toString() + "\n";
                editText.setText(""); // This is one way to reset the input box.
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg, myPort);

                //perform action
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }


private class ServerTask extends AsyncTask<ServerSocket, String, Void> {

    @Override
    protected Void doInBackground(ServerSocket... sockets) {
        ServerSocket serverSocket = sockets[0];
        try{
            while(true) {
                Socket socket = serverSocket.accept();
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String inputMsg = in.readLine();
                publishProgress(inputMsg);
                socket.close();
            }
                /*
             * TODO: Fill in your server code that receives messages and passes them
             * to onProgressUpdate().
             */}
        catch(Exception e){
            e.printStackTrace();
        }


        return null;

    }

    protected void onProgressUpdate(String...strings) {
            /*
             * The following code displays what is received in doInBackground().
             */
        String strReceived = strings[0].trim();
        TextView localTextView = (TextView) findViewById(R.id.textView1);
        localTextView.append(strReceived+"\n");

        ContentValues keyValueToInsert = new ContentValues();

        keyValueToInsert.put("key", String.valueOf(incr));
        keyValueToInsert.put("value", strReceived);

        incr=incr+1;

        Uri newUri = getContentResolver().insert(
                mUri,    // assume we already created a Uri object with our provider URI
                keyValueToInsert
        );


            /*
             * The following code creates a file in the AVD's internal storage and stores a file.
             ******************************************************************************
             * For more information on file I/O on Android, please take a look at
             * http://developer.android.com/training/basics/data-storage/files.html
             */



        return;
    }
}

/***
 * ClientTask is an AsyncTask that should send a string over the network.
 * It is created by ClientTask.executeOnExecutor() call whenever OnKeyListener.onKey() detects
 * an enter key press event.
 *
 * @author Arbaaz
 *
 */
private class ClientTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... msgs) {
        try {
            String remotePort[] = {REMOTE_PORT0,REMOTE_PORT1,REMOTE_PORT2,REMOTE_PORT3,REMOTE_PORT4};

            String msgToSend = msgs[0];
            for (int i=0;i<5;i++) {

                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(remotePort[i]));



                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    out.print(msgToSend);
                    out.flush();
                /*
                 * TODO: Fill in your client code that sends out a message.
                 */
                    socket.close();
                }

        } catch (UnknownHostException e) {
            Log.e(TAG, "ClientTask UnknownHostException");
        } catch (IOException e) {
            Log.e(TAG, "ClientTask socket IOException");
        }

        return null;
    }
}
}

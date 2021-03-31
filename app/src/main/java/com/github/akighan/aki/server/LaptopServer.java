package com.github.akighan.aki.server;

import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.github.akighan.aki.MainActivity;
import com.github.akighan.aki.MainFragment;
import com.github.akighan.aki.Note;
import com.github.akighan.aki.database.DBSingleton;
import com.github.akighan.aki.database.NotesReceiver;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class LaptopServer {
    private static final String LOG_TAG = "Aki";
    private String mServerName = "192.168.1.44";
    private int mServerPort = 6789;
    private Socket mSocket = null;
    private DBSingleton dbSingleton = DBSingleton.getInstance();

    public LaptopServer() {
    }

    public void openConnection () throws Exception {
        closeConnection();
        try {
            mSocket = new Socket(mServerName,mServerPort);
        } catch (IOException e) {
            throw new Exception("Невозможно создать соединение" + e.getMessage());
        }
    }

    public void closeConnection () {
        if (mSocket != null && !mSocket.isClosed()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e (LOG_TAG, "Невозможно закрыть сокет:" + e.getMessage());
            } finally {
                mSocket = null;
            }
        }
        mSocket = null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        closeConnection();
    }

    public void sendData (String clientId) throws Exception {
        openConnection();
        if (mSocket == null || mSocket.isClosed()) {
            throw new Exception("Невозможно отправить данные. Сокет не создан или закрыт");
        }

        try {
            List<Note> sendList = NotesReceiver.getInstance().getNotes();
            StringBuilder packedCollection = new StringBuilder();
            packedCollection.append(clientId).append('\n');
            for (int i = 0; i<sendList.size();i++) {
                packedCollection.append(sendList.get(i).getNote()).append('\n');
            }
            mSocket.getOutputStream().write(packedCollection.toString().getBytes());
            mSocket.getOutputStream().flush();
        } catch (IOException e){
            throw new Exception("Невозможно отправить данные" + e.getMessage());
        }

    }
}

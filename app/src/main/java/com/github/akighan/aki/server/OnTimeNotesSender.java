package com.github.akighan.aki.server;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.github.akighan.aki.fragments.SettingsFragment;
import com.github.akighan.aki.notes.NotesReceiver;

import java.util.TimerTask;

public class OnTimeNotesSender extends TimerTask {
    private SharedPreferences sharedPreferences;
    private NotesReceiver notesReceiver;
    private LaptopServer laptopServer;

    public OnTimeNotesSender(Activity activity) {
        sharedPreferences = activity.getSharedPreferences(SettingsFragment.APP_PREFERENCES, Context.MODE_PRIVATE);
        notesReceiver = NotesReceiver.getInstance();
        laptopServer = new LaptopServer(activity);
    }

    @Override
    public void run() {
        boolean isTelegramChecked = sharedPreferences.getBoolean(SettingsFragment.APP_TELEGRAM_ASSISTANT, false);
        System.out.println("i was created");
        if (isTelegramChecked && notesReceiver.isNeedToSynchroniseWithServer()) {
            System.out.println("i'm in");
            try {
                laptopServer.sendData();
                notesReceiver.setNeedToSynchroniseWithServer(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

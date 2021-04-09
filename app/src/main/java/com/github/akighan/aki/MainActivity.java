package com.github.akighan.aki;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.akighan.aki.database.NotesReceiver;
import com.github.akighan.aki.server.LaptopServer;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LaptopServer server;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        server = new LaptopServer();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavController navController = Navigation.findNavController(this, R.id.navFragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    protected void onPause() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String ANDROID_ID = Settings.Secure
                            .getString(MainActivity.this.getApplicationContext().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                    server.sendData(ANDROID_ID);
                    server.closeConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        NotesReceiver.getInstance().updateDBFromReceiver();
        super.onPause();
    }
}
package com.github.akighan.aki;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.github.akighan.aki.notes.NotesReceiver;
import com.github.akighan.aki.server.LaptopServer;
import com.github.akighan.aki.server.OnTimeNotesSender;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timer onTimeNotesSender = new Timer();
        onTimeNotesSender.schedule(new OnTimeNotesSender(this), 10000, 10000);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavController navController = Navigation.findNavController(this, R.id.navFragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    @Override
    protected void onPause() {
        NotesReceiver.getInstance().updateDBFromReceiver();
        super.onPause();
    }
}
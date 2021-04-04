package com.github.akighan.aki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.akighan.aki.server.LaptopServer;


public class SettingsFragment extends Fragment {
    private Switch telegramOnSwitch;
    private Switch weatherNotification;
    private Spinner citySpinner;
    private Button saveButton;
    private LinearLayout weatherNotificationContainer;
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_TELEGRAM_ASSISTANT = "TELEGRAM_ASSISTANT";
    public static final String APP_WEATHER_NOTIFICATION = "WEATHER_NOTIFICATION";
    public static final String APP_CITY_FOR_WEATHER = "CITY_FOR_WEATHER";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        telegramOnSwitch = view.findViewById(R.id.telegram_on_button);
        weatherNotification = view.findViewById(R.id.sw_weather_notification);
        citySpinner = view.findViewById(R.id.sp_choose_city);
        weatherNotificationContainer = view.findViewById(R.id.weather_notification_layout);
        saveButton = view.findViewById(R.id.bt_save_changes);

        loadPreferences();

        onClickListener(saveButton);
        onCheckedChangeListener(telegramOnSwitch);
        onCheckedChangeListener(weatherNotification);
        setOnItemSelectedListener(citySpinner);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void onClickListener(Button saveButton) {
        saveButton.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = SettingsFragment.this.getActivity()
                    .getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            boolean hasVisited = sharedPreferences.getBoolean("hasVisited", false);
            final String ANDROID_ID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            if (!hasVisited) {
                sharedPreferences.edit().putBoolean("hasVisited", true).apply();
                new Thread(() -> savePreferences(ANDROID_ID)).start();
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/AkighanBot?start=" + ANDROID_ID));
                startActivity(browserIntent);
            } else {
                new Thread(() -> savePreferences(ANDROID_ID)).start();

                NavHostFragment.findNavController(this).navigate(R.id.action_settingsFragment_to_mainFragment);
            }
        });

    }

    private void savePreferences(String clientId) {
        SharedPreferences sharedPreferences = SettingsFragment.this.getActivity()
                .getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_TELEGRAM_ASSISTANT, telegramOnSwitch.isChecked())
        .putBoolean(APP_WEATHER_NOTIFICATION, weatherNotification.isChecked())
        .putInt(APP_CITY_FOR_WEATHER,citySpinner.getSelectedItemPosition()).apply();
        LaptopServer laptopServer = new LaptopServer();

        try {
            laptopServer.sendSettings(clientId,sharedPreferences);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOnItemSelectedListener(Spinner citySpinner) {
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int num =0; //this need to avoid SharedPreferences load
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                num++;
                if (num>1) {
                    activateSaveButton();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    private void onCheckedChangeListener(Switch sw) {
        if (sw.getId() == telegramOnSwitch.getId()) {
            sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
                activateSaveButton();
                if (isChecked) {
                    weatherNotificationContainer.setVisibility(View.VISIBLE);
                    if (weatherNotification.isChecked()) {
                        citySpinner.setVisibility(View.VISIBLE);
                    }
                } else {
                    weatherNotificationContainer.setVisibility(View.INVISIBLE);
                    citySpinner.setVisibility(View.INVISIBLE);
                }
            });
        } else if (sw.getId() == weatherNotification.getId()) {
            sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
                activateSaveButton();
                if (isChecked) {
                    citySpinner.setVisibility(View.VISIBLE);
                } else citySpinner.setVisibility(View.INVISIBLE);
            });
        }
    }

    private void loadPreferences() {
        boolean isTelegramChecked;
        boolean isWeatherNotificationChecked = false;
        int cityChosen;
        SharedPreferences sharedPreferences = SettingsFragment.this.getActivity()
                .getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        isTelegramChecked = sharedPreferences.getBoolean(APP_TELEGRAM_ASSISTANT, false);
        telegramOnSwitch.setChecked(isTelegramChecked);
        if (isTelegramChecked) {
            isWeatherNotificationChecked = sharedPreferences.getBoolean(APP_WEATHER_NOTIFICATION, false);
            weatherNotification.setChecked(isWeatherNotificationChecked);
            weatherNotificationContainer.setVisibility(View.VISIBLE);
        }
        if (isWeatherNotificationChecked) {
            cityChosen = sharedPreferences.getInt(APP_CITY_FOR_WEATHER, 0);
            citySpinner.setSelection(cityChosen);
            citySpinner.setVisibility(View.VISIBLE);
        }
    }

    private void activateSaveButton() {
        saveButton.setEnabled(true);
        saveButton.setClickable(true);
        saveButton.setVisibility(View.VISIBLE);
    }


    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }
}
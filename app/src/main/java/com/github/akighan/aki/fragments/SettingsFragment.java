package com.github.akighan.aki.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.akighan.aki.R;
import com.github.akighan.aki.notes.NotesReceiver;
import com.github.akighan.aki.server.LaptopServer;


public class SettingsFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private String ANDROID_ID;
    private boolean isClientThereFirstTime;
    private Switch telegramOnSwitch;
    private Switch weatherNotification;
    private Switch newsNotification;
    private Spinner citySpinner;
    private Button saveButton;
    private Button welcomeButton;
    private LinearLayout weatherNotificationContainer;
    private LinearLayout newsNotificationContainer;
    public static final String APP_PREFERENCES = "settings";
    public static final String APP_TELEGRAM_ASSISTANT = "TELEGRAM_ASSISTANT";
    public static final String APP_WEATHER_NOTIFICATION = "WEATHER_NOTIFICATION";
    public static final String APP_NEWS_NOTIFICATION = "NEWS_NOTIFICATION";
    public static final String APP_CITY_FOR_WEATHER = "CITY_FOR_WEATHER";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ANDROID_ID = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        telegramOnSwitch = view.findViewById(R.id.telegram_on_button);
        weatherNotification = view.findViewById(R.id.sw_weather_notification);
        newsNotification = view.findViewById(R.id.sw_news_notification);
        citySpinner = view.findViewById(R.id.sp_choose_city);
        weatherNotificationContainer = view.findViewById(R.id.weather_notification_layout);
        newsNotificationContainer = view.findViewById(R.id.news_notification_layout);

        saveButton = view.findViewById(R.id.bt_save_changes);
        welcomeButton = view.findViewById(R.id.bt_equaint_with_telegram);

        loadPreferences();

        onClickListener(welcomeButton);
        onClickListener(saveButton);
        onCheckedChangeListener(telegramOnSwitch);
        onCheckedChangeListener(newsNotification);
        onCheckedChangeListener(weatherNotification);
        setOnItemSelectedListener(citySpinner);

        return view;
    }

    @Override
    public void onResume() {
        Toast.makeText(getContext(), "I'm here", Toast.LENGTH_SHORT).show();
        loadPreferences();
        if (!isClientThereFirstTime) {
            TextView welcomeText = this.getView().findViewById(R.id.welcome_text);
            LinearLayout telegramNotificationLayout = this.getView().findViewById(R.id.telegram_notification_layout);
            welcomeButton.setVisibility(View.GONE);
            welcomeText.setVisibility(View.GONE);
            telegramNotificationLayout.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void onClickListener(Button button) {
        if (button.getId() == saveButton.getId()) {
            saveButton.setOnClickListener(view -> {
                new Thread(this::savePreferences).start();
                NavHostFragment.findNavController(this).navigate(R.id.action_settingsFragment_to_mainFragment);
            });
        } else if (button.getId() == welcomeButton.getId()) {
            welcomeButton.setOnClickListener(view -> {
                sharedPreferences.edit().putBoolean("isClientThereFirstTime", false).apply();
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/AkighanBot?start=" + ANDROID_ID));
                startActivity(browserIntent);
            });
        }
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_TELEGRAM_ASSISTANT, telegramOnSwitch.isChecked())
                .putBoolean(APP_NEWS_NOTIFICATION, newsNotification.isChecked())
                .putInt(APP_CITY_FOR_WEATHER, citySpinner.getSelectedItemPosition());;

        if (citySpinner.getSelectedItemPosition() == 0) {
            editor.putBoolean(APP_WEATHER_NOTIFICATION, false);
        } else{
            editor.putBoolean(APP_WEATHER_NOTIFICATION, weatherNotification.isChecked());
        }
        editor.apply();

        LaptopServer laptopServer = new LaptopServer(getActivity());

        try {
            laptopServer.sendSettings(sharedPreferences);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOnItemSelectedListener(Spinner citySpinner) {
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int num = 0; //this need to avoid SharedPreferences load

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                num++;
                if (num > 1) {
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
                    NotesReceiver.getInstance().setNeedToSynchroniseWithServer(true);

                    newsNotificationContainer.setVisibility(View.VISIBLE);
                    weatherNotificationContainer.setVisibility(View.VISIBLE);
                    if (weatherNotification.isChecked()) {
                        citySpinner.setVisibility(View.VISIBLE);
                    }
                } else {
                    weatherNotificationContainer.setVisibility(View.INVISIBLE);
                    newsNotificationContainer.setVisibility(View.INVISIBLE);
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
        } else if (sw.getId() == newsNotification.getId()) {
            sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
                activateSaveButton();
            });
        }
    }

    private void loadPreferences() {
        boolean isTelegramChecked;
        boolean isWeatherNotificationChecked = false;
        boolean isNewsNotificationChecked = false;
        int cityChosen;

        sharedPreferences = SettingsFragment.this.getActivity()
                .getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        isClientThereFirstTime = sharedPreferences.getBoolean("isClientThereFirstTime", true);
        isTelegramChecked = sharedPreferences.getBoolean(APP_TELEGRAM_ASSISTANT, false);
        telegramOnSwitch.setChecked(isTelegramChecked);
        if (isTelegramChecked) {
            isNewsNotificationChecked = sharedPreferences.getBoolean(APP_NEWS_NOTIFICATION, false);
            newsNotification.setChecked(isNewsNotificationChecked);
            newsNotificationContainer.setVisibility(View.VISIBLE);

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
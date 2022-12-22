package com.example.baseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

interface TimeListener {
    void showTime(String formattedDate);
}

public class BaseActivity extends AppCompatActivity {
    private final static String TAG = "BaseActivity";
    public final static String URL = "https://api.ipgeolocation.io/ipgeo?apiKey=b03018f75ed94023a005637878ec0977";

    protected String formattedTime;
    protected Date currentTime;

    private OkHttpClient client = new OkHttpClient();

    private List<TimeListener> listeners = new ArrayList<TimeListener>();
    public void addListener(TimeListener toAdd) {
        listeners.add(toAdd);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initTime() {
        getTime();
    }

    protected void getTime() {
        Request request = new Request.Builder().url(URL).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "getTime", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                parseResponse(response);
            }
        });
    }

    private void parseResponse(Response response) {
        Gson gson = new Gson();
        ResponseBody body = response.body();
        if (body == null) {
            return;
        }
        try {
            String string = body.string();
            Log.d(TAG, string);
            TimeResponse timeResponse = gson.fromJson(string, TimeResponse.class);
            String currentTimeVal = timeResponse.getTimeZone().getCurrentTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
            Date datetime = simpleDateFormat.parse(currentTimeVal);

            runOnUiThread(() -> showTime(datetime));
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    private void showTime(Date dateTime) {
        if (dateTime == null){
            return;
        }
        currentTime = dateTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm, EEEE", Locale.forLanguageTag("ru"));
        formattedTime = simpleDateFormat.format(dateTime);
        for (TimeListener hl : listeners)
            hl.showTime(formattedTime);
    }
}
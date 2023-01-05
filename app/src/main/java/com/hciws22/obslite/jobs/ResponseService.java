package com.hciws22.obslite.jobs;

import android.util.Log;
import androidx.annotation.NonNull;
import com.hciws22.obslite.enums.ContentTypeFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResponseService implements Callback {

    private final String SERVER_URI = "https://obs.fbi.h-da.de/obs/service.php?action=getPersPlanAbo&lfkey=a52b599e4590ef0dcebd9cb3cce4c069fb14723adb31161445ce8d31de3942cd998ef01c09f5ffac";
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private static final List<String> filteredList = new ArrayList<>();
    private static boolean lock = true;



    public void getDataFromObs() throws IOException {
        Request request = requestSpecification();
        client.newCall(request).enqueue(this);

    }

    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {

        e.printStackTrace();
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        lock = true;
        var bufferedReader = new BufferedReader(new InputStreamReader(response.body().source().inputStream()));

        Log.d(Thread.currentThread().getName() + ": Response Service", "Response code " + response.code());
        synchronized (filteredList) {
            filteredList.clear();
            bufferedReader
                    .lines()
                    .filter(ContentTypeFactory::isValid)
                    .forEach(filteredList::add);
            lock = false;
        }
        Log.d(Thread.currentThread().getName() + ": Received List", String.valueOf(filteredList.size()));
    }


    private Request requestSpecification(){
        return  new Request.Builder()
                .addHeader("accept", "*/*")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .url(SERVER_URI)
                .build();
    }

    public List<String> getFilteredList(){


        Log.d("ResponseService: ", "wait");
        while (lock){
                System.out.println("I'm waiting...");
        };

        Log.d("ResponseService: ", "not wait anymore");
        Log.d(Thread.currentThread().getName() + ": check for converting", String.valueOf(filteredList.size()));
        return filteredList;
    }

}
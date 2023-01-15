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

    private static final String WEB_ADDRESS = "https://obs.fbi.h-da.de/obs/";
    private static final String[] REQUEST_PARAM = {"action=", "lfkey="};
    private final String SERVER_URI = "https://obs.fbi.h-da.de/obs/service.php?action=getPersPlanAbo&lfkey=ebf5317528799bfe687bd3203b724586082bce6c920044f15a7184abcdb7cd6460d39333528ff026";
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private static final List<String> filteredList = new ArrayList<>();
    private static boolean lock = true;

    public static void releaseLock(){
        lock = false;
    }

    public static void setLock(){
        lock = true;
    }

    public void getDataFromObs(String obsLink) throws IOException {

        Request request = requestSpecification(obsLink);
        filteredList.clear();
        setLock();
        client.newCall(request).enqueue(this);
    }


    private Request requestSpecification(String url){
        return  new Request.Builder()
                .addHeader("accept", "*/*")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .url(url)
                .build();
    }



    @Override
    public void onFailure(@NonNull Call call, @NonNull IOException e) {
        releaseLock();
        call.cancel();
        Log.d("Response Error: ", "Something went wrong");
    }

    @Override
    public void onResponse(@NonNull Call call, @NonNull Response response){

        var bufferedReader = new BufferedReader(new InputStreamReader(response.body().source().inputStream()));

        Log.d(Thread.currentThread().getName() + ": Response Service", "Response code " + response.code());

        bufferedReader
                .lines()
                .filter(ContentTypeFactory::isValid)
                .forEach(filteredList::add);

        Log.d(Thread.currentThread().getName() + ": Received List", String.valueOf(filteredList.size()));

        releaseLock();
    }


    public List<String> getFilteredList(){

        while (lock){
            Log.d("ResponseService: ", "I'm waiting");
        }

        Log.d("ResponseService: ", "not wait anymore");
        Log.d(Thread.currentThread().getName() + ": check for converting", String.valueOf(filteredList.size()));
        return filteredList;
    }

    public boolean checkUrl(String url) {
        return url.contains(WEB_ADDRESS) && url.contains(REQUEST_PARAM[0]) && url.contains(REQUEST_PARAM[1]);
    }
}

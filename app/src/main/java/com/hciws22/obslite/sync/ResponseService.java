package com.hciws22.obslite.sync;

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

    private final String SERVER_URI = "https://obs.fbi.h-da.de/obs/service.php?action=getPersPlanAbo&lfkey=ebf5317528799bfe687bd3203b724586082bce6c920044f15a7184abcdb7cd6460d39333528ff026";
    private final OkHttpClient client = new OkHttpClient().newBuilder().build();
    private final List<String> filteredList = new ArrayList <>();


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
        var bufferedReader = new BufferedReader(new InputStreamReader(response.body().source().inputStream()));

        synchronized (this.filteredList) {
            filteredList.clear();
            bufferedReader
                    .lines()
                    .filter(ContentTypeFactory::isValid)
                    .forEach(filteredList::add);
        }
    }


    private Request requestSpecification(){
        return  new Request.Builder()
                .addHeader("accept", "*/*")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .url(SERVER_URI)
                .build();
    }

    public List<String> getFilteredList(){
        return filteredList;
    }





}
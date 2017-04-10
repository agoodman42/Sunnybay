package com.gts.aaron.sunnybay;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Aaron on 4/9/2017.
 */

public class AsyncDownloadForecastTask extends AsyncTask<String, Void, String> {
    //Needs to call asynchronously so we don't gum up the UI while we're waiting for the
    // data to load

    private final WeakReference<Context> contextRef;
    TestDataHolder testHolder = new TestDataHolder();
    public final String TEST_STRING = testHolder.getTestJsonString();
    public String mTotalUrl = TEST_STRING;

    //the answer to life, the universe and everything
    double lat = 42;
    double lon = 42;


    Response response = null;
    public AsyncResponse delegate = null;


    public AsyncDownloadForecastTask(Context context, String totalUrl) {
        contextRef = new WeakReference<Context>(context);
        mTotalUrl = totalUrl;
            }

    @Override
    protected String doInBackground(String... params) {

        /*Okhttp isn't the most efficient library, not by a long shot, but for ease of use and
        * maintainability, it's hard to beat. Volley would have been a lot more to implement,
        as would retrofit, and the functionality and performance they provided wasn't the priority
        in this MVP. I focuesd on ease of implementation and maintainability*/
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(mTotalUrl)
                .build();

        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
            }
}



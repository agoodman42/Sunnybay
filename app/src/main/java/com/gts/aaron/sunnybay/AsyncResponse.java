package com.gts.aaron.sunnybay;

/**
 * Created by Aaron on 4/9/2017.
 */

//Interface to get the result out of my AsyncDownloadForecastTask

public interface AsyncResponse {
    void processFinish(String output);
}

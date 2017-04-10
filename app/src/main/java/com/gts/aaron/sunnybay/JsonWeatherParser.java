package com.gts.aaron.sunnybay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aaron on 4/9/2017.
 */

    public class JsonWeatherParser {

    private static JSONObject getObject(String tagName, JSONObject jsonObject)  throws JSONException {
        JSONObject childObject = jsonObject.getJSONObject(tagName);
        return childObject;
    }


    private static String getString(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jsonObject) throws JSONException {
        return (float) jsonObject.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt(tagName);
    }

        public static Weather getWeather(String dataString) throws JSONException {
            Weather weather = new Weather();

            // Instantiating the JSONObject using passed string
            JSONObject jsonObject = new JSONObject(dataString);


            JSONObject coordObj = getObject("coord", jsonObject);


            JSONObject sysObj = getObject("sys", jsonObject);
//
            // get array of weather info
            JSONArray jsonArray = jsonObject.getJSONArray("weather");

            // We use only the first item in the array to grab the values we want and set the
            //member variables.
            JSONObject JSONWeather = jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
            weather.currentCondition.setDescr(getString("description", JSONWeather));
            weather.currentCondition.setCondition(getString("main", JSONWeather));
            weather.currentCondition.setIcon(getString("icon", JSONWeather));

            //main object has most of the info we need, currentCondition is the keys to
            //most of it.
            JSONObject mainObject = getObject("main", jsonObject);
            weather.currentCondition.setHumidity(getInt("humidity", mainObject));
            weather.currentCondition.setPressure(getFloat("pressure", mainObject));
            weather.temperature.setMaxTemp(getFloat("temp_max", mainObject));
            weather.temperature.setMinTemp(getFloat("temp_min", mainObject));
            weather.temperature.setTemp(getFloat("temp", mainObject));

            // Wind
            JSONObject windObject = getObject("wind", jsonObject);
            weather.wind.setSpeed(getFloat("speed", windObject));
            weather.wind.setDeg(getFloat("deg", windObject));

            // Clouds
            JSONObject cloudObject = getObject("clouds", jsonObject);
            weather.clouds.setPerc(getInt("all", cloudObject));


            return weather;
        }

}

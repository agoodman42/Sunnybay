package com.gts.aaron.sunnybay;

import android.widget.Toast;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Aaron on 4/9/2017.
 */

public class JsonWeatherParserTest{

    /*using demo data from the api site, I was tested that the proper values were being parsed,
    to make sure that my parser was ready for prime time once I hooked up the API.
     */


    TestDataHolder dataString = new TestDataHolder();
    JsonWeatherParser parser = new JsonWeatherParser();
    Weather testWeather;

    @Test
    public void JsonWeatherParserNotNull() throws Exception{

        assertNotNull(parser.getWeather(dataString.getTestJsonString()));
    }

    @Test
    public void JsonWeatherParserReturnsProperStrings() throws Exception{
        testWeather = parser.getWeather(dataString.getTestJsonString());

        assertTrue((    testWeather.currentCondition.getCondition()+ " " +
                        testWeather.currentCondition.getHumidity()+ " " +
                        testWeather.currentCondition.getDescr()+ " " +
                        testWeather.currentCondition.getIcon() + " "+
                        testWeather.currentCondition.getPressure() ),

                testWeather.currentCondition.getCondition().equals("Clear") &&
                testWeather.currentCondition.getHumidity() == 100 &&
                testWeather.currentCondition.getDescr().equals("clear sky") &&
                testWeather.currentCondition.getIcon().equals("01n") &&
                testWeather.currentCondition.getPressure() == 1013.75);

    }

}



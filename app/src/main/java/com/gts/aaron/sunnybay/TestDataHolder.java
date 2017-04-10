package com.gts.aaron.sunnybay;

/**
 * Created by Aaron on 4/9/2017.
 */


//I used this class to easily pass around the URL and large string for testing.
public class TestDataHolder {
    String TestURL;
    String TestJsonString;

    public TestDataHolder() {
        TestURL = "http://samples.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=b1b15e88fa797225412429c1c50c122a1";
        TestJsonString= "{\n" +
                "coord: {\n" +
                "lon: 139.01,\n" +
                "lat: 35.02\n" +
                "},\n" +
                "weather: [\n" +
                "{\n" +
                "id: 800,\n" +
                "main: \"Clear\",\n" +
                "description: \"clear sky\",\n" +
                "icon: \"01n\"\n" +
                "}\n" +
                "],\n" +
                "base: \"stations\",\n" +
                "main: {\n" +
                "temp: 285.514,\n" +
                "pressure: 1013.75,\n" +
                "humidity: 100,\n" +
                "temp_min: 285.514,\n" +
                "temp_max: 285.514,\n" +
                "sea_level: 1023.22,\n" +
                "grnd_level: 1013.75\n" +
                "},\n" +
                "wind: {\n" +
                "speed: 5.52,\n" +
                "deg: 311\n" +
                "},\n" +
                "clouds: {\n" +
                "all: 0\n" +
                "},\n" +
                "dt: 1485792967,\n" +
                "sys: {\n" +
                "message: 0.0025,\n" +
                "country: \"JP\",\n" +
                "sunrise: 1485726240,\n" +
                "sunset: 1485763863\n" +
                "},\n" +
                "id: 1907296,\n" +
                "name: \"Tawarano\",\n" +
                "cod: 200\n" +
                "}";
    }

    public String getTestURL() {
        return TestURL;
    }

    public String getTestJsonString() {
        return TestJsonString;
    }
}

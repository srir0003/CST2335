package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

public class WeatherForecast extends AppCompatActivity
{
    ProgressBar weatherPB;
    ImageView Weather;
    TextView currentTempTv;
    TextView minTempTv;
    TextView maxTempTv;
    TextView uvRateTV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ForecastQuery Query = new ForecastQuery();
        Query.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric ");

        Weather = findViewById(R.id.currentWeather);
        currentTempTv = findViewById(R.id.currentTemperature);
        minTempTv = findViewById(R.id.minTemperature);
        maxTempTv = findViewById(R.id.maxTemperature);
        uvRateTV = findViewById(R.id.uvRate);

        weatherPB = findViewById(R.id.weatherPB);
        weatherPB.setVisibility(View.VISIBLE);
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>
    {
        private String wind;
        private String UV;
        private String minTemp;
        private String maxTemp;
        private String currentTemp;
        Bitmap image;

        @Override
        protected String doInBackground(String... strings) {
            String returnStrForOnPostExecute = null;
            String forecastUrlStr = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
            String iconName = null;

            try {
                URL forecastUrl = new URL(forecastUrlStr);
                HttpURLConnection urlConnection = (HttpURLConnection) forecastUrl.openConnection();
                InputStream inputStreamForecast = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inputStreamForecast, "UTF-8");

                int EVENT_TYPE;
                while ((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT) {

                    // check if the tag's name is temperature
                    if (EVENT_TYPE == START_TAG) {
                        String tagName = xpp.getName();

                        switch (tagName) {
                            case "temperature":
                                currentTemp = xpp.getAttributeValue(null, "value");
                                publishProgress(25);

                                minTemp = xpp.getAttributeValue(null, "min");
                                publishProgress(50);

                                maxTemp = xpp.getAttributeValue(null, "max");
                                publishProgress(75);
                                break;

                            case "weather":
                                iconName = xpp.getAttributeValue(null, "icon");
                        }
                    }
                    xpp.next();
                }


                String fileName = iconName + " .png";

                image = null;
                if (fileExistance(iconName + ".png")) {
                    FileInputStream fis = null;
                    try
                    {
                        fis = openFileInput(iconName + ".png");
                        Log.d("icon location:", "locally");
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }

                    image = BitmapFactory.decodeStream(fis);
                    fis.close();
                }else{

                    URL url = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200)
                    {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                        Log.d("icon location:", "downloading it");
                    }
                    FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }

                // save outlookPic image to local storage


                //Connect to the UV index server
                URL UVurl = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                InputStream inputStream = UVConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString();

                //now a JSON table:

                JSONObject jObject = new JSONObject(result);
                double value = jObject.getDouble("value");
                UV = jObject.getString("value");

                Log.i("UV is:", "" + value);

            } catch (XmlPullParserException e) {
                returnStrForOnPostExecute = "XML Pull exception. The XML is not properly formed";
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                returnStrForOnPostExecute = "MalFormed URL exception";
            } catch (IOException e) {
                returnStrForOnPostExecute = "URL connection. Is wi-fi connected?";
            } catch (JSONException e) {
                returnStrForOnPostExecute = "JSON object exception. There is an issue with the JSON file";
            }

            publishProgress(100);
            return returnStrForOnPostExecute;
        }

        @Override
        protected void onPostExecute(String sentFromDoInBackground)
        {
            super.onPostExecute(sentFromDoInBackground);
            char degreeC = 0x2103;

            currentTempTv.setText(String.format("%s%c", currentTemp, degreeC));
            maxTempTv.setText(String.format("High: %s%c", maxTemp, degreeC));
            minTempTv.setText(String.format("Low: %s%c", minTemp, degreeC));
            uvRateTV.setText(String.format("UV Index: %s", UV));
            Weather.setImageBitmap(image);

            weatherPB.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            weatherPB.setVisibility(View.VISIBLE);
            weatherPB.setProgress(values[0]);
        }

        public boolean fileExistance(String fname)
        {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }


    }
}

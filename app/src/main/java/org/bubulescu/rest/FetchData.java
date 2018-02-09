package org.bubulescu.rest;


import android.os.AsyncTask;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class FetchData extends AsyncTask<String, Void, List<Joke>> {

    @Override
    protected List<Joke> doInBackground(String... params) {

        if (params.length < 1 || params[0] == null) {
            return null;
        }

        String stringUrl = params[0];
        URL url = createUrl(stringUrl);
        String response = makeHTTPrequest(url);
        List<Joke> jokes = getJokesFromJsonResponse(response);

        return jokes;
    }

    private List<Joke> getJokesFromJsonResponse(String response) {

        if (TextUtils.isEmpty(response)) {
            return null;
        }

        List<Joke> listOfJokes = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(response);
            JSONArray jokesArray = baseJsonResponse.getJSONArray("value");
            for (int i = 0; i < jokesArray.length(); i++) {
                JSONObject jokes = jokesArray.getJSONObject(i);
                String jokeString = jokes.getString("joke");
                Joke joke = new Joke(i, jokeString);
                listOfJokes.add(joke);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return listOfJokes;
    }

    private String makeHTTPrequest(URL url) {

        String jsonResponse = "";
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(2000);
            httpURLConnection.setReadTimeout(2000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readfromStream(inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) httpURLConnection.disconnect();
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonResponse;
    }

    private String readfromStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

        if (inputStream != null) {
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;
            try {
                do {
                    line = reader.readLine();
                    output.append(line);
                } while (line != null);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return output.toString();
    }

    private URL createUrl(String stringUrl) {
        try {
            return new URL(stringUrl);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Joke> jokes) {
        StringBuilder builder = new StringBuilder();

        for (Joke joke : jokes ) {
            String jokeString = joke.getJoke();
            builder.append("\n" + jokeString + "\n");
        }

        MainActivity.setJokeToTextView(builder.toString());
    }
}

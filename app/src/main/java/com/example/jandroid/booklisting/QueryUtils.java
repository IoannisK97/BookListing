package com.example.jandroid.booklisting;
import android.text.TextUtils;

import android.util.Log;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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



public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils() {

    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the url!");
        }
        return url;
    }
    private static List<Book> extractFeatures(String booksJSON) {
        if (TextUtils.isEmpty(booksJSON)) {
            return null;
        }
        List<Book> allBooks = new ArrayList<>();

        try {
            JSONObject rawJSONResponse = new JSONObject(booksJSON);
            JSONArray books = rawJSONResponse.getJSONArray("items");
            for (int i = 0; i < books.length(); i++) {

                JSONObject book = books.getJSONObject(i);
                JSONObject volume = book.getJSONObject("volumeInfo");
                String bookTitle = volume.getString("title");


                StringBuilder authors = new StringBuilder();
                if (volume.has("authors")) {

                    JSONArray jsonAuthors = volume.getJSONArray("authors");
                    int numberOfAuthors = jsonAuthors.length();
                    int maxAuthors = 3;

                    String cAuthors = "";
                    String[] allAuthors =  null;

                    // authors are in a signle string concatenated or in a list
                    int numberOfLetters = jsonAuthors.get(0).toString().length();
                    // 35 as the max length
                    if (numberOfLetters > 35) {
                        // Authors are concatenated
                        cAuthors = jsonAuthors.toString().substring(2, numberOfLetters - 1);
                        allAuthors = cAuthors.split("[;,]");
                        for (int j = 0; j < allAuthors.length && j < maxAuthors; j++) {
                            authors.append(allAuthors[j].trim()).append("\n");
                        }

                    } else {
                        // authors are not concatenated
                        for (int j = 0; j < numberOfAuthors && j < maxAuthors; j++) {
                            authors.append(jsonAuthors.getString(j)).append("\n");
                        }
                    }
                }

                int pagesCount=0;
                if (volume.has("pageCount")) {
                    pagesCount = volume.getInt("pageCount");
                }
                JSONObject saleInfo = book.getJSONObject("saleInfo");
                // saleable or not
                String saleability = saleInfo.getString("saleability");

                if (volume.has("imageLinks")){
                    JSONObject pictures=volume.getJSONObject("imageLinks");
                    String thumbnailURL =pictures.getString("smallThumbnail");
                    allBooks.add(new Book(bookTitle, authors.toString(), pagesCount, saleability,thumbnailURL));

                }
                else{
                    allBooks.add(new Book(bookTitle, authors.toString(), pagesCount, saleability));
                }





            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the google books JSON results", e);
        }

        return allBooks;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // Return early if url is null
        if (url == null) {
            return jsonResponse;
        }

        // Initialize HTTP connection object
        HttpURLConnection urlConnection = null;

        // Initialize {@link InputStream} to hold response from request
        InputStream inputStream = null;

        try {
            // Establish connection to the url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Set request type
            urlConnection.setRequestMethod("GET");


            // Establish connection to the url
            urlConnection.connect();

            // Check for successful connection
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Connection successfully established
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error while connecting. Error Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.getMessage();
            Log.e(LOG_TAG, "Problem encountered while retrieving book results");
        } finally {
            if (urlConnection != null) {
                // Disconnect the connection after successfully making the HTTP request
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Close the stream after successfully parsing the request
                // This may throw an IOException which is why it is explicitly mentioned in the
                // method signature
                inputStream.close();
            }
        }

        // Return JSON as a {@link String}
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        // Convert the mutable characters sequence from the builder into a string and return
        return output.toString();
    }
    static List<Book> fetchBooks(String pUrl) {
        // Create valid url object from the requestURL
        URL url = createUrl(pUrl);

        // Initialize empty String object to hold the parsed JSON response
        String jsonResponse = "";

        // Perform HTTP request to the above created valid URL
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request for the search criteria");
        }

        // Extract information from the JSON response for each book
        // Return list of books
        return QueryUtils.extractFeatures(jsonResponse);
    }

}

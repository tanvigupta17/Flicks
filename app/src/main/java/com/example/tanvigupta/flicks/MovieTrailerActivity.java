package com.example.tanvigupta.flicks;

import android.os.Bundle;
import android.util.Log;

import com.example.tanvigupta.flicks.Models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    // base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this activity
    public final static String TAG = "MovieTrailerActivity";


    AsyncHttpClient client;  // client to use API
    String youtubeId;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        // initialize client
        client = new AsyncHttpClient();

        // unwrap movie passed in from intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        getVideos();
    }

    // get the list of videos associated with given movie ID using the API
    private void getVideos() {
        // create the URL to access
        String url = API_BASE_URL + "/movie/" + movie.getId() + "/videos";

        Log.i(TAG, String.format("URL created is: %s", url));

        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));

        // execute GET request, expect JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // use results to get video id for trailer
                try {
                    JSONArray results = response.getJSONArray("results");

                    // access first video in results array
                    youtubeId = results.getJSONObject(0).getString("key");
                    movie.setMovieId(youtubeId);

                    Log.i(TAG, String.format("Loaded video ID: %s", youtubeId));

                    // stuff pasted here
                    loadVideo();
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse videos", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Failed to get data from videos endpoint", throwable);
            }
        });
    }

    private void loadVideo() {
        // resolve player view
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize with API key
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                // work here to cue video, play video, etc
                Log.i(TAG, "Attempting to play video");
                youTubePlayer.cueVideo(youtubeId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                // log error
                Log.e("MovieTrailerActivity", "Error initializing Youtube Player");
            }
        });
    }
}

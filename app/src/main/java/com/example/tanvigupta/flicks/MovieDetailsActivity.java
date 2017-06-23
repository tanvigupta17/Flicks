package com.example.tanvigupta.flicks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tanvigupta.flicks.Models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    TextView tvRelease;
    ImageView ivBackdrop;

    // base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging from this activity
    public final static String TAG = "MovieDetailsActivity";

    AsyncHttpClient client;  // client to use API
    String youtubeId;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // set context
        context = this;

        // initialize client
        client = new AsyncHttpClient();

        // resolve the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        tvRelease = (TextView) findViewById(R.id.tvRelease);
        ivBackdrop = (ImageView) findViewById(R.id.ivBackdrop);

        // unwrap movie passed in from intent
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for %s", movie.getTitle()));

        // set title, overview, and release date
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvRelease.setText(String.format("Release date: %s", movie.getReleaseDate()));

        // set rating bar; vote average is /10, convert to /5
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        // load image using glide
        Glide.with(context)
                .load(movie.getBackdropUrl())
                .bitmapTransform(new RoundedCornersTransformation(context, 20, 0))
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(ivBackdrop);

        // set click listener for background image
        ivBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVideos();

                if (youtubeId != null) {
                    Intent in = new Intent(context, MovieTrailerActivity.class);
                    in.putExtra("id", youtubeId);
                    context.startActivity(in);
                }
            }
        });
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

                    Log.i(TAG, String.format("Loaded video ID: %s", youtubeId));
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
}

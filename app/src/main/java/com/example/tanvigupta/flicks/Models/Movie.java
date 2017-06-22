package com.example.tanvigupta.flicks.Models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tanvigupta on 6/21/17.
 */

public class Movie {
    // values from API
    private String title;
    private String overview;
    private String posterPath; // vertical, not complete URL
    private String backdropPath; // horizontal, not complete URL

    // initialize from JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
    }

    // access methods
    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}

package com.example.tanvigupta.flicks.Models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by tanvigupta on 6/21/17.
 */

@Parcel // indicates class is Parcelable
public class Movie {
    // values from API, must be public for parceler
    String title;
    String overview;
    String posterPath; // vertical, not complete URL
    String backdropPath; // horizontal, not complete URL
    Double voteAverage;
    String releaseDate;
    Integer id;
    String backdropUrl;

    // default constructor for parceler
    public Movie() {
    }

    // initialize from JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
        releaseDate = object.getString("release_date");
        id = object.getInt("id");
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

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Integer getId() {
        return id;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }
}

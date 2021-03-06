package com.example.tanvigupta.flicks.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tanvigupta on 6/21/17.
 */

public class Config {

    String imageBaseUrl;     // base URL for loading images
    String posterSize;       // size when fetching images vertically, part of URL
    String backdropSize;     // size when fetching images horizontally, part of URL

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        // get the image base URL
        imageBaseUrl = images.getString("secure_base_url");
        // get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        // use 4th value or fall back on default w342
        posterSize = posterSizeOptions.optString(3, "w342");

        // get the backdrop size
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        // use 1st 2nd value or fall back on default w780
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    // helper method for creating URLs
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path);
    }

    // access methods
    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}

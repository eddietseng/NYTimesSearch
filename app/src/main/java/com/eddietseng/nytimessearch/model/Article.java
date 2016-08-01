package com.eddietseng.nytimessearch.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by eddietseng on 7/27/16.
 */
@Parcel
public class Article {
    // fields must be public
    String webUrl;
    String headline;
    String thumbNail;
    int thumbNailWidth;
    int thumbNailHeight;

    // empty constructor needed by the Parceler library
    public Article() {

    }

    public Article(String webUrl, String headline, String thumbNail, int thumbNailWidth, int thumbNailHeight) {
        this.webUrl = webUrl;
        this.headline = headline;
        this.thumbNail = thumbNail;
        this.thumbNailWidth = thumbNailWidth;
        this.thumbNailHeight = thumbNailHeight;
    }

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if( multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(new Random().nextInt(multimedia.length()));
                this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
                this.thumbNailWidth = Integer.parseInt(multimediaJson.getString("width"));
                this.thumbNailHeight = Integer.parseInt(multimediaJson.getString("height"));
            } else
                this.thumbNail = "";
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Article> results = new ArrayList<>();

        for(int i=0; i < jsonArray.length(); ++i) {
            try {
                results.add(new Article(jsonArray.getJSONObject(i)));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public int getThumbNailWidth() {
        return thumbNailWidth;
    }

    public int getThumbNailHeight() {
        return thumbNailHeight;
    }
}

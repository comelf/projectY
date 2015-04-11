package com.projecty.ddotybox.model.item;

import com.projecty.ddotybox.model.base.StatisticsItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class HomeVideoItem extends StatisticsItem{


    public HomeVideoItem(JSONObject jsonItem) throws JSONException, ParseException {
        final JSONObject snippet = jsonItem.getJSONObject("snippet");

        id = jsonItem.getString("id");
        title = snippet.getString("title");
        date = snippet.getString("publishedAt").substring(0, 10);
        thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");
        videoId = snippet.getJSONObject("resourceId").getString("videoId");

        duration = setDuration(snippet.getString("duration"));
        viewCount = convertCount(snippet.getString("viewCount"));
        likeCount = convertCount(snippet.getString("likeCount"));

    }

}
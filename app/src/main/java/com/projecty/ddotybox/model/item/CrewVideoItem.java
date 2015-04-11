package com.projecty.ddotybox.model.item;

import com.projecty.ddotybox.model.base.StatisticsItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class CrewVideoItem extends StatisticsItem{


    public CrewVideoItem(JSONObject jsonItem, String sandboxId) throws JSONException, ParseException {
        final JSONObject crew = jsonItem.getJSONObject(sandboxId);

        id = jsonItem.getString("id");
        title = crew.getString("title");
        date = crew.getString("publishedAt").substring(0, 10);
        thumbnailUrl = crew.getJSONObject("thumbnails").getJSONObject("medium").getString("url");
        videoId = jsonItem.getString("id");
    }

}

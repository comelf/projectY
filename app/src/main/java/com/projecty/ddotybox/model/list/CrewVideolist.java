
package com.projecty.ddotybox.model.list;

import com.projecty.ddotybox.model.base.StatisticsItem;
import com.projecty.ddotybox.model.base.StatisticsPage;
import com.projecty.ddotybox.model.base.VideoList;
import com.projecty.ddotybox.model.item.CrewVideoItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class CrewVideolist extends VideoList {
    private String sandboxId;

    public CrewVideolist(JSONObject jsonPlaylist, String sandboxId) throws JSONException, ParseException {
        super(jsonPlaylist);
        this.sandboxId = sandboxId;
        addPage(jsonPlaylist);
    }

    public void addPage(JSONObject jsonPlaylist) throws JSONException, ParseException {
        statisticsPages.add(new StatisticsPage(
                jsonPlaylist.getJSONArray("items"),
                jsonPlaylist.getString("etag"),
                jsonPlaylist.optString("nextPageToken", null)) {
            @Override
            protected StatisticsItem getStatisticsItem(JSONObject item) throws JSONException, ParseException {
                return new CrewVideoItem(item,sandboxId);
            }
        });
    }
}


package com.projecty.ddotybox.model.list;

import com.projecty.ddotybox.model.base.PlayItem;
import com.projecty.ddotybox.model.base.PlayList;
import com.projecty.ddotybox.model.base.PlayPage;
import com.projecty.ddotybox.model.item.PlaylistItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class PlaylistOri extends PlayList {

    public PlaylistOri(JSONObject jsonPlaylist) throws JSONException, ParseException {
        super(jsonPlaylist);
    }

    @Override
    public void addPage(JSONObject jsonPlaylist) throws JSONException, ParseException {
        pages.add(new PlayPage(
                jsonPlaylist.getJSONArray("items"),
                jsonPlaylist.getString("etag"),
                jsonPlaylist.optString("nextPageToken", null)) {
            @Override
            protected PlayItem getPlayItem(JSONObject item) throws JSONException, ParseException {
                return new PlaylistItem(item);
            }
        });
    }
}

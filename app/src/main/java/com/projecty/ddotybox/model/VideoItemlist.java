package com.projecty.ddotybox.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by byungwoo on 15. 4. 4..
 */
public class VideoItemlist {
    public final int totalResults;
    public final int resultsPerPage;

    public List<Page> pages;

    public VideoItemlist(JSONObject jsonPlaylist) throws JSONException, ParseException {
        pages = new ArrayList<Page>();

        JSONObject pageInfo = jsonPlaylist.getJSONObject("pageInfo");
        totalResults = pageInfo.getInt("totalResults");
        resultsPerPage = pageInfo.getInt("resultsPerPage");

        addPage(jsonPlaylist);
    }


    public int getCount() {
        int count = 0;
        for (Page page : pages) {
            count += page.items.size();
        }

        return count;
    }

    public void addPage(JSONObject jsonPlaylist) throws JSONException, ParseException {
        pages.add(new Page(
                jsonPlaylist.getJSONArray("items"),
                jsonPlaylist.getString("etag")));
    }

    public VideoItemlistItem getItem(int position) {
        if(resultsPerPage==0) {
            return null;
        }
        Page page = pages.get(0);

        return page.items.get(position);
    }

    public class Page {
        public final List<VideoItemlistItem> items;
        public final String eTag;

        Page(JSONArray jsonItems, String etag) throws JSONException, ParseException {
            eTag = etag;
            items = new ArrayList<VideoItemlistItem>(jsonItems.length());

            for (int i = 0; i < jsonItems.length(); i++) {
                JSONObject item = jsonItems.getJSONObject(i);
                items.add(new VideoItemlistItem(item));
            }
        }
    }
}

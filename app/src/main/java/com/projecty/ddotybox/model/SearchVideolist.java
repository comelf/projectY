
package com.projecty.ddotybox.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SearchVideolist {
    public final int totalResults;
    public final int resultsPerPage;

    public List<Page> pages;

    public SearchVideolist(JSONObject jsonPlaylist) throws JSONException, ParseException {
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
                jsonPlaylist.getString("etag"),
                jsonPlaylist.optString("nextPageToken", null)));
    }

    public SearchVideolistItem getItem(int position) {
        int pageNumber = position / resultsPerPage;
        Page page = pages.get(pageNumber);

        return page.items.get(position % resultsPerPage);
    }

    public String getNextPageToken(int position) {
        int pageNumber = position / resultsPerPage;
        Page page = pages.get(pageNumber);

        return page.nextPageToken;
    }

    public String getEtag(int position) {
        int pageNumber = position / resultsPerPage;
        Page page = pages.get(pageNumber);

        return page.eTag;
    }

    public class Page {
        public final String nextPageToken;
        public final List<SearchVideolistItem> items;
        public final String eTag;

        Page(JSONArray jsonItems, String etag, String nextPageToken) throws JSONException, ParseException {
            eTag = etag;
            items = new ArrayList<SearchVideolistItem>(jsonItems.length());
            this.nextPageToken = nextPageToken;

            for (int i = 0; i < jsonItems.length(); i++) {
                JSONObject item = jsonItems.getJSONObject(i);
                items.add(new SearchVideolistItem(item));
            }
        }
    }
}

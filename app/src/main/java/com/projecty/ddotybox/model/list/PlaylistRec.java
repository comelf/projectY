
package com.projecty.ddotybox.model.list;

import com.projecty.ddotybox.model.base.PlayItem;
import com.projecty.ddotybox.model.base.PlayList;
import com.projecty.ddotybox.model.base.PlayPage;
import com.projecty.ddotybox.model.item.PlayRecItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

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
public class PlaylistRec extends PlayList {


    public PlaylistRec(JSONObject jsonPlaylist) throws JSONException, ParseException {
       super(jsonPlaylist);
    }

    public void addPage(JSONObject jsonPlaylist) throws JSONException, ParseException {
        pages.add(new PlayPage(
                jsonPlaylist.getJSONArray("items"),
                jsonPlaylist.getString("etag")
        ) {
            @Override
            protected PlayItem getPlayItem(JSONObject item) throws JSONException, ParseException {
                return new PlayRecItem(item);
            }
        });
    }
}
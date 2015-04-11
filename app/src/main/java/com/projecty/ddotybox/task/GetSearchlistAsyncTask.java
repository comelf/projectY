package com.projecty.ddotybox.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.etag.CacheRequest;
import com.github.kevinsawicki.etag.EtagCache;
import com.projecty.ddotybox.util.ApiKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class GetSearchlistAsyncTask extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = "GetYouTubelistAsyncTask";

    private static final int YOUTUBE_PLAYLIST_MAX_RESULTS = 10;
    private static final String URL_BASE = "https://www.googleapis.com/youtube/v3/search";
    private static final String URL_END = "&channelId=UChQ-VMvdGrYZxviQVMTJOHg&type=video&key=AIzaSyDrp3hVd7PBIryKmk3nBcPIoxTOX5kTPvQ";
    //            "statistics(viewCount, videoCount))";
//    , statistics(viewCount, videoCount))";
    String query;

    @Override
    protected JSONObject doInBackground(String... params) {
        final String playlistId = params[0];
        if (playlistId == null || playlistId.length() == 0) {
            return null;
        }

        if (params.length == 2 ) {
            query = params[1].trim();
            if(query.isEmpty()){
                return null;
            }
        }

        mUriBuilder = Uri.parse(URL_BASE).buildUpon();
        mUriBuilder.appendQueryParameter("part", "snippet")
                .appendQueryParameter("q", query)
                .appendQueryParameter("channelId", "UChQ-VMvdGrYZxviQVMTJOHg")
                .appendQueryParameter("type", "video")
                .appendQueryParameter("key", ApiKey.YOUTUBE_API_KEY);

        final String result = doGetUrl(mUriBuilder.build().toString());
        if (result == null) {
//            Log.e(TAG, "Failed to get playlist");
            return null;
        }

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
            JSONArray itemList = jsonObject.getJSONArray("items");
            String items = "";
            for (int i = 0; i < itemList.length() ; i++) {
                String id = itemList.getJSONObject(i).getJSONObject("id").getString("videoId");
                items = items + id + ",";
//                Log.i(TAG, id);
            }
            String api = "https://www.googleapis.com/youtube/v3/videos?part=statistics,contentDetails&id=" + items + "&key=AIzaSyDrp3hVd7PBIryKmk3nBcPIoxTOX5kTPvQ";
            Uri.Builder uriBuilder = Uri.parse(api).buildUpon();
            String result2 = doGetUrl(uriBuilder.build().toString());
            JSONObject itemInfo = new JSONObject(result2);

            Log.i(TAG, api);
            Log.i(TAG, result2);
            

            for (int i = 0; i < itemList.length() ; i++) {
                JSONObject item = itemInfo.getJSONArray("items").getJSONObject(i);
                JSONObject snippet = itemList.getJSONObject(i).getJSONObject("snippet");

                String duration = item.getJSONObject("contentDetails").getString("duration");
                String viewCount = String.valueOf(item.getJSONObject("statistics").getLong("viewCount"));
                String likeCount = String.valueOf(item.getJSONObject("statistics").getLong("likeCount"));

                snippet.put("duration",duration);
                snippet.put("viewCount", viewCount);
                snippet.put("likeCount", likeCount);
            }
            
            
            
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jsonObject;
    }

    protected Uri.Builder mUriBuilder;

    public abstract EtagCache getEtagCache();

    public String doGetUrl(String url) {
//        Log.d(TAG, url);

        CacheRequest request = CacheRequest.get(url, getEtagCache());
//        Log.d(TAG, "Response was " + request.body());

        StringBuilder builder = new StringBuilder();
        InputStream is = request.stream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (request.cached()) {
            Log.d(TAG, "Cache hit");
        } else {
            Log.d(TAG, "Cache miss");
        }

        return builder.toString();
    }
}

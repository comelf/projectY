package com.projecty.ddotybox.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.etag.CacheRequest;
import com.github.kevinsawicki.etag.EtagCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by byungwoo on 15. 4. 4..
 */
public abstract class GetRecommandlistAsyncTask extends AsyncTask<String, Void, JSONObject> {
    protected Uri.Builder mUriBuilder;
    private static final String TAG = "GetRecommandlistAsyncTask";

    private String url = "http://52.68.56.175/get_recommend";
    private static final int YOUTUBE_PLAYLIST_MAX_RESULTS = 10;
    private static final String YOUTUBE_PLAYLISTITEMS_URL = "https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics,contentDetails&id=";
    private static final String YOUTUBE_PLAYLIST_PART = "snippet";
    private static final String YOUTUBE_PLAYLIST_FIELDS = "etag,pageInfo,nextPageToken,items(id, snippet(title,position,publishedAt, thumbnails(medium,high),resourceId/videoId))";


    public GetRecommandlistAsyncTask() {

    }


    @Override
    protected JSONObject doInBackground(String... params) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        JSONObject recommend = doGetJsonFromUrl(builder.build().toString());
        String video_list = "";

        if(recommend==null){
            return null;
        }

        try {
            video_list = recommend.getString("cover_list");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        String url  = YOUTUBE_PLAYLISTITEMS_URL+video_list+"&key=AIzaSyDrp3hVd7PBIryKmk3nBcPIoxTOX5kTPvQ";


        final String result = doGetUrl(url);
        if (result == null) {
//            Log.e(TAG, "Failed to get playlist");
            return null;
        }

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return jsonObject;
    }

    private JSONObject doGetJsonFromUrl(String s) {
        CacheRequest request = CacheRequest.get(url, getEtagCache());

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

        String res =  builder.toString();
        if (res == null) {
            return null;
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;


    }


    public String doGetUrl(String url) {

        CacheRequest request = CacheRequest.get(url, getEtagCache());

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

    public abstract EtagCache getEtagCache();

}

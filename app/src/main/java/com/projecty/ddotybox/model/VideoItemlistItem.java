package com.projecty.ddotybox.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by byungwoo on 15. 4. 4..
 */
public class VideoItemlistItem {
    public final String id;
    public final String title;
    public final String date;
    public final String thumbnailUrl;
    public final String videoId;


    public final String duration;
    public final String likeCount;
    public final String viewCount;



    public VideoItemlistItem(JSONObject jsonItem) throws JSONException, ParseException {
        final JSONObject snippet = jsonItem.getJSONObject("snippet");
        final JSONObject statistics = jsonItem.getJSONObject("statistics");
        final JSONObject contentDetails = jsonItem.getJSONObject("contentDetails");

        id = jsonItem.getString("id");


        title = snippet.getString("title");
        date = snippet.getString("publishedAt").substring(0, 10);
        thumbnailUrl = snippet.getJSONObject("thumbnails").getJSONObject("medium").getString("url");
        videoId = jsonItem.getString("id");

        duration = setDuration(contentDetails.getString("duration"));
        viewCount = convertCount(statistics.getString("viewCount"));
        likeCount = convertCount(statistics.getString("likeCount"));

    }

    private String convertCount(String num){
        String result = num;

        int length = num.length();
        if (length > 4){
            result = num.substring(0, length-4) + "만";
        }
        return result;
    }


    private static HashMap<String, String> regexMap = new HashMap<>();
    private static String regex2two = "(?<=[^\\d])(\\d)(?=[^\\d])";
    private static String two = "0$1";

    private String setDuration(String druation) throws ParseException {

        regexMap.put("PT(\\d\\d)S", "00:$1");
        regexMap.put("PT(\\d\\d)M", "$1:00");
        regexMap.put("PT(\\d\\d)H", "$1:00:00");
        regexMap.put("PT(\\d\\d)M(\\d\\d)S", "$1:$2");
        regexMap.put("PT(\\d\\d)H(\\d\\d)S", "$1:00:$2");
        regexMap.put("PT(\\d\\d)H(\\d\\d)M", "$1:$2:00");
        regexMap.put("PT(\\d\\d)H(\\d\\d)M(\\d\\d)S", "$1:$2:$3");

        String d = druation.replaceAll(regex2two, two);
        String regex = getRegex(d);
        return d.replaceAll(regex, regexMap.get(regex));
    }

    private String getRegex(String date) {
        for (String r : regexMap.keySet())
            if (Pattern.matches(r, date))
                return r;
        return null;
    }
}

package com.projecty.ddotybox.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kevinsawicki.etag.EtagCache;
import com.google.gson.Gson;
import com.projecty.ddotybox.R;
import com.projecty.ddotybox.model.base.PlayItem;
import com.projecty.ddotybox.model.base.StatisticsItem;
import com.projecty.ddotybox.model.list.HomeVideolist;
import com.projecty.ddotybox.task.GetPlayListPageAsyncTask;
import com.projecty.ddotybox.task.GetVideolistAsyncTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeeYoungNam on 3/25/15.
 */
public class PlayListPageFragment extends Fragment implements View.OnClickListener {
    private static final String YOUTUBE_PLAYLIST = "UUhQ-VMvdGrYZxviQVMTIOHd";
    private static final String PLAYLIST_KEY = "PLAYLIST_KEY";

    ListView mListView;
    private EtagCache mEtagCache;
    private PlaylistPageAdapter mAdapter;
    private PlayItem item_play;
    private HomeVideolist mHomeVideolist;
    private List<AsyncTask> asyncTasks = new ArrayList<AsyncTask>();

    public PlayListPageFragment() {

    }

    public void setItem(PlayItem item){
        this.item_play = item;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.play_list_page, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.video_thumbnail_i);
        TextView date = (TextView) view.findViewById(R.id.video_date_i);
        TextView title = (TextView) view.findViewById(R.id.video_title_i);

        ImageButton playBtn = (ImageButton) view.findViewById(R.id.playButton);

        date.setText(item_play.date);
        title.setText(item_play.title);



        Picasso.with(getActivity())
                .load(item_play.thumbnailUrl)
                .into(imageView);

        playBtn.setOnClickListener(this);


        mListView = (ListView) view.findViewById(R.id.play_list_view);


        // restore the playlist after an orientation change
        if (savedInstanceState != null) {
            mHomeVideolist = new Gson().fromJson(savedInstanceState.getString(PLAYLIST_KEY), HomeVideolist.class);
        }

        // ensure the adapter and listview are initialized
        if (mHomeVideolist != null) {
            initListAdapter(mHomeVideolist);
        }

        // start loading the first page of our playlist
        AsyncTask async = new GetPlayListPageAsyncTask(item_play.id){
            @Override
            public EtagCache getEtagCache() {
                return mEtagCache;
            }

            @Override
            public void onPostExecute(JSONObject result) {
                handlePlaylistResult(result);
            }
        }.execute(YOUTUBE_PLAYLIST, null);
        asyncTasks.add(async);

        return view;
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String json = new Gson().toJson(mHomeVideolist);
        outState.putString(PLAYLIST_KEY, json);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // initialize our etag cache for this playlist
        File cacheFile = new File(activity.getFilesDir(), YOUTUBE_PLAYLIST);
        mEtagCache = EtagCache.create(cacheFile, EtagCache.FIVE_MB);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        for(AsyncTask task : asyncTasks){
            task.cancel(true);
        }
    }

    private void initListAdapter(HomeVideolist homeVideolist) {
        mHomeVideolist = homeVideolist;
        mAdapter = new PlaylistPageAdapter(homeVideolist);
        mListView.setAdapter(mAdapter);
    }

    private void handlePlaylistResult(JSONObject result) {
        try {
            if (mHomeVideolist == null) {
                mHomeVideolist = new HomeVideolist(result);
                initListAdapter(mHomeVideolist);
            } else {
                mHomeVideolist.addPage(result);
            }

            if (!mAdapter.setIsLoading(false)) {
                mAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playButton:
                StatisticsItem item = mHomeVideolist.getItem(0);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + item.videoId + "&list=" + item_play.id)));
                break;
            case R.id.favoriteButton:

                break;
        }
    }


    protected class PlaylistPageAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private HomeVideolist mHomeVideolist;
        private boolean mIsLoading = false;

        PlaylistPageAdapter(HomeVideolist HomeVideolist) {
            mHomeVideolist = HomeVideolist;
            mInflater = getLayoutInflater(null);
        }


        public boolean setIsLoading(boolean isLoading) {
            if (mIsLoading != isLoading) {
                mIsLoading = isLoading;
                notifyDataSetChanged();
                return true;
            }
            return false;
        }

        @Override
        public int getCount() {
            return mHomeVideolist.getCount() + (mIsLoading ? 1 : 0);
        }

        @Override
        public StatisticsItem getItem(int i) {
            return mHomeVideolist.getItem(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }



        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if (mIsLoading && position == (getCount() - 1)) {
                return mInflater.inflate(R.layout.youtube_video_list_item_loading, null, false);
            }
            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.youtube_video_list_item, null, false);
                viewHolder.title = (TextView) convertView.findViewById(R.id.video_title);
                viewHolder.date = (TextView) convertView.findViewById(R.id.video_date);
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.video_thumbnail);
                viewHolder.duration = (TextView) convertView.findViewById(R.id.duration);
                viewHolder.viewCount = (TextView) convertView.findViewById(R.id.playButton);
                viewHolder.likeCount = (TextView) convertView.findViewById(R.id.likeButton);
                convertView.setTag(viewHolder);

            }

            viewHolder = (ViewHolder) convertView.getTag();

            final StatisticsItem item = getItem(position);

            Typeface custom_font = Typeface.createFromAsset(convertView.getContext().getAssets(), "NotoSans.otf");
            viewHolder.title.setTypeface(custom_font);
            viewHolder.title.setText(item.title);
            viewHolder.date.setText(item.date);
            viewHolder.duration.setText(item.duration);
            viewHolder.viewCount.setText(item.viewCount);
            viewHolder.likeCount.setText(item.likeCount);

            Picasso.with(getActivity())
                    .load(item.thumbnailUrl)
                    .into(viewHolder.thumbnail);


            //프레그먼트 이동
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + item.videoId + "&list=" + item_play.id)));



                }
            });

            final String nextPageToken = mHomeVideolist.getNextPageToken(position);
            if (!isEmpty(nextPageToken) && position == getCount() - 1) {
                AsyncTask async = new GetVideolistAsyncTask() {
                    @Override
                    public EtagCache getEtagCache() {
                        return mEtagCache;
                    }

                    @Override
                    public void onPostExecute(JSONObject result) {
                        handlePlaylistResult(result);
                    }
                }.execute(YOUTUBE_PLAYLIST, nextPageToken);
                asyncTasks.add(async);
                setIsLoading(true);
            }

            return convertView;
        }

        private boolean isEmpty(String s) {
            if (s == null || s.length() == 0) {
                return true;
            }
            return false;
        }

        class ViewHolder {
            ImageView thumbnail;
            TextView title;
            TextView date;
            TextView duration;
            TextView viewCount;
            TextView likeCount;

        }

    }
}

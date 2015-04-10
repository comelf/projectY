package com.projecty.ddotybox.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.kevinsawicki.etag.EtagCache;
import com.google.gson.Gson;
import com.projecty.ddotybox.R;
import com.projecty.ddotybox.model.PlaylistIistInFav;
import com.projecty.ddotybox.model.PlaylistItem;
import com.projecty.ddotybox.model.VideoItemlist;
import com.projecty.ddotybox.model.VideoItemlistItem;
import com.projecty.ddotybox.task.GetFavoriteAsyncTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;

/**
 * Created by byungwoo on 15. 4. 8..
 */
public class FavoriteFragment extends Fragment implements View.OnClickListener {
    private static final String PLAYLIST_KEY1 = "FAVORITE_KEY";
    private static final String YOUTUBE_PLAYLIST = "UUhQ-VMvdGrYZxviQDMTIOHd";
    ListView mListView;
    private EtagCache mEtagCache;
    private FavoriteAdapter mAdapter;
    private VideoItemlist mVideolist;
    private PlaylistIistInFav pVideolist;
    private AsyncTask aVideo;
    private AsyncTask aPlay;

    Button favVideoBtn;
    Button favPlayBtn;

    int user_id = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);


        favVideoBtn = (Button) view.findViewById(R.id.fav_video_list_btn);
        favPlayBtn = (Button) view.findViewById(R.id.fav_play_list_btn);

        favPlayBtn.setOnClickListener(this);
        favVideoBtn.setOnClickListener(this);

        mListView = (ListView) view.findViewById(R.id.favorite_list_view);

        if (savedInstanceState != null) {
            mVideolist = new Gson().fromJson(savedInstanceState.getString(PLAYLIST_KEY1), VideoItemlist.class);
        }

        if (mVideolist != null) {
            initListAdapter(mVideolist);
        }

//        // start loading the first page of our playlist
        aVideo = new GetFavoriteAsyncTask("/get_favorite_videolist","video_list", user_id){
            @Override
            public EtagCache getEtagCache() {
                return mEtagCache;
            }

            @Override
            public void onPostExecute(JSONObject result) {

                handleVideoResult(result);
            }

        }.execute(YOUTUBE_PLAYLIST, null);

        return view;
    }

    private void initListAdapter(VideoItemlist videolist) {
        mVideolist = videolist;
        mAdapter = new FavoriteVideolistPageAdapter(videolist);
        mListView.setAdapter(mAdapter);
    }


    private void initPlayListAdapter(PlaylistIistInFav videolist) {
        pVideolist = videolist;
        mAdapter = new FavoritePlaylistPageAdapter(videolist);
        mListView.setAdapter(mAdapter);
    }


    private void handleVideoResult(JSONObject result) {
        try {
            if(result==null){
                return;
            }

            if (mVideolist == null) {
                mVideolist = new VideoItemlist(result);
                initListAdapter(mVideolist);
            } else {
                mVideolist = new VideoItemlist(result);
                initListAdapter(mVideolist);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void handlePlayResult(JSONObject result) {
        try {
            if(result==null){
                return;
            }

            if (pVideolist == null) {
                pVideolist = new PlaylistIistInFav(result);
                initPlayListAdapter(pVideolist);
            } else {
                pVideolist = new PlaylistIistInFav(result);
                initPlayListAdapter(pVideolist);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

        aVideo.cancel(true);
        if(aPlay!=null){
            aPlay.cancel(true);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.fav_video_list_btn:
                favVideoBtn.setBackgroundColor(Color.parseColor("#ED5565"));
                favVideoBtn.setTextColor(Color.parseColor("#FFFFFF"));
                favPlayBtn.setBackgroundColor(Color.parseColor("#F5F7FA"));
                favPlayBtn.setTextColor(Color.parseColor("#000000"));
                aVideo = new GetFavoriteAsyncTask("/get_favorite_videolist","video_list", user_id){
                    @Override
                    public EtagCache getEtagCache() {
                        return mEtagCache;
                    }

                    @Override
                    public void onPostExecute(JSONObject result) {

                        handleVideoResult(result);
                    }

                }.execute(YOUTUBE_PLAYLIST, null);
                return;
            case R.id.fav_play_list_btn:
                favPlayBtn.setBackgroundColor(Color.parseColor("#ED5565"));
                favPlayBtn.setTextColor(Color.parseColor("#FFFFFF"));
                favVideoBtn.setBackgroundColor(Color.parseColor("#F5F7FA"));
                favVideoBtn.setTextColor(Color.parseColor("#000000"));
                aPlay = new GetFavoriteAsyncTask("/get_favorite_playlist","play_list",user_id) {
                    @Override
                    public EtagCache getEtagCache() {
                        return mEtagCache;
                    }
                    @Override
                    public void onPostExecute(JSONObject result) {
                        handlePlayResult(result);
                    }
                }.execute(YOUTUBE_PLAYLIST, null);

            return;
        }
    }

    private abstract static class FavoriteAdapter extends BaseAdapter {

    };

    private class FavoriteVideolistPageAdapter extends FavoriteAdapter {
        private final LayoutInflater mInflater;
        private VideoItemlist mVideolist;
        private boolean mIsLoading = false;


        public FavoriteVideolistPageAdapter(VideoItemlist videolist) {
            mVideolist = videolist;
            mInflater = getLayoutInflater(null);

        }


        @Override
        public int getCount() {
            return mVideolist.getCount() + (mIsLoading ? 1 : 0);
        }

        @Override
        public VideoItemlistItem getItem(int i) {
            return mVideolist.getItem(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        private void moveFragement(int position){
            DetailPageFragment fr = new DetailPageFragment();
            fr.setItem(getItem(position));
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            fragmentTransaction.add(R.id.container, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
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

            final VideoItemlistItem item = getItem(position);

            viewHolder.title.setText(item.title);
            viewHolder.date.setText(item.date);
            viewHolder.duration.setText(item.duration);
            viewHolder.viewCount.setText(item.viewCount);
            viewHolder.likeCount.setText(item.likeCount);

            Typeface custom_font = Typeface.createFromAsset(convertView.getContext().getAssets(), "NotoSans.otf");
            viewHolder.title.setTypeface(custom_font);
            Picasso.with(getActivity())
                    .load(item.thumbnailUrl)
                    .into(viewHolder.thumbnail);


            //프레그먼트 이동
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveFragement(position);
                }
            });

//            final String nextPageToken = mVideolist.getNextPageToken(position);
//            if (!isEmpty(nextPageToken) && position == getCount() - 1) {
//                AsyncTask async = new GetVideolistAsyncTask() {
//                    @Override
//                    public EtagCache getEtagCache() {
//                        return mEtagCache;
//                    }
//
//                    @Override
//                    public void onPostExecute(JSONObject result) {
//                        handleVideoResult(result);
//                    }
//                }.execute(YOUTUBE_PLAYLIST, nextPageToken);
//                asyncTasks.add(async);
//                setIsLoading(true);
//            }

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


    private class FavoritePlaylistPageAdapter extends FavoriteAdapter {
        private final LayoutInflater mInflater;
        private PlaylistIistInFav mPlaylist;
        private boolean mIsLoading = false;

        FavoritePlaylistPageAdapter(PlaylistIistInFav playlist) {
            mPlaylist = playlist;
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
            return mPlaylist.getCount() + (mIsLoading ? 1 : 0);
        }

        @Override
        public PlaylistItem getItem(int i) {
            return mPlaylist.getItem(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        private void moveFragement(int position){
            PlayListPageFragment fr = new PlayListPageFragment();
            fr.setItem(getItem(position));
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();

            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            fragmentTransaction.add(R.id.container, fr);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (mIsLoading && position == (getCount() - 1)) {
                return mInflater.inflate(R.layout.youtube_video_list_item_loading, null, false);
            }
            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.youtube_play_list_item, null, false);
                viewHolder.title = (TextView) convertView.findViewById(R.id.video_title);
                viewHolder.date = (TextView) convertView.findViewById(R.id.video_date);
                viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.video_thumbnail);
                viewHolder.videoCount = (TextView) convertView.findViewById(R.id.left_box_count);
                viewHolder.playButton = (TextView) convertView.findViewById(R.id.playButton);
                viewHolder.likeButton = (TextView) convertView.findViewById(R.id.likeButton);
                viewHolder.left_video = (TextView) convertView.findViewById(R.id.left_video_title);

                convertView.setTag(viewHolder);

            }

            viewHolder = (ViewHolder) convertView.getTag();

            final PlaylistItem item = getItem(position);

            Typeface custom_font = Typeface.createFromAsset(convertView.getContext().getAssets(), "NotoSans.otf");
            viewHolder.title.setTypeface(custom_font);
            viewHolder.videoCount.setTypeface(custom_font);
            viewHolder.left_video.setTypeface(custom_font);
            viewHolder.title.setText(item.title);
            viewHolder.date.setText(item.date);
            viewHolder.videoCount.setText(item.videoCount);
            //viewHolder.playButton.setText(item.);


            Picasso.with(getActivity())
                    .load(item.thumbnailUrl)
                    .into(viewHolder.thumbnail);


            //프레그먼트 이동
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveFragement(position);
                }
            });

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
            TextView videoCount;
            TextView playButton;
            TextView likeButton;
            TextView left_video;
            LinearLayout left_box;
        }

    }
}

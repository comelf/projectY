package com.projecty.ddotybox.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.kevinsawicki.etag.EtagCache;
import com.projecty.ddotybox.R;
import com.projecty.ddotybox.adapter.CommentslistAdapter;
import com.projecty.ddotybox.model.UserProfile;
import com.projecty.ddotybox.model.base.StatisticsItem;
import com.projecty.ddotybox.model.list.CommentList;
import com.projecty.ddotybox.task.AddFavoriteAsyncTask;
import com.projecty.ddotybox.task.GetCommentListAsyncTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DetailPageFragment extends Fragment implements View.OnClickListener{
    ListView mListView;
    private CommentslistAdapter mAdapter;
    private StatisticsItem item;
    private ImageButton favoriteBtn;
    private int userId;
    private List<AsyncTask> asyncTasks = new ArrayList<AsyncTask>();
    private static final String COMMENT_KEY = "COMMENT_KEY";
    private EtagCache mEtagCache;
    private CommentList commentList;

    public DetailPageFragment() {

    }

    public void setItem(StatisticsItem item){
        this.item = item;
    }

    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate( R.layout.videopage, container, false);

        ImageView imageView =(ImageView) view.findViewById(R.id.video_thumbnail_i);
        TextView date = (TextView) view.findViewById(R.id.video_date_i);
        TextView play = (TextView) view.findViewById(R.id.video_play_i);
        TextView like = (TextView) view.findViewById(R.id.video_like_i);
        TextView title = (TextView) view.findViewById(R.id.video_title_i);
        ImageButton playBtn = (ImageButton) view.findViewById(R.id.playButton);
        ImageButton likeBtn = (ImageButton) view.findViewById(R.id.likeButton);
        favoriteBtn = (ImageButton) view.findViewById(R.id.favoriteButton);

        date.setText(item.date);
        play.setText(item.viewCount);
        like.setText(item.likeCount);
        title.setText(item.title);

        userId = UserProfile.getStaticUserId();
        Picasso.with(getActivity())
                .load(item.thumbnailUrl)
                .into(imageView);

        playBtn.setOnClickListener(this);
        favoriteBtn.setOnClickListener(this);

        mListView = (ListView) view.findViewById(R.id.commentsListview);

        AsyncTask commentAsync = new GetCommentListAsyncTask() {
            @Override
            public EtagCache getEtagCache() {
                return mEtagCache;
            }

            @Override
            public void onPostExecute(JSONObject result) {
                handletResult(result);
            }
        }.execute(COMMENT_KEY,item.videoId, null);
        asyncTasks.add(commentAsync);



        return view;
    }

    private void handletResult(JSONObject result) {
        try {
            if (commentList == null) {
                commentList = new CommentList(result);
                initListAdapter(commentList);
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
        File cacheFile = new File(activity.getFilesDir(), COMMENT_KEY);
        mEtagCache = EtagCache.create(cacheFile, EtagCache.FIVE_MB);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        for(AsyncTask task : asyncTasks){
            task.cancel(true);
        }

    }

    private void initListAdapter(CommentList commentList) {
        mAdapter = new CommentslistAdapter(commentList, getLayoutInflater(null));
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.playButton:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + item.videoId)));
                break;
            case R.id.likeButton:
                break;
            case R.id.favoriteButton:
                if(userId<1){
                    Toast.makeText(this.getActivity(),
                            "로그인을 해주세요.", Toast.LENGTH_LONG).show();
                    return;
                }
                AsyncTask like = new AddFavoriteAsyncTask(){
                    @Override
                    public void onPostExecute(String result) {
                        handleFavVideoResult(result);
                    }

                }.execute("/add_favorite_video",item.videoId,String.valueOf(userId), null);
                asyncTasks.add(like);
                break;
        }
    }

    private void handleFavVideoResult(String result) {
        if(result.equals("success")){
            Toast.makeText(this.getActivity(),
                    "성공", Toast.LENGTH_LONG).show();
            favoriteBtn.setEnabled(false);
        }else if(result.equals("duplication")){
            Toast.makeText(this.getActivity(),
                    "성공", Toast.LENGTH_LONG).show();
            favoriteBtn.setEnabled(false);
        }else {
            Toast.makeText(this.getActivity(),
                    "서버에 연결할수 없습니다.", Toast.LENGTH_LONG).show();
        }
    }
}

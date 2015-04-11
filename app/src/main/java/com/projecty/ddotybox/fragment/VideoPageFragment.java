package com.projecty.ddotybox.fragment;

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

import com.projecty.ddotybox.R;
import com.projecty.ddotybox.adapter.CommentslistAdapter;
import com.projecty.ddotybox.model.UserProfile;
import com.projecty.ddotybox.model.base.StatisticsItem;
import com.projecty.ddotybox.task.AddFavoriteAsyncTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VideoPageFragment extends Fragment implements View.OnClickListener{
    ListView mListView;
    private CommentslistAdapter mAdapter;
    private StatisticsItem item;
    private List<AsyncTask> asyncTasks = new ArrayList<AsyncTask>();
    private ImageButton favoriteBtn;
    private int userId;

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

        Picasso.with(getActivity())
                .load(item.thumbnailUrl)
                .into(imageView);

        playBtn.setOnClickListener(this);
        likeBtn.setOnClickListener(this);
        favoriteBtn.setOnClickListener(this);

        mListView = (ListView) view.findViewById(R.id.commentsListview);

        userId = UserProfile.getStaticUserId();

        String jsonData ="";

        if(mListView!=null){
            initListAdapter(jsonData);
        }
        return view;
    }

    private void initListAdapter(String jsonData) {
        mAdapter = new CommentslistAdapter(jsonData,getLayoutInflater(null));
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

    @Override
    public void onDetach() {
        super.onDetach();
        for (AsyncTask task : asyncTasks){
            task.cancel(true);
        }

    }

    private void handleFavVideoResult(String result) {
        if(result.equals("success")){
            Toast.makeText(this.getActivity(),
                    "성공", Toast.LENGTH_LONG).show();
            favoriteBtn.setEnabled(false);
            favoriteBtn.setAlpha((float)0.5);
        }else if(result.equals("duplication")){
            Toast.makeText(this.getActivity(),
                    "성공", Toast.LENGTH_LONG).show();
            favoriteBtn.setEnabled(false);
            favoriteBtn.setAlpha((float)0.5);
        }else {
            Toast.makeText(this.getActivity(),
                    "서버에 연결할수 없습니다.", Toast.LENGTH_LONG).show();
        }
    }
}

package com.projecty.ddotybox.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.projecty.ddotybox.R;
import com.projecty.ddotybox.adapter.CommentslistAdapter;
import com.projecty.ddotybox.model.base.StatisticsItem;
import com.squareup.picasso.Picasso;

public class SearchResultPageFragment extends Fragment implements View.OnClickListener{
    ListView mListView;
    private CommentslistAdapter mAdapter;
    private StatisticsItem item;

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
        ImageButton favoriteBtn = (ImageButton) view.findViewById(R.id.favoriteButton);

        date.setText(item.date);
        play.setText(item.viewCount);
        like.setText(item.likeCount);
        title.setText(item.title);

        Picasso.with(getActivity())
                .load(item.thumbnailUrl)
                .into(imageView);

        playBtn.setOnClickListener(this);


        mListView = (ListView) view.findViewById(R.id.commentsListview);


        String jsonData ="";

        if(mListView!=null){
            initListAdapter(jsonData);
        }
        return view;
    }

    private void initListAdapter(String jsonData) {
        mAdapter = new CommentslistAdapter(jsonData,getLayoutInflater(null));

        if(mListView==null){
            Log.v("DEBUG", "LIST View IS NULL!!");
        }

        if(mAdapter==null){
            Log.v("DEBUG", "Adapter IS NULL!!");
        }


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

                break;
        }
    }


}

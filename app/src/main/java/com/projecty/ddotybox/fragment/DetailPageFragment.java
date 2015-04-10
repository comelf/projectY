package com.projecty.ddotybox.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.projecty.ddotybox.R;
import com.projecty.ddotybox.model.VideoItemlistItem;
import com.squareup.picasso.Picasso;

public class DetailPageFragment extends Fragment implements View.OnClickListener{
    ListView mListView;
    private CommentslistAdapter mAdapter;
    private VideoItemlistItem item;

    public DetailPageFragment() {

    }

    public void setItem(VideoItemlistItem item){
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
        mAdapter = new CommentslistAdapter(jsonData);

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


    private class CommentslistAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private ViewHolder viewHolder;

        public CommentslistAdapter(String jsonData) {
            mInflater = getLayoutInflater(null);

            //JSON 파싱!
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

//            if ( position == (getCount() - 1)) {
//                return mInflater.inflate(R.layout.comment_card, null, false);
//            }

            if (convertView == null || convertView.getTag() == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.comment_card, null, false);
                viewHolder.detail = (TextView) convertView.findViewById(R.id.comment_detail);

                convertView.setTag(viewHolder);

                viewHolder.detail.setText("댓글댓글 댓글댓글 댓글댓글 댓글댓글 댓글댓글");
            }

            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.detail.setText("댓글댓글 댓글댓글 댓글댓글 댓글댓글 댓글댓글2");


            return convertView;
        }

        private class ViewHolder {

            public TextView detail;
        }
    }
}

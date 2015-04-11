package com.projecty.ddotybox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.projecty.ddotybox.R;

/**
 * Created by byungwoo on 15. 4. 11..
 */
public class CommentslistAdapter extends BaseAdapter{

    private final LayoutInflater mInflater;
    private ViewHolder viewHolder;

    public CommentslistAdapter(String jsonData, LayoutInflater mInflater) {
        this.mInflater = mInflater;

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

package com.projecty.ddotybox.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projecty.ddotybox.R;

/**
 * Created by LeeYoungNam on 7/19/15.
 */
public class StoreFragment extends Fragment {
    private FragmentTabHost mTabHost;
    
    public StoreFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.store,container, false);


        mTabHost = (FragmentTabHost) rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);
        


        mTabHost.addTab(mTabHost.newTabSpec("fragmentb").setIndicator("큐브 스토어 1"),
                Tab1.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragmentc").setIndicator("큐브 충전소 2"),
                Tab2.class, null);


        return rootView;
    }
}
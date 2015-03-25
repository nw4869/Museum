package com.nightwind.museum.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nightwind.museum.MainActivity;
import com.nightwind.museum.R;
import com.nightwind.museum.bean.CulturalRelic;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CulturalRelicFragment extends MainActivity.PlaceholderFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<CulturalRelic> mCulturalRelicList = new ArrayList<>();

    public CulturalRelicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCulturalRelicList.add(new CulturalRelic("Hello"));
        mCulturalRelicList.add(new CulturalRelic("World"));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cultural_relic, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);

        //use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new CulturalRelicAdapter(mCulturalRelicList);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


    private class CulturalRelicAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<CulturalRelic> mCulturalRelicList;

        public CulturalRelicAdapter(List<CulturalRelic> culturalRelicList) {
            mCulturalRelicList = culturalRelicList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_view_item_cultural_relic, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            CulturalRelic culturalRelic = mCulturalRelicList.get(i);
            viewHolder.name.setText(culturalRelic.getName());
        }

        @Override
        public int getItemCount() {
            return mCulturalRelicList.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}

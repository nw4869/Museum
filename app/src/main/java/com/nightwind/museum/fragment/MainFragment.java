package com.nightwind.museum.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nightwind.museum.MainActivity;
import com.nightwind.museum.Options;
import com.nightwind.museum.R;
import com.nightwind.museum.bean.Article;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends MainActivity.PlaceholderFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<Article> mArticleList = new ArrayList<>();
    private String[] mImageUrls = new String[] {"http://nw4869.xyz/img/slide/1.jpg", "http://nw4869.xyz/img/slide/2.jpg",
            "http://nw4869.xyz/img/slide/3.jpg", "http://nw4869.xyz/img/slide/4.jpg"};

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions slideImageOptions = Options.getSlideImageOptions();

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mArticleList.add(new Article("Title1", "Content1"));
        mArticleList.add(new Article("Title2", "Content2"));
        mArticleList.add(new Article("Title3", "Content3"));

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mRecyclerView.setHasFixedSize(true);

        //use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new ArticleAdapter(mArticleList);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


    private class ArticleAdapter extends RecyclerView.Adapter {

        private static final int VIEW_TYPE_NORMAL = 0;
        private static final int VIEW_TYPE_HEADER = 1;
        private List<Article> mArticleList;

        public ArticleAdapter(List<Article> articleList) {
            mArticleList = articleList;
        }

        @Override
        public int getItemViewType(int position) {
            int type = VIEW_TYPE_NORMAL;
            if (position == 0) {
                type = VIEW_TYPE_HEADER;
            }
            return type;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder;
            View view;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_header, parent, false);
                    viewHolder = new HeaderViewHolder(view);
                    break;
                case VIEW_TYPE_NORMAL:
                default:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
                    viewHolder = new NormalViewHolder(view);
                    break;
            }
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Article article = mArticleList.get(position);
            if (holder instanceof NormalViewHolder) {
                NormalViewHolder viewHolder = (NormalViewHolder) holder;
                viewHolder.title.setText(article.getTitle());
                viewHolder.content.setText(article.getContent());
            } else if (holder instanceof HeaderViewHolder) {
                //头部slide image view
                HeaderViewHolder viewHolder = (HeaderViewHolder) holder;

                ViewPager viewPage = viewHolder.imageViewPager;
                final ImageView[] imageViews = new ImageView[mImageUrls.length];
                for (int i = 0; i < mImageUrls.length; i++) {
                    ImageView imageView = new ImageView(getActivity());
                    imageViews[i] = imageView;
                    String imageUrl = mImageUrls[i % mImageUrls.length];
                    //加载图片
                    imageLoader.displayImage(imageUrl, imageView, slideImageOptions);
                }

                viewPage.setAdapter(new PagerAdapter() {
                    @Override
                    public int getCount() {
                        // 可左右滑动
                        return Integer.MAX_VALUE;
                    }

                    @Override
                    public boolean isViewFromObject(View view, Object object) {
                        return view == object;
                    }

                    @Override
                    public Object instantiateItem(ViewGroup container, int position) {
                        ImageView imageView = imageViews[position % imageViews.length];
                        container.addView(imageView);
                        return imageView;
                    }

                    @Override
                    public void destroyItem(ViewGroup container, int position, Object object) {
                        container.removeView(imageViews[position % imageViews.length]);
                    }
                });

                // 使可以左右滑动
                viewPage.setCurrentItem(imageViews.length * 100);

            }
        }

        @Override
        public int getItemCount() {
            return mArticleList.size();
        }
    }
    public static class NormalViewHolder extends RecyclerView.ViewHolder {

        public TextView title;

        public TextView content;

//        public TextView classify;

        public NormalViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public ViewPager imageViewPager;
        public ViewGroup pointsViewGroup;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            imageViewPager = (ViewPager) itemView.findViewById(R.id.image_slide_page);
        }
    }

}

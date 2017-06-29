package com.example.hoang.mobies.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.fragments.NewsDetailFragment;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.NewsModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonto on 6/28/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<NewsModel> newsModelList;
    private Context context;
    private View.OnClickListener onClickListener;
    private FragmentManager fragmentManager;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public NewsAdapter(List<NewsModel> newsModelList, Context context, FragmentManager fragmentManager) {
        this.newsModelList = newsModelList;
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.setData(newsModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return newsModelList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_news_title)
        TextView tvTitle;
        @BindView(R.id.tv_news_date)
        TextView tvDate;
        @BindView(R.id.tv_news_publisher)
        TextView tvPublisher;
        @BindView(R.id.iv_news_image)
        ImageView ivImage;
        @BindView(R.id.tv_news_description)
        TextView tvDescription;
        @BindView(R.id.tv_read_more)
        TextView tvReadMore;
        View view;

        public NewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(final NewsModel newsModel) {
            if (newsModel.getDescription() == null || newsModel.getDescription().equals(""))
                tvDescription.setVisibility(View.GONE);
            else {
                System.out.printf("Description: " + newsModel.getDescription());
                tvDescription.setText(newsModel.getDescription());
            }
            if (newsModel.getPublishedAt() == null || newsModel.getPublishedAt().equals(""))
                tvDate.setVisibility(View.GONE);
            else {
                System.out.printf("Date: " + newsModel.getPublishedAt());
                tvDate.setText(newsModel.getPublishedAt());
            }
            if (newsModel.getAuthor() == null || newsModel.getAuthor().equals(""))
                tvPublisher.setVisibility(View.GONE);
            else {
                System.out.printf("Author: " + newsModel.getAuthor());
                tvPublisher.setText(newsModel.getAuthor());
            }
            if (newsModel.getTitle() == null || newsModel.getTitle().equals(""))
                tvTitle.setVisibility(View.GONE);
            else {
                System.out.printf("Title: " + newsModel.getTitle());
                tvTitle.setText(newsModel.getTitle());
            }
            tvReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("NewsDetail", newsModel);
                    newsDetailFragment.setArguments(bundle);
                    ScreenManager.openFragment(fragmentManager, newsDetailFragment, R.id.drawer_layout, true, false);
                }
            });
            Picasso.with(context).load(newsModel.getUrlToImage()).placeholder(R.drawable.no_image_movie_tv_landscape_final).into(ivImage);
            view.setTag(newsModel);
        }
    }
}

package com.example.hoang.mobies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.models.TVModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.hoang.mobies.activities.MainActivity.RATED_TV_LIST;

/**
 * Created by tonto on 6/19/2017.
 */

public class TVShowByCategoriesAdapter extends RecyclerView.Adapter<TVShowByCategoriesAdapter.TVShowByCategoriesViewHolder> {
    private List<TVModel> tv_modelList;
    private Context context;
    private View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public TVShowByCategoriesAdapter(List<TVModel> tv_modelList, Context context) {
        this.tv_modelList = tv_modelList;
        this.context = context;
    }

    @Override
    public TVShowByCategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_tvshow_by_category, parent, false);
        view.setOnClickListener(onClickListener);
        return new TVShowByCategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TVShowByCategoriesViewHolder holder, int position) {
        holder.setData(tv_modelList.get(position));
    }

    @Override
    public int getItemCount() {
        return tv_modelList.size();
    }

    public class TVShowByCategoriesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_tvsbc_image)
        ImageView ivTvbcImage;
        @BindView(R.id.tv_tvsbc_name)
        TextView tvTvbcName;
        @BindView(R.id.tv_tvsbc_rating)
        TextView tvTvbcRating;
        @BindView(R.id.tv_tvsbc_vote)
        TextView tvTVbcVote;
        View view;

        public TVShowByCategoriesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(TVModel tv_model) {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + tv_model.getPoster_path()).fit().centerCrop().placeholder(R.drawable.no_image_movie_tv_portrait_final).into(ivTvbcImage);
            tvTvbcName.setText(tv_model.getName());
            tvTVbcVote.setText(tv_model.getVote_average() + "");
            tvTvbcRating.setText(tv_model.getVote_count() + " Ratings");
            for (TVModel model : RATED_TV_LIST) {
                if (model.getId() == tv_model.getId()) {
                    tvTvbcRating.setText(String.format("%,d",tv_model.getVote_count()+1) + " Ratings");
                    break;
                }
            }
            view.setTag(tv_model);
        }
    }

}

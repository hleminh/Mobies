package com.example.hoang.mobies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.TVModel;
import com.example.hoang.mobies.network.get_people.KnownForObject;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonto on 6/21/2017.
 */

public class KnownForAdapter extends RecyclerView.Adapter<KnownForAdapter.KnownForViewHolder> {
    private List<KnownForObject> knownForObjectList;
    private Context context;
    private View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public KnownForAdapter(List<KnownForObject> knownForObjectList, Context context) {
        this.knownForObjectList = knownForObjectList;
        this.context = context;
    }

    @Override
    public KnownForViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_known_for, parent, false);
        view.setOnClickListener(onClickListener);
        return new KnownForViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return knownForObjectList.size();
    }

    @Override
    public void onBindViewHolder(KnownForViewHolder holder, int position) {
        holder.setData(knownForObjectList.get(position));
    }

    public class KnownForViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_known_for_image)
        ImageView ivImage;
        @BindView(R.id.tv_known_for_name)
        TextView tvName;
        @BindView(R.id.tv_known_for_rating)
        TextView tvRating;
        @BindView(R.id.tv_known_for_vote)
        TextView tvVote;
        View view;

        public KnownForViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(KnownForObject knownForObject) {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + knownForObject.getPoster_path()).centerCrop().fit().placeholder(R.drawable.no_image_movie_tv_portrait_final).into(ivImage);
            if (knownForObject.getMedia_type().equals("movie")) {
                tvName.setText(knownForObject.getTitle());
            } else {
                tvName.setText(knownForObject.getName());
            }
            tvVote.setText(knownForObject.getVote_average() + "");
            tvRating.setText(String.format("%,d", knownForObject.getVote_count()) + " Ratings");
            if (knownForObject.getMedia_type().equals("movie")) {
                MovieModel movieModel = new MovieModel(knownForObject);
                view.setTag(movieModel);
            } else {
                TVModel tv_model = new TVModel(knownForObject);
                view.setTag(tv_model);
            }
        }
    }
}

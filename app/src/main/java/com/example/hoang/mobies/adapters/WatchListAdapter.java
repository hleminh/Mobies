package com.example.hoang.mobies.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.databases.RealmHandle;
import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.TV_Model;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 7/2/2017.
 */

public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder> {
    private List<TV_Model> tvModelList;
    private List<MovieModel> movieModelList;
    private Context context;
    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public WatchListAdapter(Context context, @Nullable List<MovieModel> movieModels, @Nullable List<TV_Model> tvModels) {
        this.movieModelList = movieModels;
        this.tvModelList = tvModels;
        this.context = context;
    }

    @Override
    public WatchListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_watch_list, parent, false);
        view.setOnClickListener(onClickListener);
        return new WatchListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WatchListViewHolder holder, int position) {
        if (movieModelList != null) {
            holder.setData(movieModelList.get(position));
        } else {
            holder.setData(tvModelList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (movieModelList != null) {
            return movieModelList.size();
        } else
            return tvModelList.size();
    }

    public class WatchListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item_image)
        ImageView ivImage;
        @BindView(R.id.tv_item_name)
        TextView tvName;
        @BindView(R.id.tv_item_genre)
        TextView tvGenre;
        @BindView(R.id.tv_realse_date)
        TextView tvRealseDate;
        @BindView(R.id.iv_remove)
        ImageView ivRemove;
        View view;

        public WatchListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(final MovieModel movieModel) {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + movieModel.getPoster_path()).placeholder(R.drawable.no_image_movie_tv_portrait_final).into(ivImage);
            tvName.setText(movieModel.getTitle());
            tvRealseDate.setText(movieModel.getRelease_date());
            String genres = "";
            for (int i = 0; i < movieModel.getGenreIDsRealmList().size(); i++) {
                for (GenresModel genreModel : RealmHandle.getInstance().getListGenresModel()) {
                    if (genreModel.getId() == movieModel.getGenreIDsRealmList().get(i).getId()) {
                        if (i == movieModel.getGenreIDsRealmList().size() - 1) {
                            genres += genreModel.getName();
                        } else genres += genreModel.getName() + ", ";
                    }
                }
            }
            tvGenre.setText(genres);
            ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Deleted " + movieModel.getTitle(), Toast.LENGTH_SHORT).show();
                    RealmHandle.getInstance().deleteFromWatchList(movieModel);
                    notifyDataSetChanged();
                }
            });
            view.setTag(movieModel);
        }

        public void setData(final TV_Model tvModel) {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + tvModel.getPoster_path()).placeholder(R.drawable.no_image_movie_tv_portrait_final).into(ivImage);
            tvName.setText(tvModel.getName());
            tvRealseDate.setText(tvModel.getFirst_air_date());
            String genres = "";
            for (int i = 0; i < tvModel.getGenreIDsRealmList().size(); i++) {
                for (GenresModel genreModel : RealmHandle.getInstance().getListGenresModel()) {
                    if (genreModel.getId() == tvModel.getGenreIDsRealmList().get(i).getId()) {
                        if (i == tvModel.getGenreIDsRealmList().size() - 1) {
                            genres += genreModel.getName();
                        } else genres += genreModel.getName() + ", ";
                    }
                }
            }
            tvGenre.setText(genres);
            ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Deleted " + tvModel.getName(), Toast.LENGTH_SHORT).show();
                    RealmHandle.getInstance().deleteFromWatchList(tvModel);
                    notifyDataSetChanged();
                }
            });
            view.setTag(tvModel);
        }
    }
}

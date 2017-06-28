package com.example.hoang.mobies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.Utils.Utils;
import com.example.hoang.mobies.models.MultiSearchModel;
import com.example.hoang.mobies.models.PeopleModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Inpriron on 6/23/2017.
 */

public class MultiSearchAdapter extends RecyclerView.Adapter<MultiSearchAdapter.MultiSearchViewHolder> {
    private Context context;
    private List<MultiSearchModel> multiSearchModels;
    private View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public MultiSearchAdapter(Context context, List<MultiSearchModel> multiSearchModels) {
        this.context = context;
        this.multiSearchModels = multiSearchModels;
    }

    @Override
    public MultiSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_search, parent, false);
        view.setOnClickListener(onClickListener);
        return new MultiSearchAdapter.MultiSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MultiSearchViewHolder holder, int position) {
        holder.setData(multiSearchModels.get(position));
    }

    @Override
    public int getItemCount() {
        return multiSearchModels.size();
    }

    public class MultiSearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_search_image)
        ImageView ivSearchImage;
        @BindView(R.id.tv_search_name)
        TextView tvSearchName;
        @BindView(R.id.tv_media_type)
        TextView tvMediaType;

        View view;

        public MultiSearchViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }

        public void setData(MultiSearchModel multiSearchModel) {
            if (multiSearchModel.getMedia_type().equals("person")) {
                Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" +
                        multiSearchModel.getProfile_path()).placeholder(R.drawable.no_image_person_u_final).fit().into(ivSearchImage);
                tvSearchName.setText(Utils.capitalize(multiSearchModel.getName()));
                tvMediaType.setText("Person");
            } else {
                Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" +
                        multiSearchModel.getPoster_path()).placeholder(R.drawable.no_image_movie_tv_portrait_final).fit().into(ivSearchImage);
                if (multiSearchModel.getMedia_type().equals("tv")) {
                    tvSearchName.setText(Utils.capitalize(multiSearchModel.getOriginal_name()));
                    tvMediaType.setText("TV Show");
                }
                else {
                    tvSearchName.setText(Utils.capitalize(multiSearchModel.getTitle()));
                    tvMediaType.setText("Movie");
                }
            }
            view.setTag(multiSearchModel);

        }
    }
}

package com.example.hoang.mobies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.models.PeopleModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonto on 6/19/2017.
 */

public class PopularCelebAdapter extends RecyclerView.Adapter<PopularCelebAdapter.PopularCelebViewHolder> {
    private Context context;
    private List<PeopleModel> peopleModelList;
    private View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public PopularCelebAdapter(Context context, List<PeopleModel> peopleModelList) {
        this.context = context;
        this.peopleModelList = peopleModelList;
    }

    @Override
    public PopularCelebViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_celeb_by_popular, parent, false);
        view.setOnClickListener(onClickListener);
        return new PopularCelebViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularCelebViewHolder holder, int position) {
        holder.setData(peopleModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return peopleModelList.size();
    }

    public class PopularCelebViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_cbp_name)
        TextView tvCbpName;
        @BindView(R.id.iv_cbp_image)
        ImageView ivCbpImage;

        View view;

        public PopularCelebViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }

        public void setData(PeopleModel peopleModel){
            Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + peopleModel.getProfile_path()).into(ivCbpImage);
            tvCbpName.setText(peopleModel.getName());
            view.setTag(peopleModel);
        }
    }
}

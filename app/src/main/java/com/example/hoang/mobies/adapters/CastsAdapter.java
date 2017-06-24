package com.example.hoang.mobies.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.models.CastModel;
import com.example.hoang.mobies.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tonto on 6/19/2017.
 */

public class CastsAdapter extends RecyclerView.Adapter<CastsAdapter.CastViewHolder> {

    private List<CastModel> castModelList;
    private Context context;
    private View.OnClickListener onClickListener;

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.onClickListener = onItemClickListener;
    }

    public CastsAdapter(List<CastModel> castModelList, Context context) {
        this.castModelList = castModelList;
        this.context = context;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_cast, parent, false);
        view.setOnClickListener(onClickListener);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.setData(castModelList.get(position));
        if (position % 2 == 0)
            holder.bgCast.setBackgroundColor(context.getResources().getColor(R.color.colorCastBackground));
    }

    @Override
    public int getItemCount() {
        return castModelList.size();
    }

    public class CastViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_cast_picture)
        ImageView ivCastImage;
        @BindView(R.id.tv_cast_name)
        TextView tvCastName;
        @BindView(R.id.tv_character_name)
        TextView tvCharacterName;
        @BindView(R.id.bg_cast)
        LinearLayout bgCast;
        View view;

        public CastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            view = itemView;
        }

        public void setData(CastModel castModel) {
            Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + castModel.getProfile_path()).into(ivCastImage);
            tvCastName.setText(castModel.getName());
            tvCharacterName.setText(castModel.getCharacter());
            view.setTag(castModel);
        }
    }
}



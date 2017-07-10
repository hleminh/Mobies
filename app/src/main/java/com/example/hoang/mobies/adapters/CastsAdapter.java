package com.example.hoang.mobies.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.fragments.CelebDetailFragment;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.CastModel;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.MultiSearchModel;
import com.example.hoang.mobies.models.PeopleModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_search.GetMultiSearchService;
import com.example.hoang.mobies.network.get_search.MainSearchModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;
import static com.example.hoang.mobies.network.RetrofitFactory.DEFAULT_PAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.LANGUAGE;

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
            if (castModel.getGender() == 1)
                Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + castModel.getProfile_path()).fit().centerCrop().placeholder(R.drawable.no_image_person_f_final).into(ivCastImage);
            else if (castModel.getGender() == 2)
                Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + castModel.getProfile_path()).fit().centerCrop().placeholder(R.drawable.no_image_person_m_final).into(ivCastImage);
            else
                Picasso.with(context).load("http://image.tmdb.org/t/p/w342/" + castModel.getProfile_path()).fit().centerCrop().placeholder(R.drawable.no_image_person_u_final_2).into(ivCastImage);

            tvCastName.setText(castModel.getName());
            tvCharacterName.setText(castModel.getCharacter());
            view.setTag(castModel);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GetMultiSearchService getMultiSearchService = RetrofitFactory.getInstance().createService(GetMultiSearchService.class);
                    getMultiSearchService.getMultiSearch(castModel.getName(), API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainSearchModel>() {
                        @Override
                        public void onResponse(Call<MainSearchModel> call, Response<MainSearchModel> response) {
                            MainSearchModel mainSearchModel = response.body();
                            if (mainSearchModel != null) {
                                for (MultiSearchModel searchModel : mainSearchModel.getResults()) {
                                    if (searchModel.getMedia_type().equals("person")) {
                                        if (searchModel.getId() == castModel.getId()) {
                                            CelebDetailFragment celebDetailFragment = new CelebDetailFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("CelebDetail", new PeopleModel(searchModel));
                                            bundle.putBoolean("FromSearch", true);
                                            celebDetailFragment.setArguments(bundle);
                                            ScreenManager.openFragment(((AppCompatActivity) context).getSupportFragmentManager(), celebDetailFragment, R.id.drawer_layout, true, false);
                                            return;
                                        }
                                    }
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<MainSearchModel> call, Throwable t) {
                            Toast.makeText(context, "Bad connection", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}



package com.example.hoang.mobies.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.adapters.KnownForAdapter;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.PeopleModel;
import com.example.hoang.mobies.models.TV_Model;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_people.GetDetailPeopleService;
import com.example.hoang.mobies.network.get_people.KnownForObject;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class CelebDetailFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.iv_poster_celeb_detail)
    ImageView ivPoster;
    @BindView(R.id.tv_celeb_name_celeb_detail)
    TextView tvCelebName;
    @BindView(R.id.tv_celeb_dob_celeb_detail)
    TextView tvCelebDoB;
    @BindView(R.id.tv_celeb_dod_celeb_detail)
    TextView tvCelebDoD;
    @BindView(R.id.iv_back_drop_celeb_detail)
    ImageView ivBackDrop;
    @BindView(R.id.tv_celeb_pob_celeb_detail)
    TextView tvCelebPoB;
    @BindView(R.id.tv_celeb_gender_celeb_detail)
    TextView tvCelebGender;
    @BindView(R.id.tv_aka)
    TextView tvAKA;
    @BindView(R.id.rv_casts)
    RecyclerView rvCasts;
    @BindView(R.id.tv_genre)
    TextView tvGenre;
    @BindView(R.id.toolbar)
    Toolbar tbDetail;
    @BindView(R.id.tv_celeb_dob_slash)
    TextView tvDoBSlash;
    @BindView(R.id.tv_no_known_for)
    TextView tvNoKnownFor;
    @BindView(R.id.tv_known_for)
    TextView tvKnownFor;
    @BindView(R.id.tv_also_known_as)
    TextView tvAlsoKA;
    @BindView(R.id.tv_biography)
    TextView tvBiography;
    private PeopleModel peopleModel;
    private PeopleModel celebModel;


    public CelebDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_celeb_detail, container, false);
        peopleModel = (PeopleModel) getArguments().getSerializable("CelebDetail");
        loadData();
        ButterKnife.bind(this, view);
        tbDetail.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        tbDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getActivity().getSupportFragmentManager());
            }
        });
        return view;
    }


    private void setupUI() {
        String gender = celebModel.getGender() == 1 ? "Female" : celebModel.getGender() == 2 ? "Male" : "Unspecified";
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + peopleModel.getKnown_for().get(0).getBackdrop_path()).placeholder(R.drawable.no_image_movie_tv_landscape_final).fit().into(ivBackDrop);
        if (gender.equals("Male"))
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + celebModel.getProfile_path()).placeholder(R.drawable.no_image_person_m_final).fit().into(ivPoster);
        else if (gender.equals("Female"))
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + celebModel.getProfile_path()).placeholder(R.drawable.no_image_person_f_final).fit().into(ivPoster);
        else
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + celebModel.getProfile_path()).placeholder(R.drawable.no_image_person_u_final_2).fit().into(ivPoster);

        tvCelebName.setText(celebModel.getName());
        tvCelebDoB.setText(celebModel.getBirthday());
        if (celebModel.getDeathday() != null) {
            tvCelebDoD.setText(celebModel.getDeathday());
            tvDoBSlash.setVisibility(View.VISIBLE);
        }
        tvCelebPoB.setText(celebModel.getPlace_of_birth());
        if (celebModel.getBiography() != null) {
            tvGenre.setText(celebModel.getBiography());
        } else {
            tvGenre.setText("We don't have a biography for " + celebModel.getName());
        }


        tvCelebGender.setText(gender);
        String knownAs = "";
        for (String aka : celebModel.getAlso_known_as()) {
            System.out.println(aka);
            knownAs += aka;
            if (!aka.equals(celebModel.getAlso_known_as().get(celebModel.getAlso_known_as().size() - 1)))
                knownAs += '\n';
        }
        if (knownAs.trim().equals("")) {
            tvAKA.setText("-");
        } else
            tvAKA.setText(knownAs);

        KnownForAdapter knownForAdapter = new KnownForAdapter(peopleModel.getKnown_for(), getContext());
        rvCasts.setAdapter(knownForAdapter);
        rvCasts.setNestedScrollingEnabled(false);
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(rvCasts);

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), GridLayoutManager.HORIZONTAL, false);
        rvCasts.setLayoutManager(manager);
        knownForAdapter.setOnItemClickListener(this);

        if (peopleModel.getKnown_for().size() == 0) {
            tvNoKnownFor.setVisibility(View.VISIBLE);
            rvCasts.setVisibility(View.GONE);
        }
    }

    private void loadData() {
        GetDetailPeopleService getDetailPeopleService = RetrofitFactory.getInstance().createService(GetDetailPeopleService.class);
        getDetailPeopleService.getDetailPeople(peopleModel.getId(), API_KEY).enqueue(new Callback<PeopleModel>() {
            @Override
            public void onResponse(Call<PeopleModel> call, Response<PeopleModel> response) {
                tvAlsoKA.setVisibility(View.VISIBLE);
                tvKnownFor.setVisibility(View.VISIBLE);
                tvBiography.setVisibility(View.VISIBLE);
                celebModel = response.body();
                setupUI();

            }

            @Override
            public void onFailure(Call<PeopleModel> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getActivity().getWindow();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() instanceof MovieModel) {
            MovieModel movieModel = (MovieModel) v.getTag();
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MovieDetail", movieModel);
            movieDetailFragment.setArguments(bundle);
            ScreenManager.openFragment(getFragmentManager(), movieDetailFragment, R.id.drawer_layout, true, false);
        }

        if (v.getTag() instanceof TV_Model) {
            TV_Model tvModel = (TV_Model) v.getTag();
            TVShowDetailFragment tvShowDetailFragment = new TVShowDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("TVDetail", tvModel);
            tvShowDetailFragment.setArguments(bundle);
            ScreenManager.openFragment(getFragmentManager(), tvShowDetailFragment, R.id.drawer_layout, true, false);
        }
    }

}

package com.example.hoang.mobies.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.Utils.Utils;
import com.example.hoang.mobies.activities.MainActivity;
import com.example.hoang.mobies.adapters.CastsAdapter;
import com.example.hoang.mobies.adapters.MoviesByCategoriesAdapter;
import com.example.hoang.mobies.dialogs.RateDialog;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.CastModel;
import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_cast.GetCastOfAMovieService;
import com.example.hoang.mobies.network.get_cast.MainCastObject;
import com.example.hoang.mobies.network.get_movies.GetRecommendMovieService;
import com.example.hoang.mobies.network.get_movies.MainObject;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;
import static com.example.hoang.mobies.activities.MainActivity.RATED_MOVIE_LIST;
import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;
import static com.example.hoang.mobies.network.RetrofitFactory.DEFAULT_PAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.LANGUAGE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.iv_poster_movie_detail)
    ImageView ivPoster;
    @BindView(R.id.tv_movie_name_movie_detail)
    TextView tvMovieName;
    @BindView(R.id.tv_movie_release_date_movie_detail)
    TextView tvMovieReleaseDate;
    @BindView(R.id.rb_movie_detail)
    RatingBar rbMovieDetail;
    @BindView(R.id.tv_rating_detail)
    TextView tvRatingDetail;
    @BindView(R.id.iv_back_drop_movie_detail)
    ImageView ivBackDrop;
    @BindView(R.id.tv_genre)
    TextView tvGenre;
    @BindView(R.id.rv_casts)
    RecyclerView rvCasts;
    private MovieModel movieModel;
    @BindView(R.id.tv_plot)
    TextView tvPlot;
    @BindView(R.id.rv_recommended)
    RecyclerView rvRecommended;
    @BindView(R.id.toolbar)
    Toolbar tbDetail;
    @BindView(R.id.ll_rate)
    LinearLayout llRate;
    @BindView(R.id.tv_full_cast)
    TextView tvFullCast;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.iv_rate)
    ImageView ivRate;
    @BindView(R.id.tv_no_cast)
    TextView tvNoCast;
    @BindView(R.id.tv_no_recommended)
    TextView tvNoRecommended;
    private List<CastModel> castModelList;
    private List<MovieModel> movieModelList;
    private MoviesByCategoriesAdapter moviesByCategoriesAdapter;
    private CastsAdapter castsAdapter;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        movieModel = (MovieModel) getArguments().getSerializable("MovieDetail");
        loadData();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + movieModel.getPoster_path()).placeholder(R.drawable.no_image_movie_tv_portrait_final).fit().into(ivPoster);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + movieModel.getBackdrop_path()).placeholder(R.drawable.no_image_movie_tv_landscape_final).fit().into(ivBackDrop);
        tvMovieName.setText(movieModel.getTitle());
        tvRatingDetail.setText(String.format("%,d", movieModel.getVote_count()) + " Ratings");
        rbMovieDetail.setRating(movieModel.getVote_average() / 2);
        tvMovieReleaseDate.setText(movieModel.getRelease_date());
        if (movieModel.getOverview() != null) {
            tvPlot.setText(movieModel.getOverview());
        } else {
            tvPlot.setText("-");
        }

        String genres = "";
        for (int i = 0; i < movieModel.getGenre_ids().size(); i++) {
            for (GenresModel genreModel : Utils.genresModelList) {
                if (genreModel.getId() == movieModel.getGenre_ids().get(i).intValue()) {
                    if (i == movieModel.getGenre_ids().size() - 1) {
                        genres += genreModel.getName();
                    } else genres += genreModel.getName() + ", ";
                }
            }
        }
        if (genres.trim().equals("")) {
            tvGenre.setText("-");
        } else
            tvGenre.setText(genres);

        castsAdapter = new CastsAdapter(castModelList, getContext());
        rvCasts.setAdapter(castsAdapter);


        moviesByCategoriesAdapter = new MoviesByCategoriesAdapter(movieModelList, getContext());
        moviesByCategoriesAdapter.setOnItemClickListener(this);
        rvRecommended.setAdapter(moviesByCategoriesAdapter);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvRecommended.setLayoutManager(linearLayoutManager1);
        rvCasts.setLayoutManager(linearLayoutManager2);

        rvCasts.setNestedScrollingEnabled(false);
        rvRecommended.setNestedScrollingEnabled(false);

        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(rvRecommended);

        tbDetail.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        tbDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenManager.backFragment(getActivity().getSupportFragmentManager());
            }
        });

        llRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateDialog rateDialog = new RateDialog(getContext(), movieModel.getId());
                rateDialog.show();
            }
        });

        tvFullCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullCastFragment fullCastFragment = new FullCastFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("MovieDetail", movieModel);
                fullCastFragment.setArguments(bundle);
                ScreenManager.openFragment(getFragmentManager(), fullCastFragment, R.id.drawer_layout, true, false);
            }
        });

        for (MovieModel model : RATED_MOVIE_LIST) {
            if (model.getId() == this.movieModel.getId()) {
                tvRate.setText("Your rating: " + model.getRating() + "/10");
                ivRate.setImageResource(R.drawable.ic_star_black_24dp);
                break;
            }
        }
    }

    private void loadData() {
        castModelList = new ArrayList<>();
        movieModelList = new ArrayList<>();
        loadCasts();
        loadRecommended();
    }

    private void loadRecommended() {
        GetRecommendMovieService getRecommendMovieService = RetrofitFactory.getInstance().createService(GetRecommendMovieService.class);
        getRecommendMovieService.getRecommendMovies(movieModel.getId(), API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                for (MovieModel movieModel : response.body().getResults()) {
                    movieModelList.add(movieModel);
                }
                moviesByCategoriesAdapter.notifyDataSetChanged();
                if (movieModelList.size() == 0) {
                    tvNoRecommended.setVisibility(View.VISIBLE);
                    rvRecommended.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadCasts() {
        GetCastOfAMovieService getCastOfAMovieService = RetrofitFactory.getInstance().createService(GetCastOfAMovieService.class);
        getCastOfAMovieService.getCastOfAMovie(movieModel.getId(), API_KEY).enqueue(new Callback<MainCastObject>() {
            @Override
            public void onResponse(Call<MainCastObject> call, Response<MainCastObject> response) {
                MainCastObject mainCastObject = response.body();
                List<CastModel> castModels = mainCastObject.getCast();
                for (CastModel castModel : castModels) {
                    if (castModelList.size() < 5)
                        castModelList.add(castModel);
                }
                castsAdapter.notifyDataSetChanged();

                if (castModelList.size() == 0) {
                    tvFullCast.setVisibility(View.GONE);
                    tvNoCast.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MainCastObject> call, Throwable t) {
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
    }

    @Subscribe
    public void onEvent(Float rating) {
        // your implementation

        tvRate.setText("Your rating: " + rating + "/10");
        ivRate.setImageResource(R.drawable.ic_star_black_24dp);
    }

}

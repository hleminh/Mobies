package com.example.hoang.mobies.fragments;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.Utils.Utils;
import com.example.hoang.mobies.adapters.CastsAdapter;
import com.example.hoang.mobies.adapters.MoviesByCategoriesAdapter;
import com.example.hoang.mobies.databases.RealmHandle;
import com.example.hoang.mobies.dialogs.RateDialog;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.CastModel;
import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_cast.GetCastOfAMovieService;
import com.example.hoang.mobies.network.get_cast.MainCastObject;
import com.example.hoang.mobies.network.get_movies.GetRecommendMovieService;
import com.example.hoang.mobies.network.get_movies.GetTrailerService;
import com.example.hoang.mobies.network.get_movies.MainObject;
import com.example.hoang.mobies.network.get_movies.MainTrailerObject;
import com.example.hoang.mobies.network.get_movies.TrailerObject;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.activities.MainActivity.RATED_MOVIE_LIST;
import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;
import static com.example.hoang.mobies.network.RetrofitFactory.DEFAULT_PAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.LANGUAGE;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.tv_add_watch_list)
    TextView tvAddWatchList;
    @BindView(R.id.iv_add_watch_list)
    ImageView ivAddWatchList;
    @BindView(R.id.ll_add_watch_list)
    LinearLayout llAddWatchList;
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
    @BindView(R.id.pb_progress_cast)
    ProgressBar pbProgressCast;
    @BindView(R.id.pb_progress_recommended)
    ProgressBar pbProgressRecommended;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;
    private List<CastModel> castModelList;
    private List<MovieModel> movieModelList;
    private List<GenresModel> genresModelList = new ArrayList<>();
    private MoviesByCategoriesAdapter moviesByCategoriesAdapter;
    private CastsAdapter castsAdapter;
    private List<String> keys = new ArrayList<>();
    private YouTubePlayer player;
    private Toast toast;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        if (getArguments() != null)
            movieModel = (MovieModel) getArguments().getSerializable("MovieDetail");
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        ButterKnife.bind(this, view);
        loadData();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        llAddWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RealmHandle.getInstance().isAdded(movieModel)) {
                    if (toast != null) toast.cancel();
                    toast = Toast.makeText(getContext(), "This movie is already added to the Watch List", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    RealmHandle.getInstance().addToWatchList(movieModel);
                    if (toast != null) toast.cancel();
                    toast = Toast.makeText(getContext(), "Added to Watch List", Toast.LENGTH_SHORT);
                    toast.show();
                    tvAddWatchList.setText("Added to Watch List");
                    ivAddWatchList.setImageResource(R.drawable.bookmark_check);
                }
            }
        });
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + movieModel.getPoster_path()).placeholder(R.drawable.no_image_movie_tv_portrait_final).fit().into(ivPoster);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + movieModel.getBackdrop_path()).placeholder(R.drawable.no_image_movie_tv_landscape_final).fit().into(ivBackDrop);
        tvMovieName.setText(movieModel.getTitle());
        tvRatingDetail.setText(String.format("%,d", movieModel.getVote_count()) + " Ratings");
        rbMovieDetail.setRating(movieModel.getVote_average() / 2);
        tvMovieReleaseDate.setText(movieModel.getRelease_date());
        if (movieModel.getOverview() != null) {
            if (movieModel.getOverview().trim().equals("")) {
                tvPlot.setText("-");
            } else {
                tvPlot.setText(movieModel.getOverview());
            }
        } else {
            tvPlot.setText("-");
        }

        if (movieModel.getGenresString() == null) {
            String genres = "";
            for (int i = 0; i < movieModel.getGenre_ids().size(); i++) {
                for (GenresModel genreModel : RealmHandle.getInstance().getListGenresModel()) {
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
        } else {
            if (movieModel.getGenresString().trim().equals("")) {
                tvGenre.setText("-");
            } else
                tvGenre.setText(movieModel.getGenresString());
        }


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
                RateDialog rateDialog = new RateDialog(getContext(), movieModel.getId(), true);
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

                tvRate.setText("Your rating: " + (int) model.getRating() + "/10");
                ivRate.setImageResource(R.drawable.ic_star_black_24dp);
                tvRatingDetail.setText(String.format("%,d", movieModel.getVote_count() + 1) + " Ratings");
                break;
            }
        }

        if (RealmHandle.getInstance().isAdded(movieModel)) {
            tvAddWatchList.setText("Added to Watch List");
            ivAddWatchList.setImageResource(R.drawable.bookmark_check);
        }

    }

    private void loadData() {
        castModelList = new ArrayList<>();
        movieModelList = new ArrayList<>();
        loadCasts();
        loadRecommended();
        loadTrailer();
    }

    private void loadTrailer() {
        GetTrailerService getTrailerService = RetrofitFactory.getInstance().createService(GetTrailerService.class);
        getTrailerService.getMovieTrailer(movieModel.getId(), API_KEY, LANGUAGE).enqueue(new Callback<MainTrailerObject>() {
            @Override
            public void onResponse(Call<MainTrailerObject> call, Response<MainTrailerObject> response) {
                if (response.body() != null) {
                    if (response.body().getResults() != null) {
                        for (TrailerObject trailerObject : response.body().getResults()) {
                            keys.add(trailerObject.getKey());
                        }


                        floatingActionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                        YoutubePlayFragment youTubePlayerFragment = new YoutubePlayFragment();
//                        Bundle bundle = new Bundle();
//                        ArrayList<String> urls = new ArrayList<String>();
//                        urls.addAll(keys);
//                        bundle.putStringArrayList("keys", urls);
//                        youTubePlayerFragment.setArguments(bundle);
                                YouTubePlayerSupportFragment youTubePlayerFragment = new YouTubePlayerSupportFragment().newInstance();
                                youTubePlayerFragment.initialize(Utils.getYoutubeKey(), new YouTubePlayer.OnInitializedListener() {
                                    @Override
                                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                        player = youTubePlayer;
                                        player.setFullscreen(true);
                                        player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE | FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);
                                        player.loadVideos(keys);
                                        player.play();
                                    }

                                    @Override
                                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                                    }
                                });
                                ScreenManager.openFragment(getFragmentManager(), youTubePlayerFragment, R.id.drawer_layout, true, false);

                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<MainTrailerObject> call, Throwable t) {
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (toast != null) toast.cancel();
                        toast = Toast.makeText(getContext(), "Sorry this movie doesn't have trailer yet :'(", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                });
            }
        });
    }

    private void loadRecommended() {
        System.out.println(movieModel.getId());
        GetRecommendMovieService getRecommendMovieService = RetrofitFactory.getInstance().createService(GetRecommendMovieService.class);
        getRecommendMovieService.getRecommendMovies(movieModel.getId(), API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainObject>() {
            @Override
            public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                if (response.body() != null) {
                    if (response.body().getResults() != null) {
                        for (MovieModel movieModel : response.body().getResults()) {
                            movieModelList.add(movieModel);
                        }
                        moviesByCategoriesAdapter.notifyDataSetChanged();
                        pbProgressRecommended.setVisibility(View.GONE);
                        rvRecommended.setVisibility(View.VISIBLE);
                        if (movieModelList.size() == 0) {
                            tvNoRecommended.setVisibility(View.VISIBLE);
                            rvRecommended.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MainObject> call, Throwable t) {
                if (toast != null) toast.cancel();
                toast = Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT);
                toast.show();
                pbProgressRecommended.setVisibility(View.GONE);
                tvNoRecommended.setVisibility(View.VISIBLE);
                tvNoRecommended.setText("No connection");
            }
        });
    }

    private void loadCasts() {
        GetCastOfAMovieService getCastOfAMovieService = RetrofitFactory.getInstance().createService(GetCastOfAMovieService.class);
        getCastOfAMovieService.getCastOfAMovie(movieModel.getId(), API_KEY).enqueue(new Callback<MainCastObject>() {
            @Override
            public void onResponse(Call<MainCastObject> call, Response<MainCastObject> response) {
                if (response.body() != null) {
                    MainCastObject mainCastObject = response.body();
                    List<CastModel> castModels = mainCastObject.getCast();
                    for (CastModel castModel : castModels) {
                        if (castModelList.size() < 5)
                            castModelList.add(castModel);
                    }
                    castsAdapter.notifyDataSetChanged();
                    tvFullCast.setVisibility(View.VISIBLE);
                    pbProgressCast.setVisibility(View.GONE);
                    rvCasts.setVisibility(View.VISIBLE);
                    if (castModelList.size() == 0) {
                        rvCasts.setVisibility(View.GONE);
                        tvFullCast.setVisibility(View.GONE);
                        tvNoCast.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<MainCastObject> call, Throwable t) {
                if (toast != null) toast.cancel();
                toast = Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT);
                toast.show();
                pbProgressCast.setVisibility(View.GONE);
                tvNoCast.setVisibility(View.VISIBLE);
                tvFullCast.setVisibility(View.GONE);
                tvNoCast.setText("No connection");
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
        ((DrawerLayout) getActivity().findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onStop() {
        super.onStop();
//        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getActivity().getWindow();
        }
        ((DrawerLayout) getActivity().findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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
        float x = rating;
        tvRate.setText("Your rating: " + (int) x + "/10");
        ivRate.setImageResource(R.drawable.ic_star_black_24dp);
        tvRatingDetail.setText(String.format("%,d", movieModel.getVote_count() + 1) + " Ratings");
    }

    @Subscribe(sticky = true)
    public void onReceiveMovieModel(MovieModel movieModel) {
        if (this.movieModel == null) {
            this.movieModel = movieModel;
        }
    }

}

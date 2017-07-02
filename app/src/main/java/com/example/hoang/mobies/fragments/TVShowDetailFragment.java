package com.example.hoang.mobies.fragments;


import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.Utils.Utils;
import com.example.hoang.mobies.adapters.CastsAdapter;
import com.example.hoang.mobies.adapters.TVShowByCategoriesAdapter;
import com.example.hoang.mobies.databases.RealmHandle;
import com.example.hoang.mobies.dialogs.RateDialog;
import com.example.hoang.mobies.managers.ScreenManager;
import com.example.hoang.mobies.models.CastModel;
import com.example.hoang.mobies.models.GenresModel;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.models.TVModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.get_cast.GetCastTvService;
import com.example.hoang.mobies.network.get_cast.MainCastObject;
import com.example.hoang.mobies.network.get_movies.GetTrailerService;
import com.example.hoang.mobies.network.get_movies.MainTrailerObject;
import com.example.hoang.mobies.network.get_movies.TrailerObject;
import com.example.hoang.mobies.network.get_tv.GetRecommendTvService;
import com.example.hoang.mobies.network.get_tv.MainTvObject;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
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

import static com.example.hoang.mobies.activities.MainActivity.RATED_TV_LIST;
import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;
import static com.example.hoang.mobies.network.RetrofitFactory.DEFAULT_PAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.LANGUAGE;
import static com.example.hoang.mobies.network.RetrofitFactory.retrofitFactory;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI;

/**
 * A simple {@link Fragment} subclass.
 */
public class TVShowDetailFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.tv_add_watch_list)
    TextView tvAddWatchList;
    @BindView(R.id.iv_poster_tvshow_detail)
    ImageView ivPoster;
    @BindView(R.id.tv_tvshow_name_tvshow_detail)
    TextView tvTvShowName;
    @BindView(R.id.iv_add_watch_list)
    ImageView ivAddWatchList;
    @BindView(R.id.tv_tvshow_release_date_tvshow_detail)
    TextView tvTvShowReleaseDate;
    @BindView(R.id.rb_tvshow_detail)
    RatingBar rbTvShowDetail;
    @BindView(R.id.tv_rating_detail)
    TextView tvRatingDetail;
    @BindView(R.id.iv_back_drop_tvshow_detail)
    ImageView ivBackDrop;
    @BindView(R.id.tv_genre)
    TextView tvGenre;
    @BindView(R.id.rv_casts)
    RecyclerView rvCasts;
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
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.iv_rate)
    ImageView ivRate;
    private TVModel tvModel;
    private List<CastModel> castModelList;
    private List<TVModel> tv_modelList;
    private TVShowByCategoriesAdapter tvShowByCategoriesAdapter;
    private CastsAdapter castsAdapter;
    private List<String> keys = new ArrayList<>();
    private YouTubePlayer player;

    public TVShowDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_tvshow_detail, container, false);
        if (getArguments() != null) {
            tvModel = (TVModel) getArguments().getSerializable("TVDetail");
        }
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        loadData();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        ButterKnife.bind(this, view);
        tvAddWatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RealmHandle.getInstance().isAdded(tvModel)) {
                    Toast.makeText(getContext(), "This tv show is already added to the Watch List", Toast.LENGTH_SHORT).show();
                } else {
                    RealmHandle.getInstance().addToWatchList(tvModel);
                    Toast.makeText(getContext(), "Added to Watch list", Toast.LENGTH_SHORT).show();
                    tvAddWatchList.setText("Added to Watch List");
                    ivAddWatchList.setImageResource(R.drawable.bookmark_check);
                }
            }
        });
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + tvModel.getPoster_path()).placeholder(R.drawable.no_image_movie_tv_portrait_final).fit().into(ivPoster);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/original/" + tvModel.getBackdrop_path()).placeholder(R.drawable.no_image_movie_tv_landscape_final).fit().into(ivBackDrop);
        tvTvShowName.setText(tvModel.getName());
        tvRatingDetail.setText(String.format("%,d", tvModel.getVote_count()) + " Ratings");
        for (TVModel model : RATED_TV_LIST) {
            if (model.getId() == tvModel.getId()) {
                tvRatingDetail.setText(String.format("%,d", tvModel.getVote_count() + 1) + " Ratings");
                break;
            }
        }
        if (RealmHandle.getInstance().isAdded(tvModel)) {
            tvAddWatchList.setText("Added to Watch List");
            ivAddWatchList.setImageResource(R.drawable.bookmark_check);
        }
        rbTvShowDetail.setRating(tvModel.getVote_average() / 2);
        tvTvShowReleaseDate.setText(tvModel.getFirst_air_date());
        if (tvModel.getOverview() != null) {
            if (tvModel.getOverview().trim().equals("")) {
                tvPlot.setText("-");
            } else {
                tvPlot.setText(tvModel.getOverview());
            }
        } else {
            tvPlot.setText("-");
        }

        if (tvModel.getGenresString() == null) {
            String genres = "";
            for (int i = 0; i < tvModel.getGenre_ids().size(); i++) {
                for (GenresModel genreModel : RealmHandle.getInstance().getListGenresModel()) {
                    if (genreModel.getId() == tvModel.getGenre_ids().get(i).intValue()) {
                        if (i == tvModel.getGenre_ids().size() - 1) {
                            genres += genreModel.getName();
                        } else genres += genreModel.getName() + ", ";
                    }
                }
            }
            if (genres.trim().equals("")) {
                tvGenre.setText("-");
            } else {
                if (genres.trim().charAt(genres.trim().length() - 1) == ',') {
                    tvGenre.setText(genres.trim().substring(0, genres.length() - 2));
                } else
                    tvGenre.setText(genres);
            }
        } else {
            if (tvModel.getGenresString().trim().equals("")) {
                tvGenre.setText("-");
            } else {
                if (tvModel.getGenresString().trim().charAt(tvModel.getGenresString().trim().length() - 1) == ',') {
                    tvGenre.setText(tvModel.getGenresString().trim().substring(0, tvModel.getGenresString().length() - 2));
                } else
                    tvGenre.setText(tvModel.getGenresString());
            }
        }


        castsAdapter = new CastsAdapter(castModelList, getContext());
        rvCasts.setAdapter(castsAdapter);


        tvShowByCategoriesAdapter = new TVShowByCategoriesAdapter(tv_modelList, getContext());
        tvShowByCategoriesAdapter.setOnItemClickListener(this);
        rvRecommended.setAdapter(tvShowByCategoriesAdapter);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        rvRecommended.setLayoutManager(linearLayoutManager1);
        rvCasts.setLayoutManager(linearLayoutManager2);

        rvRecommended.setNestedScrollingEnabled(false);
        rvCasts.setNestedScrollingEnabled(false);

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
                RateDialog rateDialog = new RateDialog(getContext(), tvModel.getId(), false);
                rateDialog.show();
            }
        });

        tvFullCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullCastFragment fullCastFragment = new FullCastFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("TVDetail", tvModel);
                fullCastFragment.setArguments(bundle);
                ScreenManager.openFragment(getFragmentManager(), fullCastFragment, R.id.drawer_layout, true, false);
            }
        });
    }


    private void loadData() {
        castModelList = new ArrayList<>();
        tv_modelList = new ArrayList<>();
        loadCasts();
        loadRecommended();
        loadTrailer();
    }

    private void loadTrailer() {
        GetTrailerService getTrailerService = retrofitFactory.getInstance().createService(GetTrailerService.class);
        getTrailerService.getTVTrailer(tvModel.getId(), API_KEY, LANGUAGE).enqueue(new Callback<MainTrailerObject>() {
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
                                        Toast.makeText(getContext(), "Sorry this movie doesn't have trailer yet :'(", Toast.LENGTH_SHORT);

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
                        Toast.makeText(getContext(), "Sorry this movie doesn't have trailer yet :'(", Toast.LENGTH_SHORT);

                    }
                });
            }
        });
    }

    private void loadRecommended() {
        GetRecommendTvService getRecommendTvService = RetrofitFactory.getInstance().createService(GetRecommendTvService.class);
        getRecommendTvService.getRecommendTv(tvModel.getId(), API_KEY, LANGUAGE, DEFAULT_PAGE).enqueue(new Callback<MainTvObject>() {
            @Override
            public void onResponse(Call<MainTvObject> call, Response<MainTvObject> response) {
                MainTvObject mainTvObject = response.body();
                for (TVModel tv_model : mainTvObject.getResults()) {
                    tv_modelList.add(tv_model);
                }
                tvShowByCategoriesAdapter.notifyDataSetChanged();
                pbProgressRecommended.setVisibility(View.GONE);
                rvRecommended.setVisibility(View.VISIBLE);
                if (tv_modelList.size() == 0) {
                    rvRecommended.setVisibility(View.GONE);
                    tvNoRecommended.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MainTvObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                pbProgressRecommended.setVisibility(View.GONE);
                tvNoRecommended.setVisibility(View.VISIBLE);
                tvNoRecommended.setText("No connection");
            }
        });
    }

    private void loadCasts() {
        GetCastTvService getCastTvService = RetrofitFactory.getInstance().createService(GetCastTvService.class);
        getCastTvService.getCastOfAMovie(tvModel.getId(), API_KEY).enqueue(new Callback<MainCastObject>() {
            @Override
            public void onResponse(Call<MainCastObject> call, Response<MainCastObject> response) {
                for (CastModel castModel : response.body().getCast()) {
                    if (castModelList.size() < 5)
                        castModelList.add(castModel);
                }
                castsAdapter.notifyDataSetChanged();
                tvFullCast.setVisibility(View.VISIBLE);
                pbProgressCast.setVisibility(View.GONE);
                rvCasts.setVisibility(View.VISIBLE);
                if (castModelList.size() == 0) {
                    tvFullCast.setVisibility(View.GONE);
                    tvNoCast.setVisibility(View.VISIBLE);
                    rvCasts.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MainCastObject> call, Throwable t) {
                Toast.makeText(getContext(), "Bad connection", Toast.LENGTH_SHORT).show();
                pbProgressCast.setVisibility(View.GONE);
                tvFullCast.setVisibility(View.GONE);
                tvNoCast.setVisibility(View.VISIBLE);
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
        if (v.getTag() instanceof TVModel) {
            TVModel tvModel = (TVModel) v.getTag();
            TVShowDetailFragment tvShowDetailFragment = new TVShowDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("TVDetail", tvModel);
            tvShowDetailFragment.setArguments(bundle);
            ScreenManager.openFragment(getFragmentManager(), tvShowDetailFragment, R.id.drawer_layout, true, false);
        }
    }

    @Subscribe
    public void onEvent(Float rating) {
        Log.d("receive", "receive");
        float x = rating;
        tvRate.setText("Your rating: " + (int) x + "/10");
        ivRate.setImageResource(R.drawable.ic_star_black_24dp);
        tvRatingDetail.setText(String.format("%,d", tvModel.getVote_count() + 1) + " Ratings");
    }

    @Subscribe(sticky = true)
    public void onReceiveMovieModel(TVModel tvModel) {
        if (this.tvModel == null) {
            this.tvModel = tvModel;
        }
    }

}

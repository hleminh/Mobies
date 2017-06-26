package com.example.hoang.mobies.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.models.MovieModel;
import com.example.hoang.mobies.network.RetrofitFactory;
import com.example.hoang.mobies.network.guest_session.CreateGuestSessionService;
import com.example.hoang.mobies.network.guest_session.GuestObject;
import com.example.hoang.mobies.network.rate.RateMovieRequest;
import com.example.hoang.mobies.network.rate.RateMoviesResponse;
import com.example.hoang.mobies.network.rate.RateMoviesService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.hoang.mobies.activities.MainActivity.*;
import static com.example.hoang.mobies.network.RetrofitFactory.API_KEY;
import static com.example.hoang.mobies.network.RetrofitFactory.GUEST_ID;
import static com.example.hoang.mobies.network.RetrofitFactory.GUEST_ID_PREFERENCE;
import static com.example.hoang.mobies.network.RetrofitFactory.SHAREED_PREFERENCES;

/**
 * Created by Hoang on 6/24/2017.
 */

public class RateDialog extends Dialog implements View.OnClickListener {
    @BindView(R.id.rb_dialog)
    RatingBar rbDialog;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.tv_dialog)
    TextView tvDialog;
    float rating;
    int movieID;
    public RateDialog(@NonNull Context context, int movieID) {
        super(context);
        this.movieID=movieID;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_rate);
        ButterKnife.bind(this);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        rbDialog.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                tvDialog.setText((int)(rating*2)+"");
                RateDialog.this.rating = rating*2;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                createGuestAndRate();
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
        }
    }



    private void createGuestAndRate() {
        if (GUEST_ID == null) {
            CreateGuestSessionService createGuestSessionService = RetrofitFactory.getInstance().createService(CreateGuestSessionService.class);
            createGuestSessionService.getNewGuest(API_KEY).enqueue(new Callback<GuestObject>() {
                @Override
                public void onResponse(Call<GuestObject> call, Response<GuestObject> response) {
                    GUEST_ID = response.body().getGuest_session_id();
                    SharedPreferences.Editor editor = SHAREED_PREFERENCES.edit();
                    editor.putString(GUEST_ID_PREFERENCE, GUEST_ID);
                    editor.commit();
                    RateDialog.this.rate();
                }
                @Override
                public void onFailure(Call<GuestObject> call, Throwable t) {
                    Toast.makeText(getContext(),"Rate failed. Check your connection",Toast.LENGTH_SHORT);
                }
            });

        }
        else {
            rate();
        }
    }

    private void rate(){
        final RateMoviesService rateMoviesService = RetrofitFactory.getInstance().createService(RateMoviesService.class);
        rateMoviesService.rateMovie(movieID, new RateMovieRequest(rating),API_KEY,GUEST_ID).enqueue(new Callback<RateMoviesResponse>() {
            @Override
            public void onResponse(Call<RateMoviesResponse> call, Response<RateMoviesResponse> response) {
                Toast.makeText(getContext(), response.body().getStatus_message(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new Float(rating));
                RATED_MOVIE_LIST.add(new MovieModel(movieID,rating));
            }

            @Override
            public void onFailure(Call<RateMoviesResponse> call, Throwable t) {
                Log.d("failed","failed");
                Toast.makeText(getContext(),"Rate Failed. Check your connection",Toast.LENGTH_SHORT).show();
            }
        });
    }

}

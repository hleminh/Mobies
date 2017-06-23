package com.example.hoang.mobies.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.hoang.mobies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivitiy extends AppCompatActivity {
    @BindView(R.id.iv_logo)
    TextView tvLogo;
    @BindView(R.id.tv_splash)
    TextView tvSplash;
    @BindView(R.id.pb_splash)
    ProgressBar pbSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(tvLogo);
        YoYo.with(Techniques.FadeIn).duration(1000).playOn(tvSplash);
        CountDownTimer countDownTimer = new CountDownTimer(4000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                pbSplash.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(SplashActivitiy.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(intent);
                overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
            }
        };
        countDownTimer.start();
    }
}

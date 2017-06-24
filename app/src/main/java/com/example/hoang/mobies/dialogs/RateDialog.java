package com.example.hoang.mobies.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hoang.mobies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    public RateDialog(@NonNull Context context) {
        super(context);
    }

    public RateDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected RateDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
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
                tvDialog.setText(rating + "");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                //TODO:
                break;
            case R.id.btn_cancel:
                dismiss();
        }
    }
}

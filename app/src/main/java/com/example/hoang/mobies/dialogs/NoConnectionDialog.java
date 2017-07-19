package com.example.hoang.mobies.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.hoang.mobies.R;
import com.example.hoang.mobies.activities.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hoang on 7/11/2017.
 */

public class NoConnectionDialog extends Dialog implements View.OnClickListener {
    private final Context context;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    public NoConnectionDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_no_connection);
        ButterKnife.bind(this);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm){
            MainActivity.flagNetworkSettings = true;
            dismiss();
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
        if (v.getId() == R.id.btn_cancel){
            dismiss();
        }
    }
}

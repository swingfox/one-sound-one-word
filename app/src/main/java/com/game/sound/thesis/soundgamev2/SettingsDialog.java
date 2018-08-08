package com.game.sound.thesis.soundgamev2;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.game.sound.thesis.soundgamev2.utils.Session;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wells on 05/03/2017.
 */

public class SettingsDialog  extends Dialog implements View.OnClickListener{
    public Activity c;
    private Session session;
    @BindView(R.id.btnOK) Button ok;
    @BindView(R.id.btnCancel) Button cancel;
    @BindView(R.id.seekAudio) SeekBar seekVolume;
    @BindView(R.id.txtVolume) TextView txtVolume;
    @BindView(R.id.chkAutoPlay) CheckBox chkAutoPlay;

    public SettingsDialog(Activity context) {
        super(context);
        this.c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        session = Session.getInstance(SettingsDialog.this.getContext());

        txtVolume.setText("Volume: " + session.getVolume());
        seekVolume.setProgress(session.getVolume());
        chkAutoPlay.setChecked(session.getAutoplay() == 1);

        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                session.setVolume(seekBar.getProgress());
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtVolume.setText("Volume: " + progress);
            }
        });
    }

    @OnClick({R.id.chkAutoPlay, R.id.btnOK, R.id.btnCancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOK:
            case R.id.btnCancel:
                dismiss();
                break;
            case R.id.chkAutoPlay:
                if(chkAutoPlay.isChecked()){
                    session.setAutoplay(1);
                    Toast.makeText(getContext(),"Autoplay",Toast.LENGTH_LONG).show();
                }
                else{
                    session.setAutoplay(0);
                }
                break;
            default:
                break;
        }
    }
}

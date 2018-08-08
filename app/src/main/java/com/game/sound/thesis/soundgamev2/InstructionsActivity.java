package com.game.sound.thesis.soundgamev2;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wells on 11/02/2017.
 */

public class InstructionsActivity extends AppCompatActivity {
    @BindView(R.id.txtInstructions) TextView txtInstruction;
    @BindView(R.id.txtProcedures) TextView txtProcedures;
    @BindView(R.id.instructionsLayout) RelativeLayout instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();

        ButterKnife.bind(this);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/LDFComicSans.ttf");

        txtInstruction.setTypeface(custom_font);
        txtProcedures.setTypeface(custom_font);
        txtProcedures.setText(getString(R.string.instructions));
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

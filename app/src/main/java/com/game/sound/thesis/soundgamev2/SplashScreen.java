package com.game.sound.thesis.soundgamev2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wells on 10/02/2017.
 */

public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 2500;
    private int progressStatus = 0;
    @BindView(R.id.progressLoad) ProgressBar progressLoad;
    private Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

	            /*
	             * Showing splash screen with a timer. This will be useful when you
	             * want to show case your app logo / company
	             */

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainMenuActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);

        ButterKnife.bind(this);

        progressLoad.getIndeterminateDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        // Start long running operation in a background thread
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressLoad.setProgress(progressStatus);
                          //  textView.setText(progressStatus+"/"+progressBar.getMax());
                        }
                    });
                    try {
                        // Sleep for 20 milliseconds.
                        //Just to display the progress slowly
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}

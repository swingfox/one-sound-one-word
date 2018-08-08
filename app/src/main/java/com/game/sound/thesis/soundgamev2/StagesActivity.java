package com.game.sound.thesis.soundgamev2;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.game.sound.thesis.soundgamev2.sql.DatabaseHelper;
import com.game.sound.thesis.soundgamev2.utils.Config;
import com.game.sound.thesis.soundgamev2.utils.Session;
import com.game.sound.thesis.soundgamev2.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wells on 11/03/2017.
 */

public class StagesActivity extends AppCompatActivity {
    @BindViews({R.id.imgStage1, R.id.imgStage2, R.id.imgStage3})
    List<ImageView> stages;

    @BindViews({R.id.imgStar1,R.id.imgStar2,R.id.imgStar3,
                R.id.imgStar4,R.id.imgStar5,R.id.imgStar6,
                R.id.imgStar7,R.id.imgStar8,R.id.imgStar9,
                R.id.imgStar10,R.id.imgStar11,R.id.imgStar12,
                R.id.imgStar13,R.id.imgStar14,R.id.imgStar15,
                R.id.imgStar16,R.id.imgStar17,R.id.imgStar18})
    List<ImageView> stars;

    @BindViews({R.id.imgScore1, R.id.imgScore2, R.id.imgScore3, R.id.imgScore4, R.id.imgScore5, R.id.imgScore6})
    List<ImageView> score;

    private Session saved;

    private Integer[] challenges = new Integer[] {
            R.drawable.scorezero, R.drawable.scoreone, R.drawable.scoretwo,
            R.drawable.scorethree,R.drawable.scorefour, R.drawable.scorefive,
            R.drawable.scoresix, R.drawable.scoreseven, R.drawable.scoreeight,
            R.drawable.scorenine, R.drawable.scoreten, R.drawable.scoreeleven,
            R.drawable.scoretwelve, R.drawable.scorethirteen, R.drawable.scorefourteen,
            R.drawable.scorefifteen
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stages_selection);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        ButterKnife.bind(this);
        saved = Session.getInstance(StagesActivity.this);

    }

    @Override
    public void onResume(){
        super.onResume();
        initYellowStars();

        /* SCORE PART */
        for(int i = 0 ; i < score.size(); i++){
            int c = saved.getChallengesFinished(i+1);
            score.get(i).setImageResource(challenges[c > 15 ? 15 : c]);
        }
    }

    public void initYellowStars(){
        ArrayList<Integer> numOfStars = new ArrayList<>();
        int j,k,l;
        j = 0;
        k = 1;
        l = 2;

        for(int i = 1; i <= 6; i++) {
            int stageStars = saved.getChallengesFinished(i);
            ImageView star1 = stars.get(j);
            ImageView star2 = stars.get(k);
            ImageView star3 = stars.get(l);
            if(stageStars >= 5){
                star1.setImageResource(R.drawable.star);
            }
            if(stageStars >= 10){
                star2.setImageResource(R.drawable.star);
            }
            if(stageStars >= 15){
                star3.setImageResource(R.drawable.star);
            }
            numOfStars.add(stageStars);
            j += 3;
            k += 3;
            l += 3;
        }
        boolean isCompleted = true;
        for(int i = 0; i < numOfStars.size(); i++){
            if(numOfStars.get(i) < 15){
                isCompleted = false;
                break;
            }
        }
        if(isCompleted && saved.getCongratulationStatus() == 1){
            MediaPlayer mp = MediaPlayer.create(StagesActivity.this,R.raw.congratulations);
            mp.start();
            Utils.playCongratulations(StagesActivity.this);
            createDialog("Congratulations!","You finished all the stages!","Reset","Back");
            saved.setCongratulationStatus(0);
        }
    }

    private AlertDialog.Builder builder;

    private void createDialog(final String title,final String message,final String ok, final String cancel){
        builder = new AlertDialog.Builder(StagesActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final AlertDialog.Builder clear = new AlertDialog.Builder(StagesActivity.this)
                                .setTitle("Clear")
                                .setMessage("Are you sure you want to clear the game?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Session session = Session.getInstance(getApplicationContext());
                                        session.clear();
                                        clearDB();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        clear.show();
                    }
                }).setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
    private void startStage(int stage) {
        if(saved.getChallengesFinished(stage) == 0) {
            saved.setChallengesFinished(stage,0);
        }
        Intent i = new Intent(StagesActivity.this,ChallengesActivity.class);
        i.putExtra("stage",stage);
        startActivity(i);
    }

    public void clearDB(){
        DatabaseHelper db = new DatabaseHelper(StagesActivity.this);
        db.clearVocabulary();
        Toast.makeText(StagesActivity.this,"SUCCESSFULLY CLEARED!",Toast.LENGTH_LONG).show();
    }

    public String TAG = "STAGES ACTIVITY: ";

    @OnClick(R.id.imgStage1)
    public void stage1(){
        startStage(1);
    }
    @OnClick(R.id.imgStage2)
    public void stage2(){
        startStage(2);
    }
    @OnClick(R.id.imgStage3)
    public void stage3(){
        startStage(3);
    }
    @OnClick(R.id.imgStage4)
    public void stage4(){
        startStage(4);
    }
    @OnClick(R.id.imgStage5)
    public void stage5(){
        startStage(5);
    }
    @OnClick(R.id.imgStage6)
    public void stage6(){
        startStage(6);
    }
}

package com.game.sound.thesis.soundgamev2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.game.sound.thesis.soundgamev2.utils.Session;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;


/**
 * Created by asus on 12/03/2017.
 */

public class ChallengesActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.imgStage) ImageView imgStage;
    @BindViews({
                R.id.c1,R.id.c2,R.id.c3,
                R.id.c4,R.id.c5,R.id.c6,
                R.id.c7,R.id.c8,R.id.c9,
                R.id.c10,R.id.c11,R.id.c12,
                R.id.c13,R.id.c14,R.id.c15
                })
    ImageView[] challenges = new ImageView[15];
    private int stage;
    private Session session = Session.getInstance(ChallengesActivity.this);
 //   @BindDrawable({R.drawable.challengeone,R.drawable.challengetwo})
    List<Integer> drawables = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stages_challenges);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        ButterKnife.bind(this);


        Bundle b = getIntent().getExtras();
        if(b != null){
            stage = b.getInt("stage");
            if(stage == 1){
                imgStage.setImageResource(R.drawable.stage1);
            }
            else if(stage == 2){
                imgStage.setImageResource(R.drawable.stage2);
            }
            else if(stage == 3){
                imgStage.setImageResource(R.drawable.stage3);
            }
            else if(stage == 4){
                imgStage.setImageResource(R.drawable.stage4);
            }
            else if(stage == 5){
                imgStage.setImageResource(R.drawable.stage5);
            }
            else if(stage == 6){
                imgStage.setImageResource(R.drawable.stage6);
            }
        }


    }

    private void updateChallengesView(){
        int stageAlpha = session.getChallengesFinished(stage);

        for(int i = 0; i < challenges.length; i++){
            challenges[i].setTag(i+1); // challenge 1,2,3,4,5,etc...
                if (i > stageAlpha) {
                    challenges[i].setImageResource(R.drawable.lock);
                } else {
                    int id;
                    switch (i+1){
                        case 2:
                            id = R.drawable.challengetwo;
                            break;
                        case 3:
                            id = R.drawable.challengethree;
                            break;
                        case 4:
                            id = R.drawable.challengefour;
                            break;
                        case 5:
                            id = R.drawable.challengefive;
                            break;
                        case 6:
                            id = R.drawable.challengesix;
                            break;
                        case 7:
                            id = R.drawable.challengeseven;
                            break;
                        case 8:
                            id = R.drawable.challengeeight;
                            break;
                        case 9:
                            id = R.drawable.challengenine;
                            break;
                        case 10:
                            id = R.drawable.challengeten;
                            break;
                        case 11:
                            id = R.drawable.challengeeleven;
                            break;
                        case 12:
                            id = R.drawable.challengetwelve;
                            break;
                        case 13:
                            id = R.drawable.challengethirteen;
                            break;
                        case 14:
                            id = R.drawable.challengefourteen;
                            break;
                        case 15:
                            id = R.drawable.challengefifteen;
                            break;
                        default:
                            id = R.drawable.challengeone;
                            break;
                    }
                    challenges[i].setImageResource(id);
                    challenges[i].setAlpha(1.0f);
                    challenges[i].setOnClickListener(ChallengesActivity.this);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  Toast.makeText(getApplicationContext(),"ON ACTIVITY REQUEST: " + requestCode + " RESULT: " + resultCode,Toast.LENGTH_SHORT).show();

        if (requestCode == 1) {
            if(resultCode == 0){
               // refreshView();
               // Toast.makeText(getApplicationContext(),"OK!",Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
    @Override
    public void onResume(){
        super.onResume();
        updateChallengesView();
    }

    @Override
    public void onClick(View v) {

        for(int i = 0; i < challenges.length; i++){
            if(challenges[i].getId() == v.getId()) {
                Intent j = null;

                if(stage == 1) {
                    j = new Intent(ChallengesActivity.this, GameActivity.class);
                }
                else if(stage == 2){
                    j = new Intent(ChallengesActivity.this, GameTwoActivity.class);
                }
                else if(stage == 3){
                    j = new Intent(ChallengesActivity.this, GameThreeActivity.class);
                }
                else if(stage == 4){
                    j = new Intent(ChallengesActivity.this, GameFourActivity.class);
                }
                else if(stage == 5){
                    j = new Intent(ChallengesActivity.this, GameFiveActivity.class);
                }
                else if(stage == 6){
                    j = new Intent(ChallengesActivity.this, GameSixActivity.class);
                }
                j.putExtra("challenge", challenges[i].getTag() + "");
                j.putExtra("stage",stage);
                j.putExtra("finished",session.getChallengesFinished(stage));
                startActivityForResult(j,1);
                break;
            }
        }
    }
}

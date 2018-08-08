package com.game.sound.thesis.soundgamev2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.game.sound.thesis.soundgamev2.model.Letter;
import com.game.sound.thesis.soundgamev2.model.Vocabulary;
import com.game.sound.thesis.soundgamev2.sql.DatabaseHelper;
import com.game.sound.thesis.soundgamev2.utils.Config;
import com.game.sound.thesis.soundgamev2.utils.Session;
import com.game.sound.thesis.soundgamev2.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by warren on 11/02/2017.
 */

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.txtChallenge1) TextView txtChallenge1;
    @BindView(R.id.txtCoins) TextView txtCoins;
    @BindView(R.id.txtStatus) TextView txtStatus;
    @BindView(R.id.imgSpeakerb4) ImageView imgSpeaker;
    private int challenge;
    private int stage;
    private String word;
    private ArrayList<Letter> letters = new ArrayList<>();
    @BindViews({R.id.s1,R.id.s2,R.id.s3,R.id.s4,R.id.s5,R.id.s6,R.id.s7,R.id.s8,R.id.s9,R.id.s10}) List<ImageView> imgLetters;
    @BindView(R.id.imgBulb) ImageView imgBulb;
    @BindView(R.id.imgTrash) ImageView imgTrashCan;
    @BindView(R.id.txtTrashRemaining1) TextView txtTrash;
    @BindView(R.id.txtBulbRem1) TextView txtBulb;

    private List<Vocabulary> random  = new ArrayList<>();
    private Session session;
    private int finished;
    public String TAG = "VOCABULARY ACTIVITY: ";
    private DatabaseHelper db = new DatabaseHelper(GameActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ButterKnife.bind(this);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        session = Session.getInstance(GameActivity.this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LDFComicSans.ttf");
        txtChallenge1.setTypeface(custom_font);
        txtStatus.setTypeface(custom_font);

        txtCoins.setText(session.getCoins()+"");

        for(int i = 0; i < imgLetters.size() ; i++){
            imgLetters.get(i).setOnClickListener(GameActivity.this);
        }

        a1 = (ImageView) findViewById(R.id.a1);
        a2 = (ImageView) findViewById(R.id.a2);
        a3 = (ImageView) findViewById(R.id.a3);
        a1.setOnClickListener(GameActivity.this);
        a2.setOnClickListener(GameActivity.this);
        a3.setOnClickListener(GameActivity.this);


        imgBulb.setOnClickListener(GameActivity.this);
        imgTrashCan.setOnClickListener(GameActivity.this);
        imgSpeaker.setOnClickListener(this);

        Bundle b = getIntent().getExtras();
        if(b != null){
            String c = b.getString("challenge");
            stage = b.getInt("stage");
            challenge = Integer.parseInt(c);
            txtChallenge1.setText(challenge +"");
            challenge--;
            finished = b.getInt("finished");
            random = db.getWords(Config.STAGE1_WORDS);
            initLetters(challenge);

        }
        imgSpeaker.setOnClickListener(this);
    }

    private Vocabulary w;

    public void autoplay(){
        if(session.getAutoplay() == 1) {
            Config.IS_PLAYING = true;
            Utils.playMusic(GameActivity.this, Config.STAGE1, w.getWord());
        }
    }

    private void makeVisible(){
        for (int i = 0; i < imgLetters.size(); i++) {
            imgLetters.get(i).setImageResource(letters.get(i).getImageId());
            imgLetters.get(i).setTag(letters.get(i));
            imgLetters.get(i).setVisibility(View.VISIBLE);
        }
    }

    private void formLetters(){
        letters.clear();
        int index;
        String alpha = new String(Config.alpha);
        for (index = 0; index < word.length(); index++) {
            char _c = word.charAt(index);
            letters.add(new Letter(_c, index + 1, Config.ALPHA_ID[getLetterIndex(_c)]));
        }
        for (int i = index; i < 10; i++) {
            for (int j = 0; j < Config.LETTERS_COUNT; j++) {
                alpha = Utils.shuffleString(alpha);
                if (!containsLetter(alpha.charAt(j))) {
                    char a = alpha.charAt(j);
                    letters.add(new Letter(a, 0, Config.ALPHA_ID[getLetterIndex(a)]));
                    break;
                }
            }
        }

        long seed = System.nanoTime();
        Collections.shuffle(letters, new Random(seed));
        makeVisible();
    }

    private void initLetters(int c){
        if(random.size() > 0 && c >= 0 && c < 15) {
            Log.d(TAG, "CURRENT CHALLENGE: " + session.getChallengesFinished(stage));

            if(c >= session.getChallengesFinished(stage)) {
                do {
                    w = random.get(Utils.getRandom(random.size()));
                    word = w.getWord();
                } while (w.getIsUsed() == 1);
                Toast.makeText(getApplicationContext(),"CURRENT WORD: " + w.getWord(), Toast.LENGTH_LONG).show();
            }else {
                for (int i = 0; i < random.size(); i++) {
                    Vocabulary rand = random.get(i);
                    if (rand.getIsUsed() == 1 && rand.getStage() > 0 && rand.getStage() == (c + 1)) {
                        w = rand;
                        Log.d(TAG, "IS USED: " + rand.getIsUsed());
                        word = w.getWord();
                        break;
                    }
                }
            }
            autoplay();
            formLetters();
        }
        else{
            Toast.makeText(getApplicationContext(),"RANDOM IS EMPTY!",Toast.LENGTH_LONG).show();
        }
    }

    private int getLetterIndex(char c){
        return Character.toUpperCase(c) - 65;//index == 1
    }


    private boolean containsLetter(char c){
        boolean flag = false;

        for(int i = 0; i < letters.size() ; i++){
            if((letters.get(i).getLetter()+"").equals(c+"")){
                flag = true;
                break;
            }
        }
        return flag;
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
     @Override
     public void onBackPressed(){
         super.onBackPressed();
         Intent returnIntent = new Intent();
       //  returnIntent.putExtra("result",result);
         setResult(Activity.RESULT_OK,returnIntent);
         finish();
     }

    private Letter selectedLetter;

    private void initAnswers(){
        a1.setImageResource(R.drawable.square);
        a2.setImageResource(R.drawable.square);
        a3.setImageResource(R.drawable.square);
        a1.setTag(null);
        a2.setTag(null);
        a3.setTag(null);
        txtStatus.setText("");

    }

    private void initRemaining(){
        trashRemaining = 3;
        bulbsRemaining = 1;
        txtTrash.setText(trashRemaining+"");
        txtBulb.setText(bulbsRemaining+"");
    }

    ImageView a1;
    ImageView a2;
    ImageView a3;
    CountDownTimer timer;

    private void check(){
        txtStatus.setText("CORRECT!");
        if (timer != null) {
            timer.cancel();
        }
        Toast.makeText(getApplicationContext(),"STAGE: " + stage, Toast.LENGTH_LONG).show();
        timer = new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                db.getWord(w.getWord());
                db.updateWordIsUsed(w,challenge+1);
                if (challenge >= 0 && challenge <= 14) {
                    if (challenge == finished) {
                        int score = session.getCoins() + 7;
                        session.setCoins(score);
                        txtCoins.setText(score + "");
                        finished++;
                    }
                    if (challenge < 14) {
                        if (challenge >= session.getChallengesFinished(stage)) {
                            session.setChallengesFinished(stage, ++challenge);
                        } else {
                            challenge++;
                        }
                        txtChallenge1.setText((challenge + 1) + "");
                        w.setIsUsed(1);
                        w.setStage(challenge-1);
                        initLetters(challenge);
                        initAnswers();
                        initRemaining();
                    } else {
                        session.setChallengesFinished(stage, 15);
                        session.setCongratulationStatus(1);
                        finish();
                    }
                } else {
                    session.setChallengesFinished(stage, 15);
                    finish();
                }
            }
        };
        timer.start();
    }

    @Override
    public void onClick(View v) {
        ImageView img = (ImageView) findViewById(v.getId());
        switch (v.getId()){
            case R.id.imgSpeakerb4:
                if(Config.IS_PLAYING == false) {
                    Config.IS_PLAYING = true;
                    Utils.playMusic(GameActivity.this, Config.STAGE1, w.getWord());
                }
                break;
            case R.id.s1:
            case R.id.s2:
            case R.id.s3:
            case R.id.s4:
            case R.id.s5:
            case R.id.s6:
            case R.id.s7:
            case R.id.s8:
            case R.id.s9:
            case R.id.s10:
                selectedLetter = (Letter) img.getTag();
                    if (isRedundant(selectedLetter) == false) {
                        Letter l1 = (Letter) a1.getTag();
                        Letter l2 = (Letter) a2.getTag();
                        Letter l3 = (Letter) a3.getTag();
                        if (l1 == null) {
                            a1.setImageResource(selectedLetter.getImageId());
                            a1.setTag(selectedLetter);
                            l1 = (Letter) a1.getTag();
                        } else if (l2 == null) {
                            a2.setImageResource(selectedLetter.getImageId());
                            a2.setTag(selectedLetter);
                            l2 = (Letter) a2.getTag();
                        } else if (l3 == null) {
                            a3.setImageResource(selectedLetter.getImageId());
                            a3.setTag(selectedLetter);
                            l3 = (Letter) a3.getTag();
                        }
                        img.setImageResource(selectedLetter.getImageId());
                        img.setTag(selectedLetter);

                        if (l1 != null && l2 != null && l3 != null) {
                            if (l1.getLetter() == word.charAt(0) && l2.getLetter() == word.charAt(1) && l3.getLetter() == word.charAt(2)) {
                                check();
                            } else {
                                createDialog(getString(R.string.wrongTitle), getString(R.string.wrongMessage), "Retry", "Back",w);
                                txtStatus.setText("WRONG!");
                            }
                        }
                    }
                    img.setVisibility(View.INVISIBLE);
                break;
            case R.id.a1:
            case R.id.a2:
            case R.id.a3:
                if(img != null && img.getTag() != null) {
                    Letter letter = (Letter) img.getTag();
                    for (int i = 0; i < imgLetters.size(); i++) {
                        Letter l = (Letter) imgLetters.get(i).getTag();
                        if (l.getLetter() == letter.getLetter() && l.getFlag() == letter.getFlag()) {
                            imgLetters.get(i).setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                    img.setImageResource(R.drawable.square);
                    img.setTag(null);
                }
                break;
            case R.id.imgBulb:
                bulb();
                break;
            case R.id.imgTrash:
                trash();
                break;
        }
    }
    private AlertDialog.Builder builder;

    private void createDialog(final String title,final String message,final String ok, final String cancel, final Vocabulary w){
        final String f = "You finished the challenges!";
        builder = new AlertDialog.Builder(GameActivity.this)
                .setTitle(title)
                .setMessage(challenge >= 14 ? f : message)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(ok.contentEquals("Retry")){
                            txtStatus.setText("");
                            a1.setImageResource(R.drawable.square);
                            a2.setImageResource(R.drawable.square);
                            a3.setImageResource(R.drawable.square);
                            a1.setTag(null);
                            a2.setTag(null);
                            a3.setTag(null);

                            initLetters(challenge);
                            initAnswers();
                            initRemaining();

                            for(int i = 0; i < imgLetters.size(); i++){
                                imgLetters.get(i).setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });

        if(w.getStage() == 0) {
            builder.show();
        }
    }
    private int bulbsRemaining = 1;
    private int trashRemaining = 3;

    private void bulb(){
        if((session.getCoins() - 5) >= 0 && bulbsRemaining > 0) {
            bulbsRemaining--;
            txtBulb.setText(bulbsRemaining+"");
            int score = session.getCoins() - 5;
            txtCoins.setText(score+"");
            session.setCoins(score);

            Letter available = null;

            if (a1.getTag() == null) {
                Letter l1 = new Letter(word.charAt(0),1,Config.ALPHA_ID[getLetterIndex(word.charAt(0))]);
                a1.setImageResource(l1.getImageId());
                a1.setTag(l1);
                available = l1;
            } else if (a2.getTag() == null) {
                Letter l2 = new Letter(word.charAt(1),2,Config.ALPHA_ID[getLetterIndex(word.charAt(1))]);
                a2.setImageResource(l2.getImageId());
                a2.setTag(l2);
                available = l2;
            } else if (a3.getTag() == null) {
                Letter l3 = new Letter(word.charAt(2),3,Config.ALPHA_ID[getLetterIndex(word.charAt(2))]);
                a3.setImageResource(l3.getImageId());
                a3.setTag(l3);
                available = l3;
            }

            if(available != null){
                for(int i = 0; i < imgLetters.size(); i++){
                    if(((Letter)imgLetters.get(i).getTag()).getLetter() == available.getLetter()){
                        imgLetters.get(i).setVisibility(View.INVISIBLE);
                        break;
                    }
                }
            }

            if(     a1.getTag() != null &&
                    a2.getTag() != null &&
                    a3.getTag() != null)
            {
                if (    word.charAt(0) == ((Letter) a1.getTag()).getLetter() &&
                        word.charAt(1) == ((Letter) a2.getTag()).getLetter() &&
                        word.charAt(2) == ((Letter) a3.getTag()).getLetter()) {
                    check();
                }
            }
        }
    }

    public boolean isRedundant(Letter image){
        boolean flag = false;
        Letter l1 = ((Letter)a1.getTag());
        Letter l2 = ((Letter)a2.getTag());
        Letter l3 = ((Letter)a3.getTag());

        if(l1 != null && l1.hashCode() == image.hashCode()){
            flag = true;
        }
        else if(l2 != null && l2.hashCode() == image.hashCode()){
            flag = true;
        }
        else if(l3 != null && l3.hashCode() == image.hashCode()){
            flag = true;
        }

        return flag;
    }
    public void trash(){
        if((session.getCoins() - 1) >= 0 && trashRemaining > 0) {
            trashRemaining--;
            txtTrash.setText(trashRemaining+"");
            int score = session.getCoins() - 2;
            txtCoins.setText(score+"");
            session.setCoins(score);
            Random r = new Random();
            int a = r.nextInt(10);

            while (true) {
                a = r.nextInt(10);
                if((((Letter) imgLetters.get(a).getTag()).getFlag() == 0) && imgLetters.get(a).getVisibility() == View.VISIBLE){
                    imgLetters.get(a).setVisibility(View.INVISIBLE);
                    break;
                }
            }
        }
    }
}

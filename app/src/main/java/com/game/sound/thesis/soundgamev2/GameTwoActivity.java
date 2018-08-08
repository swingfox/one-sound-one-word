package com.game.sound.thesis.soundgamev2;

import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.game.sound.thesis.soundgamev2.model.*;
import com.game.sound.thesis.soundgamev2.sql.DatabaseHelper;
import com.game.sound.thesis.soundgamev2.utils.Config;
import com.game.sound.thesis.soundgamev2.utils.Session;
import com.game.sound.thesis.soundgamev2.utils.Utils;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by warren on 16/03/2017.
 */

public class GameTwoActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.txtChallengeb2) TextView txtChallenge1;
    @BindView(R.id.txtCoinsb2) TextView txtCoins;
    @BindView(R.id.txtStatusb2) TextView txtStatus;
    @BindView(R.id.imgSpeakerb4) ImageView imgSpeaker;
    @BindViews({R.id.s1b2,R.id.s2b2,R.id.s3b2,R.id.s4b2,R.id.s5b2,R.id.s6b2,R.id.s7b2,R.id.s8b2,R.id.s9b2,R.id.s10b2})
    List<ImageView> imgLetters = new ArrayList<>();
    @BindView(R.id.imgBulbb2) ImageView imgBulb;
    @BindView(R.id.imgTrashb2) ImageView imgTrashCan;
    @BindView(R.id.txtTrashRemaining2) TextView txtTrash;
    @BindView(R.id.txtBulbRem2) TextView txtBulb;
    @BindView(R.id.a1b2) ImageView a1;
    @BindView(R.id.a2b2) ImageView a2;
    @BindView(R.id.a3b2) ImageView a3;
    @BindView(R.id.a4b2) ImageView a4;

    private int currentStage;
    private int stage;
    private int finished;
    private String word;
    private ArrayList<Letter> letters = new ArrayList<>();
    private CountDownTimer timer;
    private DatabaseHelper db = new DatabaseHelper(GameTwoActivity.this);
    private Session session;
    private Vocabulary w;
    private List<Vocabulary> random  = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gametwo);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        ButterKnife.bind(this);

        session = Session.getInstance(GameTwoActivity.this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LDFComicSans.ttf");
        txtChallenge1.setTypeface(custom_font);
        txtStatus.setTypeface(custom_font);
        txtCoins.setText(session.getCoins()+"");


        for(int i = 0; i < imgLetters.size() ; i++){
            imgLetters.get(i).setOnClickListener(GameTwoActivity.this);
        }

        a1.setOnClickListener(GameTwoActivity.this);
        a2.setOnClickListener(GameTwoActivity.this);
        a3.setOnClickListener(GameTwoActivity.this);
        a4.setOnClickListener(GameTwoActivity.this);

        imgBulb.setOnClickListener(GameTwoActivity.this);
        imgTrashCan.setOnClickListener(GameTwoActivity.this);

        Bundle b = getIntent().getExtras();
        if(b != null){
            String challenge = b.getString("challenge");
            stage = b.getInt("stage");
            currentStage = Integer.parseInt(challenge);
            txtChallenge1.setText(currentStage +"");
            currentStage--;
            random = db.getWords(4);

            for(int i = 0; i < random.size(); i++){
                printRow(random.get(i));
            }
            finished = b.getInt("finished");
            initLetters(currentStage);
        }
        imgSpeaker.setOnClickListener(this);
    }

    public void autoplay(){
        if(session.getAutoplay() == 1) {
            Config.IS_PLAYING = true;
            Utils.playMusic(GameTwoActivity.this, Config.STAGE2, w.getWord());
        }
    }

    public void printRow(Vocabulary v){
        Log.d(TAG,v.toString());
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
            letters.clear();
            int index;
            String alpha = new String(Config.alpha);
            for (index = 0; index < w.getWord().length(); index++) {
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
            autoplay();
            long seed = System.nanoTime();
            Collections.shuffle(letters, new Random(seed));

            for (int i = 0; i < imgLetters.size(); i++) {
                imgLetters.get(i).setImageResource(letters.get(i).getImageId());
                imgLetters.get(i).setTag(letters.get(i));
                imgLetters.get(i).setVisibility(View.VISIBLE);
            }
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

    private Letter selectedLetter;

    private void initAnswers(){
        a1.setImageResource(R.drawable.square);
        a2.setImageResource(R.drawable.square);
        a3.setImageResource(R.drawable.square);
        a4.setImageResource(R.drawable.square);
        a1.setTag(null);
        a2.setTag(null);
        a3.setTag(null);
        a4.setTag(null);
        txtStatus.setText("");
        trashRemaining = 3;
        bulbsRemaining = 1;
    }

    private String TAG = "GAME ACTIVITY ";

    @Override
    public void onClick(View v) {
        ImageView img = (ImageView) findViewById(v.getId());
        switch (v.getId()){
            case R.id.imgSpeakerb4:
                if(Config.IS_PLAYING == false) {
                    Config.IS_PLAYING = true;
                    Utils.playMusic(GameTwoActivity.this, Config.STAGE2, w.getWord());
                }
                break;
            case R.id.s1b2:
            case R.id.s2b2:
            case R.id.s3b2:
            case R.id.s4b2:
            case R.id.s5b2:
            case R.id.s6b2:
            case R.id.s7b2:
            case R.id.s8b2:
            case R.id.s9b2:
            case R.id.s10b2:
                selectedLetter = (Letter) img.getTag();
                if(isRedundant(selectedLetter) == false) {
                    Letter l1 = (Letter) a1.getTag();
                    Letter l2 = (Letter) a2.getTag();
                    Letter l3 = (Letter) a3.getTag();
                    Letter l4 = (Letter) a4.getTag();

                    if(l1 == null){
                        a1.setImageResource(selectedLetter.getImageId());
                        a1.setTag(selectedLetter);
                        l1 = (Letter) a1.getTag();
                    }
                    else if(l2 == null){
                        a2.setImageResource(selectedLetter.getImageId());
                        a2.setTag(selectedLetter);
                        l2 = (Letter) a2.getTag();
                    }
                    else if(l3 == null){
                        a3.setImageResource(selectedLetter.getImageId());
                        a3.setTag(selectedLetter);
                        l3 = (Letter) a3.getTag();
                    }
                    else if(l4 == null){
                        a4.setImageResource(selectedLetter.getImageId());
                        a4.setTag(selectedLetter);
                        l4 = (Letter) a4.getTag();
                    }
                    img.setImageResource(selectedLetter.getImageId());
                    img.setTag(selectedLetter);

                    if (l1 != null && l2 != null && l3 != null && l4 != null) {
                        if (l1.getLetter() == word.charAt(0) && l2.getLetter() == word.charAt(1) && l3.getLetter() == word.charAt(2) && l4.getLetter() == word.charAt(3)) {
                            check();
                        } else {
                            createDialog(getString(R.string.wrongTitle), getString(R.string.wrongMessage), "Retry", "Back");
                            txtStatus.setText("WRONG!");
                        }
                    }
                }

                img.setVisibility(View.INVISIBLE);
                break;
            case R.id.a1b2:
            case R.id.a2b2:
            case R.id.a3b2:
            case R.id.a4b2:
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
            case R.id.imgBulbb2:
                bulb();
                break;
            case R.id.imgTrashb2:
                trash();
            break;
        }
    }

    private void check(){
        txtStatus.setText("CORRECT!");
        if(timer != null){
            timer.cancel();
        }

        timer = new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                db.getWord(w.getWord());
                db.updateWordIsUsed(w,currentStage+1);
                if (currentStage >= 0 && currentStage <= 14) {
                    if (currentStage == finished) {
                        int score = session.getCoins() + 7;
                        session.setCoins(score);
                        txtCoins.setText(score + "");
                        finished++;
                    }
                    if (currentStage < 14) {
                        if (currentStage >= session.getChallengesFinished(stage)) {
                            session.setChallengesFinished(stage, ++currentStage);
                        }
                        else {
                            currentStage++;
                        }
                        txtChallenge1.setText((currentStage + 1) + "");
                        w.setIsUsed(1);
                        w.setStage(currentStage-1);
                        initLetters(currentStage);
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
            }};
        timer.start();
    }

    private AlertDialog.Builder builder;

    private void createDialog(final String title,final String message,final String ok, final String cancel){
        final String f = "You finished the challenges!";
        builder = new AlertDialog.Builder(GameTwoActivity.this)
                .setTitle(title)
                .setMessage(session.getChallengesFinished(stage) >= 15 ? f : message)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(ok.contentEquals("Retry")){
                            txtStatus.setText("");
                            a1.setImageResource(R.drawable.square);
                            a2.setImageResource(R.drawable.square);
                            a3.setImageResource(R.drawable.square);
                            a4.setImageResource(R.drawable.square);
                            a1.setTag(null);
                            a2.setTag(null);
                            a3.setTag(null);
                            a4.setTag(null);

                            initLetters(currentStage);
                            initRemaining();
                            initAnswers();

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

    private void initRemaining(){
        trashRemaining = 3;
        bulbsRemaining = 1;
        txtTrash.setText(trashRemaining+"");
        txtBulb.setText(bulbsRemaining+"");
    }

    private void bulb(){
        if((session.getCoins()-1) >= 0 && bulbsRemaining > 0) {
            bulbsRemaining--;
            txtBulb.setText(bulbsRemaining+"");
            int score = session.getCoins() - 5;
            txtCoins.setText(score+"");
            session.setCoins(score);
            if (a1.getTag() == null) {
                Letter l1 = new Letter(word.charAt(0),1,Config.ALPHA_ID[getLetterIndex(word.charAt(0))]);
                a1.setImageResource(l1.getImageId());
                a1.setTag(l1);
            } else if (a2.getTag() == null) {
                Letter l2 = new Letter(word.charAt(1),2,Config.ALPHA_ID[getLetterIndex(word.charAt(1))]);
                a2.setImageResource(l2.getImageId());
                a2.setTag(l2);
            } else if (a3.getTag() == null) {
                Letter l3 = new Letter(word.charAt(2),3,Config.ALPHA_ID[getLetterIndex(word.charAt(2))]);
                a3.setImageResource(l3.getImageId());
                a3.setTag(l3);
            }
            else if (a4.getTag() == null) {
                Letter l4 = new Letter(word.charAt(3),4,Config.ALPHA_ID[getLetterIndex(word.charAt(3))]);
                a4.setImageResource(l4.getImageId());
                a4.setTag(l4);
            }
            if(     a1.getTag() != null &&
                    a2.getTag() != null &&
                    a3.getTag() != null &&
                    a4.getTag() != null)
            {
                if (    word.charAt(0) == ((Letter) a1.getTag()).getLetter() &&
                        word.charAt(1) == ((Letter) a2.getTag()).getLetter() &&
                        word.charAt(2) == ((Letter) a3.getTag()).getLetter() &&
                        word.charAt(3) == ((Letter) a4.getTag()).getLetter()) {
                    check();
                }
            }
        }
    }

    public boolean isRedundant(Letter letter){
        boolean flag = false;
        Letter l1 = ((Letter)a1.getTag());
        Letter l2 = ((Letter)a2.getTag());
        Letter l3 = ((Letter)a3.getTag());
        Letter l4 = ((Letter)a4.getTag());


        if(l1 != null && l1.hashCode() == letter.hashCode()){
            flag = true;
        }
        if(l2 != null && l2.hashCode() == letter.hashCode()){
            flag = true;
        }
        if(l3 != null && l3.hashCode() == letter.hashCode()){
            flag = true;
        }
        if(l4 != null && l4.hashCode() == letter.hashCode()){
            flag = true;
        }

        return flag;
    }

    public void trash(){
        if((session.getCoins()-1) >= 0 && trashRemaining > 0) {
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

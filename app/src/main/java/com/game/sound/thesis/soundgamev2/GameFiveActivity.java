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
import butterknife.OnClick;

/**
 * Created by warren on 09/09/2017.
 */

public class GameFiveActivity extends AppCompatActivity {
    @BindView(R.id.txtChallengeb5) TextView txtChallenge1;
    @BindView(R.id.txtCoinsb5) TextView txtCoins;
    @BindView(R.id.txtStatusb5) TextView txtStatus;
    @BindView(R.id.imgSpeakerb5) ImageView imgSpeaker;
    @BindView(R.id.imgBulbb5) ImageView imgBulb;
    @BindView(R.id.imgTrashb5) ImageView imgTrashCan;
    @BindViews({R.id.a1b5,R.id.a2b5,R.id.a3b5,R.id.a4b5,R.id.a5b5,R.id.a6b5,R.id.a7b5}) List<ImageView> answer;
    @BindView(R.id.txtBulbRem5) TextView txtBulb;
    @BindView(R.id.txtTrashRemaining5) TextView txtTrash;
    @BindViews({R.id.s1b5,R.id.s2b5,R.id.s3b5, R.id.s4b5,R.id.s5b5,R.id.s6b5, R.id.s7b5,R.id.s8b5,R.id.s9b5, R.id.s10b5})
    List<ImageView> imgLetters = new ArrayList<>();
    private Vocabulary w;
    private int currentStage;
    private int stage;
    private String word;
    private int finished;
    private String TAG = "GAME FIVE ACTIVITY: ";
    private CountDownTimer timer;

    private ArrayList<Letter> letters = new ArrayList<>();
    private Session session;
    private List<Vocabulary> random  = new ArrayList<>();
    private Letter selectedLetter;
    private DatabaseHelper db = new DatabaseHelper(GameFiveActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamefive);
        ButterKnife.bind(this);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        session = Session.getInstance(GameFiveActivity.this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/LDFComicSans.ttf");
        txtChallenge1.setTypeface(custom_font);
        txtStatus.setTypeface(custom_font);
        txtCoins.setText(session.getCoins()+"");



        Bundle b = getIntent().getExtras();
        if(b != null){
            String challenge = b.getString("challenge");
            stage = b.getInt("stage");
            currentStage = Integer.parseInt(challenge);
            finished = b.getInt("finished");
            txtChallenge1.setText(currentStage +"");
            currentStage--;
            random = db.getWords(Config.STAGE5_WORDS);
            initLetters(currentStage);
        }
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

    public void autoplay(){
        if(session.getAutoplay() == 1) {
            Config.IS_PLAYING = true;
            Utils.playMusic(GameFiveActivity.this, Config.STAGE5, w.getWord());
        }
    }

    private void check(){
        txtStatus.setText("CORRECT!");
        if(timer != null){
            timer.cancel();
        }
        timer = new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) { }
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
                        } else {
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
            }
        };
        timer.start();
    }

    private void initAnswers(){
        for(int i = 0; i < answer.size(); i++){
            answer.get(i).setImageResource(R.drawable.square);
            answer.get(i).setTag(null);
        }
        txtStatus.setText("");
        initRemaining();
    }
    @OnClick({R.id.s1b5,R.id.s2b5,R.id.s3b5,R.id.s4b5,R.id.s5b5,R.id.s6b5, R.id.s7b5,R.id.s8b5,R.id.s9b5,
              R.id.s10b5,R.id.imgTrashb5,R.id.imgBulbb5,R.id.imgSpeakerb5,R.id.a1b5,R.id.a2b5,R.id.a3b5,
              R.id.a4b5,R.id.a5b5,R.id.a6b5,R.id.a7b5})
    public void onClick(View v) {
        ImageView img = (ImageView) findViewById(v.getId());
        switch (v.getId()){
            case R.id.imgSpeakerb5:
                if(Config.IS_PLAYING == false) {
                    Config.IS_PLAYING = true;
                    Utils.playMusic(GameFiveActivity.this, Config.STAGE5, w.getWord());
                }
                break;
            case R.id.s1b5:
            case R.id.s2b5:
            case R.id.s3b5:
            case R.id.s4b5:
            case R.id.s5b5:
            case R.id.s6b5:
            case R.id.s7b5:
            case R.id.s8b5:
            case R.id.s9b5:
            case R.id.s10b5:
                selectedLetter = (Letter) img.getTag();
                if(isRedundant(selectedLetter) == false) {
                    Letter l1 = (Letter) answer.get(0).getTag();
                    Letter l2 = (Letter) answer.get(1).getTag();
                    Letter l3 = (Letter) answer.get(2).getTag();
                    Letter l4 = (Letter) answer.get(3).getTag();
                    Letter l5 = (Letter) answer.get(4).getTag();
                    Letter l6 = (Letter) answer.get(5).getTag();
                    Letter l7 = (Letter) answer.get(6).getTag();


                    if(l1 == null){
                        answer.get(0).setImageResource(selectedLetter.getImageId());
                        answer.get(0).setTag(selectedLetter);
                        l1 = (Letter) answer.get(0).getTag();
                    }
                    else if(l2 == null){
                        answer.get(1).setImageResource(selectedLetter.getImageId());
                        answer.get(1).setTag(selectedLetter);
                        l2 = (Letter) answer.get(1).getTag();
                    }
                    else if(l3 == null){
                        answer.get(2).setImageResource(selectedLetter.getImageId());
                        answer.get(2).setTag(selectedLetter);
                        l3 = (Letter) answer.get(2).getTag();
                    }
                    else if(l4 == null){
                        answer.get(3).setImageResource(selectedLetter.getImageId());
                        answer.get(3).setTag(selectedLetter);
                        l4 = (Letter) answer.get(3).getTag();
                    }
                    else if(l5 == null){
                        answer.get(4).setImageResource(selectedLetter.getImageId());
                        answer.get(4).setTag(selectedLetter);
                        l5 = (Letter) answer.get(4).getTag();
                    }
                    else if(l6 == null){
                        answer.get(5).setImageResource(selectedLetter.getImageId());
                        answer.get(5).setTag(selectedLetter);
                        l6 = (Letter) answer.get(5).getTag();
                    }
                    else if(l7 == null){
                        answer.get(6).setImageResource(selectedLetter.getImageId());
                        answer.get(6).setTag(selectedLetter);
                        l7 = (Letter) answer.get(6).getTag();
                    }
                    img.setImageResource(selectedLetter.getImageId());
                    img.setTag(selectedLetter);
                    if (l1 != null && l2 != null && l3 != null && l4 != null && l5 != null && l6 != null && l7 != null) {
                        if (l1.getLetter() == word.charAt(0) && l2.getLetter() == word.charAt(1) && l3.getLetter() == word.charAt(2) && l4.getLetter() == word.charAt(3) && l4.getLetter() == word.charAt(3) && l5.getLetter() == word.charAt(4) && l6.getLetter() == word.charAt(5) && l7.getLetter() == word.charAt(6)) {
                            check();
                        } else {
                            createDialog(getString(R.string.wrongTitle), getString(R.string.wrongMessage), "Retry", "Back");
                            txtStatus.setText("WRONG!");
                        }
                    }
                }
                img.setVisibility(View.INVISIBLE);
                break;
            case R.id.a1b5:
            case R.id.a2b5:
            case R.id.a3b5:
            case R.id.a4b5:
            case R.id.a5b5:
            case R.id.a6b5:
            case R.id.a7b5:
                if(img != null && img.getTag() != null) {
                    Letter letter = (Letter) img.getTag();
                    for (int i = 0; i < imgLetters.size(); i++) {
                        Letter l = (Letter) imgLetters.get(i).getTag();
                        if (l.getLetter() == letter.getLetter()&& l.getFlag() == letter.getFlag()) {
                            imgLetters.get(i).setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                    img.setImageResource(R.drawable.square);
                    img.setTag(null);
                }
                break;
            case R.id.imgBulbb5:
                bulb();
                break;
            case R.id.imgTrashb5:
                trash();
                break;
        }
    }
    private AlertDialog.Builder builder;


    private void initRemaining(){
        bulbsRemaining = 1;
        trashRemaining = 2;
        txtTrash.setText(trashRemaining+"");
        txtBulb.setText(bulbsRemaining+"");
    }

    private void createDialog(final String title,final String message,final String ok, final String cancel){
        final String f = "You finished the challenges!";
        builder = new AlertDialog.Builder(GameFiveActivity.this)
                .setTitle(title)
                .setMessage(session.getChallengesFinished(stage) >= 15 ? f : message)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(ok.contentEquals("Retry")){
                            txtStatus.setText("");
                            for(int i = 0; i < answer.size(); i++){
                                answer.get(i).setImageResource(R.drawable.square);
                                answer.get(i).setTag(null);
                            }

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
    private int trashRemaining = 2;
    private void bulb(){
        if((session.getCoins()-1) >= 0 && bulbsRemaining > 0) {
            bulbsRemaining--;
            txtBulb.setText(bulbsRemaining+"");
            int score = session.getCoins() - 5;
            txtCoins.setText(score+"");
            session.setCoins(score);
            if (answer.get(0).getTag() == null) {
                Letter l1 = new Letter(word.charAt(0),1,Config.ALPHA_ID[getLetterIndex(word.charAt(0))]);
                answer.get(0).setImageResource(l1.getImageId());
                answer.get(0).setTag(l1);
            } else if (answer.get(1).getTag() == null) {
                Letter l2 = new Letter(word.charAt(1),2,Config.ALPHA_ID[getLetterIndex(word.charAt(1))]);
                answer.get(1).setImageResource(l2.getImageId());
                answer.get(1).setTag(l2);
            } else if (answer.get(2).getTag() == null) {
                Letter l3 = new Letter(word.charAt(2),3,Config.ALPHA_ID[getLetterIndex(word.charAt(2))]);
                answer.get(2).setImageResource(l3.getImageId());
                answer.get(2).setTag(l3);
            }
            else if (answer.get(3).getTag() == null) {
                Letter l4 = new Letter(word.charAt(3),4,Config.ALPHA_ID[getLetterIndex(word.charAt(3))]);
                answer.get(3).setImageResource(l4.getImageId());
                answer.get(3).setTag(l4);
            }
            else if (answer.get(4).getTag() == null) {
                Letter l5 = new Letter(word.charAt(4),5,Config.ALPHA_ID[getLetterIndex(word.charAt(4))]);
                answer.get(4).setImageResource(l5.getImageId());
                answer.get(4).setTag(l5);
            }
            else if (answer.get(5).getTag() == null) {
                Letter l6 = new Letter(word.charAt(5),5,Config.ALPHA_ID[getLetterIndex(word.charAt(5))]);
                answer.get(5).setImageResource(l6.getImageId());
                answer.get(5).setTag(l6);
            }
            else if (answer.get(6).getTag() == null) {
                Letter l7 = new Letter(word.charAt(6),6,Config.ALPHA_ID[getLetterIndex(word.charAt(6))]);
                answer.get(6).setImageResource(l7.getImageId());
                answer.get(6).setTag(l7);
            }

            if(     answer.get(0).getTag() != null &&
                    answer.get(1).getTag() != null &&
                    answer.get(2).getTag() != null &&
                    answer.get(3).getTag() != null &&
                    answer.get(4).getTag() != null &&
                    answer.get(5).getTag() != null &&
                    answer.get(6).getTag() != null)
            {
                if (    word.charAt(0) == ((Letter) answer.get(0).getTag()).getLetter() &&
                        word.charAt(1) == ((Letter) answer.get(1).getTag()).getLetter() &&
                        word.charAt(2) == ((Letter) answer.get(2).getTag()).getLetter() &&
                        word.charAt(3) == ((Letter) answer.get(3).getTag()).getLetter() &&
                        word.charAt(4) == ((Letter) answer.get(4).getTag()).getLetter() &&
                        word.charAt(5) == ((Letter) answer.get(5).getTag()).getLetter() &&
                        word.charAt(6) == ((Letter) answer.get(6).getTag()).getLetter()
                        ) {
                    check();
                }
            }
        }
    }

    public boolean isRedundant(Letter letter){
        boolean flag = false;
        Letter l1 = ((Letter)answer.get(0).getTag());
        Letter l2 = ((Letter)answer.get(1).getTag());
        Letter l3 = ((Letter)answer.get(2).getTag());
        Letter l4 = ((Letter)answer.get(3).getTag());
        Letter l5 = ((Letter)answer.get(4).getTag());
        Letter l6 = ((Letter)answer.get(5).getTag());
        Letter l7 = ((Letter)answer.get(6).getTag());

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
        if(l5 != null && l5.hashCode() == letter.hashCode()){
            flag = true;
        }
        if(l6 != null && l6.hashCode() == letter.hashCode()){
            flag = true;
        }
        if(l7 != null && l7.hashCode() == letter.hashCode()){
            flag = true;
        }

        return flag;
    }
    public void trash(){
        if((session.getCoins()-1) >= 0 && trashRemaining > 0) {
            trashRemaining--;
            txtTrash.setText(trashRemaining+"");
            int score = session.getCoins() - 4;
            txtCoins.setText(score+"");
            session.setCoins(score);
            Random r = new Random();
            int a = 0;

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

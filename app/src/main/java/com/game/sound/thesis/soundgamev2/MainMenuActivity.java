package com.game.sound.thesis.soundgamev2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.game.sound.thesis.soundgamev2.model.Vocabulary;
import com.game.sound.thesis.soundgamev2.sql.DatabaseHelper;
import com.game.sound.thesis.soundgamev2.utils.Config;
import com.game.sound.thesis.soundgamev2.utils.Session;
import com.game.sound.thesis.soundgamev2.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuActivity extends AppCompatActivity {
    @BindView(R.id.btnPlay) Button btnPlay;
    @BindView(R.id.btnVocabulary) Button btnVocabulary;
    @BindView(R.id.btnInstructions) Button btnInstructions;
    @BindView(R.id.btnQuit) Button btnQuit;
    @BindView(R.id.imgSettings) ImageView imgSettings;
    @BindView(R.id.mainLayout) RelativeLayout main;

    public void getAvailableWords(final DatabaseHelper db, final String word, final int stage){
        StringRequest strReq = new StringRequest(Request.Method.POST, Config.URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                try {
                    if(response.equals(" []")){
                    }
                    else {
                        JSONArray jsonArray = new JSONArray(response);
                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String word = jsonObject.getString("word").trim();
                            int stage = Integer.parseInt(jsonObject.getString("stage"));
                            int isUsed = Integer.parseInt(jsonObject.getString("isUsed"));
                            String definition = jsonObject.getString("description").trim();

                            Vocabulary v = new Vocabulary();
                            v.setWord(word);
                            v.setLength(word.length());
                            v.setStage(stage);
                            v.setIsUsed(isUsed);
                            v.setDefinition(definition);
                            random.add(v);
                            if(db != null){
                                db.addVocabulary(v);
                                printRow(v.toString());
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                final Map<String, String> params = new HashMap<String, String>();
                params.put("query", "select_word");
                params.put("stage", stage+"");
                params.put("word", word);
                return params;
            }

        };

        strReq.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(strReq);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        ButterKnife.bind(this);
        initializeQuitButton();

        Session session = Session.getInstance(MainMenuActivity.this);
        if(session.getCoins()==0){
            session.setCoins(25);
        }

        DatabaseHelper db = new DatabaseHelper(MainMenuActivity.this);

        for(int i = 1; i <= 6 ;i++){
            List<String> assets = Utils.listAssets(MainMenuActivity.this,"music/stage"+i);
            for(int j = 0; j < assets.size(); j++) {
                String word = assets.get(j).replace(".mp3","");
                getAvailableWords(db,word,i);
            }
        }
        List<Vocabulary> vList = db.getAllWords();
        Toast.makeText(getApplicationContext(),"LIST OF DB WORDS: " + vList.size(), Toast.LENGTH_LONG).show();
        for(int i = 0; i < vList.size(); i++){
            printRow(vList.get(i).getWord());
        }
    }
    private List<Vocabulary> random = new ArrayList<>();

    public void printRow(String word){
        DatabaseHelper db = new DatabaseHelper(MainMenuActivity.this);
        Vocabulary v = db.getWord(word);
        Log.d("DB ROW",v.toString());
    }

    private void initializeQuitButton(){
        quit = new AlertDialog.Builder(MainMenuActivity.this)
                .setTitle("")
                .setMessage("Are you sure you want to exit the game?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        yesButton();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        noButton();
                    }
                });
    }
    @OnClick(R.id.btnPlay)
    public void play(){
        Intent i = new Intent(MainMenuActivity.this,StagesActivity.class);
        startActivity(i);
    }
    @OnClick(R.id.btnInstructions)
    public void instructions(){
        Intent j = new Intent(MainMenuActivity.this,InstructionsActivity.class);
        startActivity(j);
    }

    @OnClick(R.id.btnVocabulary)
    public void vocabulary(){
        Intent j = new Intent(MainMenuActivity.this,VocabularyActivity.class);
        startActivity(j);
    }
    @OnClick(R.id.btnQuit)
    public void quit(){
        onBackPressed();
    }

    @OnClick(R.id.imgSettings)
    public void settings(){
        SettingsDialog settingsDialog = new SettingsDialog(MainMenuActivity.this);
        Window window = settingsDialog.getWindow();
        window.setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        settingsDialog.show();

    }

    private void noButton(){

    }
    private void yesButton(){
        finish();
    }
    private  AlertDialog.Builder quit;

    private void executeQuitButton(){
        quit.show();
    }
    @Override
    public void onBackPressed() {
        executeQuitButton();
    }

    @OnClick(R.id.imgReset)
    public void reset(){
        final AlertDialog.Builder clear = new AlertDialog.Builder(MainMenuActivity.this)
                .setTitle(getString(R.string.resetTitle))
                .setMessage(getString(R.string.resetMessage))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Session session = Session.getInstance(getApplicationContext());
                        session.clear();
                        clearDB();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        noButton();
                    }
                });
        clear.show();
    }

    public String TAG = "MAIN MENU ACTIVITY: ";
    public void clearDB(){
        DatabaseHelper db = new DatabaseHelper(MainMenuActivity.this);
        db.clearVocabulary();
        Toast.makeText(MainMenuActivity.this,"SUCCESSFULLY CLEARED!",Toast.LENGTH_LONG).show();
    }

}

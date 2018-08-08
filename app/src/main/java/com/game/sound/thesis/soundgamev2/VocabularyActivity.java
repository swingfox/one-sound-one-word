package com.game.sound.thesis.soundgamev2;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.game.sound.thesis.soundgamev2.model.Vocabulary;
import com.game.sound.thesis.soundgamev2.sql.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wells on 09/09/2017.
 */

public class VocabularyActivity extends AppCompatActivity {
    @BindView(R.id.vocabularyLayout)LinearLayout vocabulary;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        ButterKnife.bind(this);

        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.show();
        actionBar.setTitle("Vocabulary");
        pd = new ProgressDialog(VocabularyActivity.this);
        pd.setMessage("Please wait...");
        pd.setCancelable(false);
        pd.show();
        DatabaseHelper db = new DatabaseHelper(VocabularyActivity.this);
        List<Vocabulary> v = db.getVocabulary();

        for(int i = 0; i < v.size(); i++){
            vocabulary.addView(new VocabularyView(VocabularyActivity.this,v.get(i)));
        }

        pd.dismiss();
    }
    public String TAG = "VOCABULARY ACTIVITY: ";


    /**
     * Created by David on 10/09/2017.
     */


    public class VocabularyView extends LinearLayout{

        LinearLayout layout;
        TextView word;
        TextView description;
        public VocabularyView(Context context, Vocabulary w){

            super(context);

            setOrientation(LinearLayout.VERTICAL);
            setVisibility(View.VISIBLE);

            Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/LDFComicSans.ttf");
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp.setMargins(20,0,0,0);
            LayoutParams lp2 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            lp2.setMargins(50,0,0,15);

            word = new TextView(context);
            word.setTextSize(30);
            word.setVisibility(View.VISIBLE);
            word.setText(w.getWord());
            word.setTextColor(Color.BLACK);
            word.setTypeface(custom_font);
            word.setLayoutParams(lp);

            description = new TextView(context);
            description.setTextSize(16);
            description.setVisibility(View.VISIBLE);
            description.setText(w.getDefinition());
            description.setTextColor(Color.BLACK);
            description.setTypeface(custom_font);
            description.setLayoutParams(lp2);

            addView(word);
            addView(description);
        }
    }
}

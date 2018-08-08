package com.game.sound.thesis.soundgamev2.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.game.sound.thesis.soundgamev2.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alladin on 23/09/2017.
 */

public class Config {
    public static int LETTERS_COUNT = 26;
    public static int[] ALPHA_ID = new int[]{
            R.drawable.a,R.drawable.b,R.drawable.c,
            R.drawable.d,R.drawable.e,R.drawable.f,
            R.drawable.g,R.drawable.h,R.drawable.i,
            R.drawable.j,R.drawable.k,R.drawable.l,
            R.drawable.m,R.drawable.n,R.drawable.o,
            R.drawable.p,R.drawable.q,R.drawable.r,
            R.drawable.s,R.drawable.t,R.drawable.u,
            R.drawable.v,R.drawable.w,R.drawable.x,
            R.drawable.y,R.drawable.z};
    public static String alpha = "abcdefghijklmnopqrstuvwxyz";

    public static String ip = "http://192.168.43.91:80/";
    public static String URL = ip+"onesoundoneword/index.php";

    public static int STAGE1_WORDS = 3;
    public static int STAGE2_WORDS = 4;
    public static int STAGE3_WORDS = 5;
    public static int STAGE4_WORDS = 6;
    public static int STAGE5_WORDS = 7;
    public static int STAGE6_WORDS = 8;

    public static int STAGE1 = 1;
    public static int STAGE2 = 2;
    public static int STAGE3 = 3;
    public static int STAGE4 = 4;
    public static int STAGE5 = 5;
    public static int STAGE6 = 6;

    public static boolean IS_PLAYING = false;
}

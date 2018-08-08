package com.game.sound.thesis.soundgamev2.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alladin on 17/03/2017.
 */

public class Session {
    private SharedPreferences sharedPreferences;
    private static Session session;

    private Session(Context activity){
        sharedPreferences = activity.getSharedPreferences(activity.getClass().toString(),Context.MODE_PRIVATE);
    }

    public static Session getInstance(Context activity){
        if(session == null){
            session = new Session(activity);
        }
        return session;
    }

    public void setChallengesFinished(int stage,int challenge){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("challenge"+stage, challenge);
        editor.commit();
    }

    public int getChallengesFinished(int stage){
        int defaultValue = 0;
        return sharedPreferences.getInt("challenge"+stage, defaultValue);
    }

    public void setScore(int score){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("score", score);
        editor.commit();
    }

    public void setCoins(int coins){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("coins", coins);
        editor.commit();
    }

    public void setVolume(int volume){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("volume", volume);
        editor.commit();
    }

    public void setAutoplay(int autoplay){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("autoplay", autoplay);
        editor.commit();
    }

    public void setCongratulationStatus(int congrats){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("congrats", congrats);
        editor.commit();
    }

    public int getAutoplay(){
        int defaultValue =  0;
        return sharedPreferences.getInt("autoplay", defaultValue);
    }

    public int getVolume(){
        int defaultValue =  100;
        return sharedPreferences.getInt("volume", defaultValue);
    }

    public int getCoins(){
        int defaultValue = 0;
        return sharedPreferences.getInt("coins", defaultValue);
    }

    public int getCongratulationStatus(){
        int defaultValue = 0;
        return sharedPreferences.getInt("congrats", defaultValue);
    }

    public void clear(){
        sharedPreferences.edit().clear().commit();
    }
}

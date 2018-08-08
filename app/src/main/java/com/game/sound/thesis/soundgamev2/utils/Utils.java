package com.game.sound.thesis.soundgamev2.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by alladin on 07/10/2017.
 */

public class Utils {
    public static int getRandom(int size){
        int s = size - 1;
        SecureRandom sr = new SecureRandom();
        return sr.nextInt(s);
    }

    public static String shuffleString(String string)
    {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for (String letter : letters) {
            shuffled += letter;
        }
        return shuffled;
    }

    public static void playMusic(Context c,int stage, String word){
        AssetFileDescriptor descriptor = null;
        try {
            descriptor = c.getAssets().openFd("music/stage"+stage+"/"+word+".mp3");
            final MediaPlayer player = new MediaPlayer();

            long start = descriptor.getStartOffset();
            long end = descriptor.getLength();

            player.setDataSource(descriptor.getFileDescriptor(), start, end);
            player.prepare();

            Session s = Session.getInstance(c);
            player.setVolume(s.getVolume(), s.getVolume());
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Config.IS_PLAYING = false;
                    player.release();
                }
            });
        }
        catch(IOException e){
            Toast.makeText(c,"Message: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static void playCongratulations(Context c){
        AssetFileDescriptor descriptor = null;
        try {
            descriptor = c.getAssets().openFd("music/congratulations.mp3");
            final MediaPlayer player = new MediaPlayer();

            long start = descriptor.getStartOffset();
            long end = descriptor.getLength();

            player.setDataSource(descriptor.getFileDescriptor(), start, end);
            player.prepare();

            Session s = Session.getInstance(c);
            player.setVolume(s.getVolume(), s.getVolume());
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    player.release();
                }
            });
        }
        catch(IOException e){
            Toast.makeText(c,"Message: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static List<String> listAssets(Context c,String dirFrom){
        Resources res = c.getResources(); //if you are in an activity
        AssetManager am = res.getAssets();
        List<String> assets = new ArrayList<>();
        String fileList[] = null;
        try {
            fileList = am.list(dirFrom);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fileList != null)
        {
            for ( int i = 0;i<fileList.length;i++)
            {
                Log.d("ASSET FILES ",fileList[i]);
                assets.add(fileList[i]);
            }
        }
        return assets;
    }
}

package com.game.sound.thesis.soundgamev2.model;

/**
 * Created by asus on 12/03/2017.
 */

public class Word {
    private String word;
    private int musicId;
    private int isUsed;
    private int stage;

    public Word(int musicId, String word, int stageAnswered, int isUsed){
        this.musicId = musicId;
        this.word = word;
        this.stage = stageAnswered;
        this.isUsed = isUsed;
    }
    public Word(int musicId, String word){
        this.musicId = musicId;
        this.word = word;
    }

    public Word(Word word){
        this.musicId = word.getMusic();
        this.word = word.getWord();
        this.isUsed = word.getIsUsed();
        this.stage = word.getStage();
    }
    public String getWord() { return word; }
    public int getMusic() { return musicId;}
    public int getStage() { return stage;}
    public int getIsUsed() { return isUsed; }
    public void setIsUsed(int isUsed){
        this.isUsed = isUsed;
    }
    public void setStage(int stage){
        this.stage = stage;
    }

}

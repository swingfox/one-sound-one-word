package com.game.sound.thesis.soundgamev2.model;

/**
 * Created by nyzha on 01/10/2017.
 */
public class Vocabulary {
    private int id;
    private String word;
    private String definition;
    private int isUsed;
    private int stage;
    private int length;


    public void setLength(int length) {
        this.length = length;
    }
    public int getLength() {
        return length;
    }
    public int getIsUsed() {
        return isUsed;
    }
    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }
    public int getStage() {
        return stage;
    }
    public void setStage(int stage) {
        this.stage = stage;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public String getDefinition() {
        return definition;
    }
    public void setDefinition(String definition) {
        this.definition = definition;
    }
    @Override
    public String toString(){
        return "Word: " + word + " DEFINITION: " + definition + " IS_USED: " + isUsed;
    }

}

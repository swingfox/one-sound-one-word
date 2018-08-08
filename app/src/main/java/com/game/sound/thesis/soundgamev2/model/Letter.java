package com.game.sound.thesis.soundgamev2.model;

/**
 * Created by nyzha on 12/03/2017.
 */

public class Letter {
    private char letter;
    private int flag;
    private int imageId;
    public Letter(char letter, int flag, int imageId){
        this.letter = letter;
        this.flag = flag;
        this.imageId = imageId;
    }

    public char getLetter(){
        return  letter;
    }

    public int getFlag(){
        return flag;
    }
    public int getImageId(){
        return imageId;
    }
}

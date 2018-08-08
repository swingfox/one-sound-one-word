package com.game.sound.thesis.soundgamev2.sql;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.game.sound.thesis.soundgamev2.model.Vocabulary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by warren on 15/10/2017.
 */

public class DatabaseHelper extends DatabaseHandler{
    public DatabaseHelper(Context context){
        super(context);
    }
    public void addVocabulary(Vocabulary v) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_WORD, v.getWord());
        values.put(KEY_DEFINITION, v.getDefinition());
        values.put(KEY_IS_USED, v.getIsUsed());
        values.put(KEY_STAGE, v.getStage());
        values.put(KEY_LENGTH, v.getLength());

        if(getWord(v.getWord()) == null){
            db.insert(TABLE_VOCABULARY, null, values);
            Log.d("TABLE INSERT","SUCCESSFULLY INSERTED");
        }
        else{
            Log.d("TABLE INSERT","DUPLICATE WORD DETECTED!");
        }

        db.close();
    }

    public Vocabulary getWord(String word) {
        Vocabulary v = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_VOCABULARY, new String[] { KEY_ID,KEY_WORD,KEY_DEFINITION,KEY_IS_USED,KEY_STAGE,KEY_LENGTH }, KEY_WORD + "=?",
                new String[] { String.valueOf(word) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if(cursor.getCount() > 0){
            v = new Vocabulary();
            v.setId(cursor.getInt(0));
            v.setWord(cursor.getString(1));
            v.setDefinition(cursor.getString(2));
            v.setIsUsed(cursor.getInt(3));
            v.setStage(cursor.getInt(4));
            v.setLength(cursor.getInt(5));
        }

        cursor.close();
        return v;
    }

    public List<Vocabulary> getWords(int length) {
        List<Vocabulary> vocabularyList = new ArrayList<Vocabulary>();
        String selectQuery = "SELECT  * FROM " + TABLE_VOCABULARY + " WHERE length = " + length;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Vocabulary v = new Vocabulary();
                v.setId(Integer.parseInt(cursor.getString(0)));
                v.setWord(cursor.getString(1));
                v.setDefinition(cursor.getString(2));
                v.setIsUsed(cursor.getInt(3));
                v.setStage(cursor.getInt(4));
                v.setLength(cursor.getInt(5));
                vocabularyList.add(v);
            } while (cursor.moveToNext());
        }
        db.close();

        return vocabularyList;
    }

    public List<Vocabulary> getAllWords() {
        List<Vocabulary> vocabularyList = new ArrayList<Vocabulary>();
        String selectQuery = "SELECT  * FROM " + TABLE_VOCABULARY + " ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Vocabulary v = new Vocabulary();
                v.setId(Integer.parseInt(cursor.getString(0)));
                v.setWord(cursor.getString(1));
                v.setDefinition(cursor.getString(2));
                vocabularyList.add(v);
            } while (cursor.moveToNext());
        }
        db.close();
        return vocabularyList;
    }

    public List<Vocabulary> getVocabulary() {
        List<Vocabulary> vocabularyList = new ArrayList<Vocabulary>();
        String selectQuery = "SELECT  * FROM " + TABLE_VOCABULARY + " WHERE isUsed = 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Vocabulary v = new Vocabulary();
                v.setId(Integer.parseInt(cursor.getString(0)));
                v.setWord(cursor.getString(1));
                v.setDefinition(cursor.getString(2));
                vocabularyList.add(v);
            } while (cursor.moveToNext());
        }
        db.close();
        return vocabularyList;
    }

    public int updateDefinition(Vocabulary v) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DEFINITION, v.getDefinition());
        Log.d("UPDATE DEF","SUCCESSFULLY UPDATED");
        return db.update(TABLE_VOCABULARY, values, KEY_WORD + " = ?", new String[] { String.valueOf(v.getWord()) });
    }

    public int updateWordIsUsed(Vocabulary v,int stage) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_USED, 1);
        values.put(KEY_STAGE, stage);

        Log.d("UPDATE DEF","SUCCESSFULLY USED");
        return db.update(TABLE_VOCABULARY, values, KEY_WORD + " = ?", new String[] { String.valueOf(v.getWord()) });
    }

    public int clearVocabulary() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IS_USED, 0);
        values.put(KEY_STAGE, 0);

        Log.d("UPDATE DEF","SUCCESSFULLY CLEARED");
        return db.update(TABLE_VOCABULARY, values, null,null);
    }
}

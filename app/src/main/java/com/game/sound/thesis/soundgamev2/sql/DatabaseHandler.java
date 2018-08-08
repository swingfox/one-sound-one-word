package com.game.sound.thesis.soundgamev2.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by warren on 14/10/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 2;

    // Database Name
    public static final String DATABASE_NAME = "onesoundoneword";

    // Contacts table name
    public static final String TABLE_VOCABULARY = "vocabulary";

    // Contacts Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_WORD = "word";
    public static final String KEY_DEFINITION = "definition";
    public static final String KEY_IS_USED = "isUsed";
    public static final String KEY_STAGE = "stage";
    public static final String KEY_LENGTH = "length";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_VOCABULARY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_WORD + " TEXT,"
                + KEY_DEFINITION + " TEXT" + " ," + KEY_IS_USED  +" INTEGER, "
                + KEY_STAGE +" INTEGER, " + KEY_LENGTH + " INTEGER )";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VOCABULARY);
        // Create tables again
        Log.d("DATABASE OPERATIONS","DROP TABLE");
        onCreate(db);
    }
}
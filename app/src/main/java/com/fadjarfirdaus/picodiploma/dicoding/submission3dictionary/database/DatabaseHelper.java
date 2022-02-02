package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database.DatabaseContract.DictionaryColumns.DESC;
import static com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database.DatabaseContract.DictionaryColumns.WORDS;
import static com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database.DatabaseContract.TABLE_ENGLISH_DICTIONARY;
import static com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database.DatabaseContract.TABLE_INDONESIA_DICTIONARY;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "db_dictionary";
    private static final int DATABASE_VERSION = 1;

    private static String CREATE_ENGLISH_TABLE = "create table " + TABLE_ENGLISH_DICTIONARY +
            " (" + _ID + " integer primary key autoincrement, " +
            WORDS + " text not null, " +
            DESC + " text not null);";

    private static String CREATE_INDONESIAN_TABLE = "create table " + TABLE_INDONESIA_DICTIONARY+
            " (" + _ID + " integer primary key autoincrement, " +
            WORDS + " text not null, " +
            DESC + " text not null);";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ENGLISH_TABLE);
        db.execSQL(CREATE_INDONESIAN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

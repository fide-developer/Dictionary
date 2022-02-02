package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.model.ModelDictionary;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database.DatabaseContract.DictionaryColumns.DESC;
import static com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database.DatabaseContract.DictionaryColumns.WORDS;
import static com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database.DatabaseContract.TABLE_ENGLISH_DICTIONARY;
import static com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database.DatabaseContract.TABLE_INDONESIA_DICTIONARY;

public class DictionaryHelper {
    private DatabaseHelper databaseHelper;
    private static DictionaryHelper INSTANCE;

    private SQLiteDatabase database;

    public DictionaryHelper(Context context){
        databaseHelper = new DatabaseHelper(context);
    }
    public static DictionaryHelper getInstance(Context context){
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null) {
                    INSTANCE = new DictionaryHelper(context);
                }
            }
        }
        return INSTANCE;
    }
    public void open() throws SQLException{
        database = databaseHelper.getWritableDatabase();
    }
    public void close(){
        databaseHelper.close();
        if(database.isOpen()){
            database.close();
        }
    }

    public ArrayList<ModelDictionary> getAllEnglishData() {
        Cursor cursor = database.query(TABLE_ENGLISH_DICTIONARY, null, null, null, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<ModelDictionary> arrayList = new ArrayList<>();
        ModelDictionary modelDictionary;
        if (cursor.getCount() > 0) {
            do {
                modelDictionary = new ModelDictionary();
                modelDictionary.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                modelDictionary.setWords(cursor.getString(cursor.getColumnIndexOrThrow(WORDS)));
                modelDictionary.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESC)));
                arrayList.add(modelDictionary);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }
    public ArrayList<ModelDictionary> getAllIndonesianData() {
        Cursor cursor = database.query(TABLE_INDONESIA_DICTIONARY, null, null, null, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<ModelDictionary> arrayList = new ArrayList<>();
        ModelDictionary modelDictionary;
        if (cursor.getCount() > 0) {
            do {
                modelDictionary = new ModelDictionary();
                modelDictionary.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                modelDictionary.setWords(cursor.getString(cursor.getColumnIndexOrThrow(WORDS)));
                modelDictionary.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESC)));
                arrayList.add(modelDictionary);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public void insertEnglishDictionary(ModelDictionary dictionary) {
        String sql = "INSERT INTO " + TABLE_ENGLISH_DICTIONARY+ " (" + WORDS+ ", " + DESC
                + ") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, dictionary.getWords());
        stmt.bindString(2, dictionary.getDescription());
        stmt.execute();
        stmt.clearBindings();
    }
    public void insertIndonesianDictionary(ModelDictionary dictionary) {
        String sql = "INSERT INTO " + TABLE_INDONESIA_DICTIONARY+ " (" + WORDS+ ", " + DESC
                + ") VALUES (?, ?)";
        SQLiteStatement stmt = database.compileStatement(sql);
        stmt.bindString(1, dictionary.getWords());
        stmt.bindString(2, dictionary.getDescription());
        stmt.execute();
        stmt.clearBindings();
    }
    public ModelDictionary getDetail(long item,String table){
        String lang = "lang";
        if (table.equalsIgnoreCase("en")){
            lang = TABLE_ENGLISH_DICTIONARY;
        }else if(table.equalsIgnoreCase("id")){
            lang = TABLE_INDONESIA_DICTIONARY;
        }
        Cursor cursor = database.query(lang, null, _ID + " = ?",new String[]{String.valueOf(item)}, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        ModelDictionary dictionary = new ModelDictionary();
                dictionary = new ModelDictionary();
                dictionary.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                dictionary.setWords(cursor.getString(cursor.getColumnIndexOrThrow(WORDS)));
                dictionary.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESC)));
                cursor.moveToNext();
        cursor.close();
        return dictionary;

    }
    public ArrayList<ModelDictionary> getDataByWord(String keyword,String table) {
        String lang = "lang";
        if (table == "en"){
            lang = TABLE_ENGLISH_DICTIONARY;
        }else if(table == "id"){
            lang = TABLE_INDONESIA_DICTIONARY;
        }
        Cursor cursor = database.query(lang, null, WORDS + " LIKE ?", new String[]{keyword}, null, null, _ID + " ASC", null);
        cursor.moveToFirst();
        ArrayList<ModelDictionary> arrayList = new ArrayList<>();
        ModelDictionary dictionary;
        if (cursor.getCount() > 0) {
            do {
                dictionary = new ModelDictionary();
                dictionary.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                dictionary.setWords(cursor.getString(cursor.getColumnIndexOrThrow(WORDS)));
                dictionary.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESC)));
                arrayList.add(dictionary);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }
    public void beginTransaction() {
        database.beginTransaction();
    }
    public void setTransactionSuccess() {
        database.setTransactionSuccessful();
    }
    public void endTransaction() {
        database.endTransaction();
    }
}

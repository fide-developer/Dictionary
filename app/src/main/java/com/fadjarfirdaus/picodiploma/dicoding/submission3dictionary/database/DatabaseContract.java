package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    static String TABLE_ENGLISH_DICTIONARY = "table_english_dictionary";
    static String TABLE_INDONESIA_DICTIONARY = "table_indonesian_dictionary";

    static final class DictionaryColumns implements BaseColumns{
        static String WORDS = "words";
        static String DESC = "description";
    }
}

package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.services;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.LoadDataCallBack;
import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.R;
import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database.DictionaryHelper;
import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.model.ModelDictionary;
import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.pref.AppPreference;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class LoadDataAsync extends AsyncTask<Void, Integer, Boolean> {
    private final String TAG = LoadDataAsync.class.getSimpleName();
    private DictionaryHelper dictionaryHelper;
    private AppPreference appPreference;
    private WeakReference<LoadDataCallBack> weakCallback;
    private WeakReference<Resources> weakResources;
    double progress;
    double maxprogress = 100;

    LoadDataAsync(DictionaryHelper dictionaryHelper, AppPreference preference, LoadDataCallBack callback, Resources resources) {
        this.dictionaryHelper = dictionaryHelper;
        this.appPreference = preference;
        this.weakCallback = new WeakReference<>(callback);
        this.weakResources = new WeakReference<>(resources);
    }

    private ArrayList<ModelDictionary> preLoadRawEnglish() {
        ArrayList<ModelDictionary> englishModels = new ArrayList<>();
        String line;
        BufferedReader reader;
        try {
            Resources res = weakResources.get();
            InputStream raw_dict = res.openRawResource(R.raw.english_indonesia);
            reader = new BufferedReader(new InputStreamReader(raw_dict));
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");
                ModelDictionary englishModel;
                englishModel = new ModelDictionary(splitstr[0], splitstr[1]);
                englishModels.add(englishModel);
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return englishModels;
    }
    private ArrayList<ModelDictionary> preLoadRawIndonesia() {
        ArrayList<ModelDictionary> englishModels = new ArrayList<>();
        String line;
        BufferedReader reader;
        try {
            Resources res = weakResources.get();
            InputStream raw_dict = res.openRawResource(R.raw.indonesia_english);
            reader = new BufferedReader(new InputStreamReader(raw_dict));
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");
                ModelDictionary englishModel;
                englishModel = new ModelDictionary(splitstr[0], splitstr[1]);
                englishModels.add(englishModel);
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return englishModels;
    }

    @Override
    protected void onPreExecute() {
        Log.e(TAG, "onPreExecute");
        weakCallback.get().onPreLoad();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        weakCallback.get().onProgressUpdate(values[0]);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Boolean firstDlEnglish = appPreference.getFirstDlEnglish();
        Boolean firstDlIndonesian= appPreference.getFirstDlIndonesia();
        if (firstDlEnglish) {
            ArrayList<ModelDictionary> enDictionaryModels = preLoadRawEnglish();
            dictionaryHelper.open();
            progress = 30;
            publishProgress((int) progress);
            Double progressMaxInsert = 80.0;
            Double progressDiff = (progressMaxInsert - progress) / enDictionaryModels.size();
            boolean isInsertSuccess;
            try {
                dictionaryHelper.beginTransaction();
                for (ModelDictionary model : enDictionaryModels) {
                    dictionaryHelper.insertEnglishDictionary(model);
                    progress += progressDiff;
                    publishProgress((int) progress);
                }
                dictionaryHelper.setTransactionSuccess();
                isInsertSuccess = true;
                appPreference.setEnglishDownload(false);
                dictionaryHelper.endTransaction();
                dictionaryHelper.close();
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: fail2");
                isInsertSuccess = false;
            }
            publishProgress((int) maxprogress);
            return isInsertSuccess;
        }else if (firstDlIndonesian) {
            dictionaryHelper.open();
            ArrayList<ModelDictionary> idDictionaryModels = preLoadRawIndonesia();
            progress = 30;
            publishProgress((int) progress);
            Double progressMaxInsert = 80.0;
            Double progressDiff = (progressMaxInsert - progress) / idDictionaryModels.size();
            boolean isInsertSuccess;
            try {
                dictionaryHelper.beginTransaction();
                for (ModelDictionary model : idDictionaryModels) {
                    dictionaryHelper.insertIndonesianDictionary(model);
                    progress += progressDiff;
                    publishProgress((int) progress);
                }
                dictionaryHelper.setTransactionSuccess();
                isInsertSuccess = true;
                appPreference.setIndonesianDownload(false);
                dictionaryHelper.endTransaction();
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: fail1");
                isInsertSuccess = false;
            }
            dictionaryHelper.close();
            publishProgress((int) maxprogress);
            return isInsertSuccess;
        }else {
            try {
                synchronized (this) {
                    this.wait(2000);
                    publishProgress(50);
                    this.wait(2000);
                    publishProgress((int) maxprogress);
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }
    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            weakCallback.get().onLoadSuccess();
        } else {
            weakCallback.get().onLoadFailed();
        }
    }

}

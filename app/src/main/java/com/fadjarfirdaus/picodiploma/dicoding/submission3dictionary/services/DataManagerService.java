package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.LoadDataCallBack;
import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.database.DictionaryHelper;
import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.pref.AppPreference;

public class DataManagerService extends Service {
    public static final int PREPARATION_MESSAGE = 0;
    public static final int UPDATE_MESSAGE = 1;
    public static final int SUCCESS_MESSAGE = 2;
    public static final int FAILED_MESSAGE = 3;
    public static final String ACTIVITY_HANDLER = "activity_handler";
    private String TAG = DataManagerService.class.getSimpleName();
    private LoadDataAsync loadData;
    private Messenger mActivityMessenger;

    LoadDataCallBack myCallback = new LoadDataCallBack() {
        @Override
        public void onPreLoad() {
            Message message = Message.obtain(null, PREPARATION_MESSAGE);
            try {
                mActivityMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onProgressUpdate(long progress) {
            try {
                Message message = Message.obtain(null, UPDATE_MESSAGE);
                Bundle bundle = new Bundle();
                bundle.putLong("KEY_PROGRESS", progress);
                message.setData(bundle);
                mActivityMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLoadSuccess() {
            Message message = Message.obtain(null, SUCCESS_MESSAGE);
            try {
                mActivityMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onLoadFailed() {
            Message message = Message.obtain(null, FAILED_MESSAGE);
            try {
                mActivityMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    public void onCreate() {
        super.onCreate();
        DictionaryHelper dictionaryHelper = DictionaryHelper.getInstance(getApplicationContext());
        AppPreference appPreference = new AppPreference(getApplicationContext());
        loadData = new LoadDataAsync(dictionaryHelper, appPreference, myCallback, getResources());
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        mActivityMessenger = intent.getParcelableExtra(ACTIVITY_HANDLER);
        loadData.execute();
        return mActivityMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind: ");
    }
}

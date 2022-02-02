package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.services.DataManagerService;

import java.lang.ref.WeakReference;

import static com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.services.DataManagerService.FAILED_MESSAGE;
import static com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.services.DataManagerService.PREPARATION_MESSAGE;
import static com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.services.DataManagerService.SUCCESS_MESSAGE;
import static com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary.services.DataManagerService.UPDATE_MESSAGE;

public class LoadDataActivity extends AppCompatActivity implements HandlerCallback{
    ProgressBar progressBar;
    TextView textProgress;
    Messenger mActivityMessenger;
    Messenger mBoundService;
    boolean mServiceBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);
        progressBar = findViewById(R.id.progress_bar);
        textProgress = findViewById(R.id.text_loading);

        Intent mBoundServiceIntent = new Intent(LoadDataActivity.this, DataManagerService.class);
        mActivityMessenger = new Messenger(new IncomingHandler(this));
        mBoundServiceIntent.putExtra(DataManagerService.ACTIVITY_HANDLER, mActivityMessenger);
        bindService(mBoundServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = new Messenger(service);
            mServiceBound = true;
        }
    };

    @Override
    public void preparation() {
        textProgress.setText(R.string.on_load);
    }

    @Override
    public void updateProgress(long progress) {
        progressBar.setProgress((int) progress);
    }

    @Override
    public void loadSuccess() {
        Toast.makeText(this, "Download Complete", Toast.LENGTH_LONG).show();
        startActivity(new Intent(LoadDataActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void loadFailed() {
        Toast.makeText(this, "Failed to download", Toast.LENGTH_LONG).show();
    }

    private static class IncomingHandler extends Handler {
        WeakReference<HandlerCallback> weakCallback;
        IncomingHandler(HandlerCallback callback) {
            weakCallback = new WeakReference<>(callback);
        }
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PREPARATION_MESSAGE:
                    weakCallback.get().preparation();
                    break;
                case UPDATE_MESSAGE:
                    Bundle bundle = msg.getData();
                    long progress = bundle.getLong("KEY_PROGRESS");
                    weakCallback.get().updateProgress(progress);
                    break;
                case SUCCESS_MESSAGE:
                    weakCallback.get().loadSuccess();
                    break;
                case FAILED_MESSAGE:
                    weakCallback.get().loadFailed();
                    break;
            }
        }
    }
}

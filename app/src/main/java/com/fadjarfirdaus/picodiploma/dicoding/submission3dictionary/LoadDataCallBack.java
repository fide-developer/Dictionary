package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary;

public interface LoadDataCallBack {
    void onPreLoad();
    void onProgressUpdate(long progress);
    void onLoadSuccess();
    void onLoadFailed();
}

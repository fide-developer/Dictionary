package com.fadjarfirdaus.picodiploma.dicoding.submission3dictionary;

interface HandlerCallback {
    void preparation();
    void updateProgress(long progress);
    void loadSuccess();
    void loadFailed();
}
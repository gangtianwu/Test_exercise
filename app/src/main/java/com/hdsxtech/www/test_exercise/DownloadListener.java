package com.hdsxtech.www.test_exercise;

/**
 * 作者:丁文 on 2017/10/18.
 * copyright: www.tpri.org.cn
 */

public interface DownloadListener {
    void onProgress(int process);
    void onSuccess();
    void onFailed();
    void onPaused();
    void onCanceled();
}

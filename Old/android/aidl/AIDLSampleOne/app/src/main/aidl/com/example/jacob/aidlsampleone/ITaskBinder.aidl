// ITaskBinder.aidl
package com.example.jacob.aidlsampleone;

import com.example.jacob.aidlsampleone.ITaskCallback;
// Declare any non-default types here with import statements

interface ITaskBinder {
    boolean isTaskRunning();
    void stopRunningTask();
    void registerCallback(ITaskCallback cb);
    void unregisterCallback(ITaskCallback cb);
}

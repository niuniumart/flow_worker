package com.zdf.client.core;

import com.zdf.client.data.AsyncTaskBase;
import com.zdf.client.data.ScheduleConfig;

import java.util.List;

public interface ObserverFunction {
    void onBoot();
    void onObtain(List<AsyncTaskBase> asyncTaskBaseList);
    void onExecute(AsyncTaskBase asyncTaskReturn);
    void onFinish(AsyncTaskBase asyncTaskReturn);
    void onStop(AsyncTaskBase asyncTaskReturn);
    void onError(AsyncTaskBase asyncTaskReturn, ScheduleConfig scheduleConfig, List<AsyncTaskBase> asyncTaskBaseList, Exception e);

}

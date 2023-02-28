package com.zdf.client.core.observers;

import com.alibaba.fastjson.JSON;
import com.zdf.client.Client.TaskFlower;
import com.zdf.client.Client.TaskFlowerImpl;
import com.zdf.client.boot.AppLaunch;
import com.zdf.client.constant.UserConfig;
import com.zdf.client.core.AnnType;
import com.zdf.client.core.ObserverFunction;
import com.zdf.client.data.AsyncTaskBase;
import com.zdf.client.data.AsyncTaskSetRequest;
import com.zdf.client.data.ScheduleConfig;
import com.zdf.client.data.ScheduleLog;
import com.zdf.client.enums.TaskStatus;

import java.util.List;
import java.util.UUID;

public class TimeObserver implements ObserverFunction{
    private Long beginTime;
    TaskFlower taskFlower = new TaskFlowerImpl();

    @Override
    @AnnType(observerType = AppLaunch.ObserverType.onObtain)
    public void onObtain(List<AsyncTaskBase> asyncTaskBaseList) {
        System.out.println("改变任务为执行中。。。");
        for (AsyncTaskBase asyncTaskBase : asyncTaskBaseList) {
            setTaskNow(modifyStatus(asyncTaskBase, TaskStatus.EXECUTING));
        }
    }
    @Override
    @AnnType(observerType = AppLaunch.ObserverType.onExecute)
    public void onExecute(AsyncTaskBase asyncTaskReturn) {
        this.beginTime = System.currentTimeMillis();
        System.out.println(asyncTaskReturn.getTask_type() + "开始执行。");
    }
    @Override
    @AnnType(observerType = AppLaunch.ObserverType.onBoot)
    public void onBoot() {
        System.out.println(UserConfig.USERID + "的线程" + Thread.currentThread().getName() + "取任务");
    }
    @Override
    @AnnType(observerType = AppLaunch.ObserverType.onError)
    public void onError(AsyncTaskBase asyncTaskReturn, ScheduleConfig scheduleConfig, List<AsyncTaskBase> asyncTaskBaseList, Exception e) {
        if (asyncTaskReturn.getCrt_retry_num() < 60) {
            if (asyncTaskReturn.getCrt_retry_num() != 0) {
                asyncTaskReturn.setMax_retry_num(asyncTaskReturn.getCrt_retry_num() << 1);
            }
        } else {
            asyncTaskReturn.setMax_retry_interval(scheduleConfig.getRetry_interval());
        }
        if (asyncTaskReturn.getMax_retry_interval() > scheduleConfig.getRetry_interval()) {
            asyncTaskReturn.setMax_retry_interval(scheduleConfig.getRetry_interval());
        }
        asyncTaskReturn.getSchedule_log().getLastData().setErrMsg(e.getMessage());
        if (asyncTaskReturn.getMax_retry_num() == 0 || asyncTaskReturn.getCrt_retry_num() >= asyncTaskReturn.getMax_retry_num()) {
            AsyncTaskSetRequest asyncTaskSetRequest = modifyStatus(asyncTaskReturn, TaskStatus.FAIL);
            asyncTaskSetRequest.setCrt_retry_num(asyncTaskReturn.getCrt_retry_num());
            asyncTaskSetRequest.setMax_retry_interval(asyncTaskReturn.getMax_retry_interval());
            asyncTaskSetRequest.setMax_retry_num(asyncTaskReturn.getMax_retry_num());
            setTaskNow(asyncTaskSetRequest);
            return;
        }
        System.out.println(asyncTaskReturn.getTask_type() + "任务执行出错！");
        e.printStackTrace();
        if (asyncTaskReturn.getStatus() != TaskStatus.FAIL.getStatus()) {
            asyncTaskReturn.setCrt_retry_num(asyncTaskReturn.getCrt_retry_num() + 1);
        }
        asyncTaskBaseList.add(asyncTaskReturn);
    }
    @Override
    @AnnType(observerType = AppLaunch.ObserverType.onFinish)
    public void onFinish(AsyncTaskBase asyncTaskReturn){
        ScheduleLog schedule_log = asyncTaskReturn.getSchedule_log();
        long cost = System.currentTimeMillis() - beginTime;
        schedule_log.getLastData().setTraceId(UUID.randomUUID() + "");
        schedule_log.getLastData().setCost(cost + "");
        schedule_log.getLastData().setErrMsg("");
        schedule_log.getHistoryDatas().add(schedule_log.getLastData());
        if (schedule_log.getHistoryDatas().size() > 3) {
            schedule_log.getHistoryDatas().remove(0);
        }
        AsyncTaskSetRequest asyncTaskSetRequest = modifyStatus(asyncTaskReturn, TaskStatus.SUCCESS);
        asyncTaskReturn.setMax_retry_interval(0);
        asyncTaskSetRequest.setStatus(asyncTaskReturn.getStatus());
        asyncTaskSetRequest.setSchedule_log(JSON.toJSONString(asyncTaskReturn.getSchedule_log()));
        asyncTaskSetRequest.setCrt_retry_num(asyncTaskReturn.getCrt_retry_num());
        asyncTaskSetRequest.setMax_retry_interval(asyncTaskReturn.getMax_retry_interval());
        asyncTaskSetRequest.setMax_retry_num(asyncTaskReturn.getMax_retry_num());
        setTaskNow(asyncTaskSetRequest);
        System.out.println(asyncTaskReturn.getTask_type() + "执行完毕！");
    }
    @Override
    @AnnType(observerType = AppLaunch.ObserverType.onStop)
    public void onStop(AsyncTaskBase asyncTaskReturn){
    }

    public AsyncTaskSetRequest modifyStatus(AsyncTaskBase asyncTaskBase, TaskStatus taskStatus) {
        asyncTaskBase.setStatus(taskStatus.getStatus());
        AsyncTaskSetRequest asyncTaskSetRequest = new AsyncTaskSetRequest(asyncTaskBase.getTask_id());
        asyncTaskSetRequest.setStatus(taskStatus.getStatus());
        return asyncTaskSetRequest;
    }

    public void setTaskNow(AsyncTaskSetRequest asyncTaskSetRequest) {
        taskFlower.setTask(asyncTaskSetRequest);
    }
}

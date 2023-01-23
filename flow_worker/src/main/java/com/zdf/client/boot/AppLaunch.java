package com.zdf.client.boot;

import com.alibaba.fastjson.JSON;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.zdf.client.Client.TaskFlower;
import com.zdf.client.Client.TaskFlowerImpl;
import com.zdf.client.core.ObserverManager;
import com.zdf.client.enums.TaskStatus;
import com.zdf.client.constant.TaskConstant;
import com.zdf.client.core.observers.TimeObserver;
import com.zdf.client.data.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class AppLaunch implements Launch{
    final TaskFlower taskFlower;
    public String packageName;
    private Long intervalTime;
    private String taskType;
    private int scheduleLimit;
    public Long cycleScheduleConfigTime = 10000L;
    public static int MaxConcurrentRunTimes = 5;
    public static int concurrentRunTimes = MaxConcurrentRunTimes;
    Map<String, ScheduleConfig> scheduleCfgDic;
    Class<?> larkTaskClass;
    Logger logger = LoggerFactory.getLogger(AppLaunch.class);
    ScheduledExecutorService threadPoolExecutor;

    public AppLaunch(Class<?> taskType) {
        this(taskType, 0);
    }
    public AppLaunch(Class<?> taskType, int scheduleLimit) {
        this.larkTaskClass = taskType;
        this.taskType = this.larkTaskClass.getSimpleName();
        scheduleCfgDic = new ConcurrentHashMap<>();
        taskFlower = new TaskFlowerImpl();
        this.packageName = taskType.getPackage().getName();
        this.scheduleLimit = scheduleLimit;
        this.threadPoolExecutor = Executors.newScheduledThreadPool(MaxConcurrentRunTimes);
        init();
    }

    public int getScheduleLimit() {
        return scheduleLimit;
    }

    public void setScheduleLimit(int scheduleLimit) {
        this.scheduleLimit = scheduleLimit;
    }

    @Override
    public int start() {
        ScheduleConfig scheduleConfig = scheduleCfgDic.get(this.taskType);
        // take the task at fixed rate
        intervalTime = scheduleConfig.getSchedule_interval() == 0 ? TaskConstant.DEFAULT_TIME_INTERVAL * 1000L : scheduleConfig.getSchedule_interval() * 1000L;
        // 前后波动500ms
        int step = (int) (Math.random() * 500 + 1);
        intervalTime += step;
        threadPoolExecutor.scheduleAtFixedRate(this::execute, 0, intervalTime, TimeUnit.MILLISECONDS);
        return 0;
    }

    public void execute() {
        ObserverManager observerManager = new ObserverManager();
        observerManager.registerEventObserver(new TimeObserver());
        try {
            observerManager.wakeupObserver(ObserverType.onBoot);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        List<AsyncTaskReturn> taskList = taskFlower.getTaskList(larkTaskClass, TaskStatus.PENDING.getStatus(), scheduleCfgDic.get(larkTaskClass.getSimpleName()).getSchedule_limit());
        if (taskList.size() == 0) {
            logger.warn("no task to deal!");
            return;
        }
        List<AsyncTaskBase> asyncTaskBaseList = convertModel(taskList);
        int size = asyncTaskBaseList.size();
        for (int i = 0; i < size; i++) {
            AsyncTaskBase v = asyncTaskBaseList.get(i);
            try {
                observerManager.wakeupObserver(ObserverType.onExecute, v);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            try {
                Class<?> aClass = Class.forName(packageName + "." + v.getTask_type());
                Method method = aClass.getMethod(v.getTask_stage(), v.getTask_context().getClazz());
                Object returnVal = method.invoke(null, v.getTask_context().getParams());
            } catch (Exception e) {
                try {
                    observerManager.wakeupObserver(ObserverType.onError, v, scheduleCfgDic.get(v.getTask_type()), asyncTaskBaseList, e);
                    size = asyncTaskBaseList.size();
                    continue;
                } catch (InvocationTargetException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                observerManager.wakeupObserver(ObserverType.onFinish, v);
            } catch (InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadCfg() {
        TaskFlower taskFlower = new TaskFlowerImpl();
        List<ScheduleConfig> taskTypeCfgList = taskFlower.getTaskTypeCfgList();
        for (ScheduleConfig scheduleConfig : taskTypeCfgList) {
            scheduleCfgDic.put(scheduleConfig.getTask_type(), scheduleConfig);
        }
    }

    @Override
    public int init() {
        if (scheduleLimit != 0) {
            logger.debug("init ScheduleLimit : %d", scheduleLimit);
            concurrentRunTimes = scheduleLimit;
            MaxConcurrentRunTimes = scheduleLimit;
        }
        loadCfg();
        threadPoolExecutor.scheduleAtFixedRate(this::loadCfg, cycleScheduleConfigTime, cycleScheduleConfigTime,TimeUnit.MILLISECONDS);
        return 0;
    }


    public List<AsyncTaskBase> convertModel(List<AsyncTaskReturn> asyncTaskReturnList) {
        List<AsyncTaskBase> asyncTaskBaseList = new ArrayList<>();
        for (AsyncTaskReturn asyncTaskReturn : asyncTaskReturnList) {
            AsyncTaskBase asyncTaskBase = new AsyncTaskBase();
            asyncTaskBase.setUser_id(asyncTaskReturn.getUser_id());
            asyncTaskBase.setTask_id(asyncTaskReturn.getTask_id());
            asyncTaskBase.setTask_type(asyncTaskReturn.getTask_type());
            asyncTaskBase.setTask_stage(asyncTaskReturn.getTask_stage());
            asyncTaskBase.setCrt_retry_num(asyncTaskReturn.getCrt_retry_num());
            asyncTaskBase.setMax_retry_num(asyncTaskReturn.getMax_retry_num());
            asyncTaskBase.setMax_retry_interval(asyncTaskReturn.getMax_retry_interval());
            asyncTaskBase.setCreate_time(asyncTaskReturn.getCreate_time());
            asyncTaskBase.setModify_time(asyncTaskReturn.getModify_time());
            asyncTaskBase.setSchedule_log(JSON.parseObject(asyncTaskReturn.getSchedule_log(), ScheduleLog.class));
            asyncTaskBase.setTask_context(JSON.parseObject(asyncTaskReturn.getTask_context(), NftTaskContext.class));
            asyncTaskBase.setTask_id(asyncTaskReturn.getTask_id());
            asyncTaskBase.setStatus(asyncTaskReturn.getStatus());
            asyncTaskBaseList.add(asyncTaskBase);
        }

        return asyncTaskBaseList;
    }

    @Override
    public int destroy() {
        return 0;
    }

    public enum ObserverType {
        onBoot(0),
        onError(1),
        onExecute(2),
        onFinish(3),
        onStop(4), onObtain(5);
        private int code;

        private ObserverType(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }
}

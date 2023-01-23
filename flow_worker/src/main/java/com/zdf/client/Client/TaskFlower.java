package com.zdf.client.Client;

import com.zdf.client.data.*;

import java.lang.reflect.Method;
import java.util.List;

public interface TaskFlower {
    public String createTask(AsyncTaskRequest asyncTaskRequest);
    public void setTask(AsyncTaskSetRequest asyncTaskSetRequest);
    public AsyncTaskReturn getTask(Long taskId);
    public List<AsyncTaskReturn> getTaskList(Class<?> clazz, int status, int limit);
    public List<ScheduleConfig> getTaskTypeCfgList();
}

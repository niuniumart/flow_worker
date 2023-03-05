package com.zdf.client.Client;

import com.zdf.client.data.AsyncTaskRequest;
import com.zdf.client.data.AsyncTaskReturn;
import com.zdf.client.data.AsyncTaskSetRequest;
import com.zdf.client.data.ScheduleConfig;
import com.zdf.client.enums.TaskStatus;

import java.util.List;

public interface TaskFlower {
    public String createTask(AsyncTaskRequest asyncTaskRequest);
    public void setTask(AsyncTaskSetRequest asyncTaskSetRequest);
    public AsyncTaskReturn getTask(Long taskId);
    public List<AsyncTaskReturn> getTaskList(Class<?> clazz, int status, int limit);
    public List<ScheduleConfig> getTaskTypeCfgList();
    public List<AsyncTaskReturn> getUserTaskList(List<TaskStatus> taskStatuses);
    public void createTaskCFG(ScheduleConfig scheduleConfig);


}

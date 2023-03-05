package com.zdf.client.rpc;

import com.zdf.client.data.*;


public interface FlowServer {
    ReturnStatus getTaskList(String taskType, int status, int limit);
    ReturnStatus createTask(AsyncTaskRequest asyncTaskRequest);
    ReturnStatus setTask(AsyncTaskSetRequest asyncTaskSetRequest);
    ReturnStatus getTask(Long taskId);

    ReturnStatus getTaskTypeCfgList();
    ReturnStatus getUserTaskList(String user_id, int statusList);
    ReturnStatus createTaskCFG(ScheduleConfig scheduleConfig);

}

package com.zdf.client.test;

import com.zdf.client.Client.TaskBuilder;
import com.zdf.client.Client.TaskFlower;
import com.zdf.client.Client.TaskFlowerImpl;
import com.zdf.client.data.*;
import com.zdf.client.enums.TaskStatus;
import com.zdf.client.task.LarkTask;

import java.lang.reflect.Method;
import java.util.List;

public class Test {
    static TaskFlower taskFlower = new TaskFlowerImpl();
    public static void main(String[] args) {
        testCeateTask();
//        testSetTask();

    }

    private static void testGetTaskList() {
        List<AsyncTaskReturn> larkTask = taskFlower.getTaskList(LarkTask.class, 1, 5);
        System.out.println(larkTask);
    }

    private static void testSetTask() {
        AsyncTaskSetRequest asyncTaskSetRequest = new AsyncTaskSetRequest(
                7891977538371584L
        );
        asyncTaskSetRequest.setStatus(TaskStatus.PENDING.getStatus());
        taskFlower.setTask(asyncTaskSetRequest);
    }

    private static void testGetTask() {
        AsyncTaskReturn task = taskFlower.getTask(7403463373750272L);
        System.out.println(task);
    }

    private static void testCeateTask() {
        AsyncFlowClientData asyncFlowClientData = TaskBuilder.build(LarkTask.class,"printMsg", new String[]{"I did it!"}, new Object[0]);
        String task = taskFlower.createTask(new AsyncTaskRequest(asyncFlowClientData));
        System.out.println(task);
    }
}

package com.zdf.client.enums;

import java.util.ArrayList;
import java.util.List;

public enum TaskStatus {
    PENDING(0x01),
    EXECUTING(0x02),
    SUCCESS(0x04),
    FAIL(0x08);

    private TaskStatus(int status) {
        this.status = status;
    }
    private int status;

    public int getStatus() {
        return this.status;
    }

    public List<TaskStatus> getAliveStatus() {
        List<TaskStatus> aliveList = new ArrayList<>();
        aliveList.add(PENDING);
        aliveList.add(EXECUTING);
        return aliveList;
    }
    public List<TaskStatus> getFailStatus() {
        List<TaskStatus> failList = new ArrayList<>();
        failList.add(FAIL);
        return failList;
    }

    public List<TaskStatus> getSuccessStatus() {
        List<TaskStatus> sucList = new ArrayList<>();
        sucList.add(SUCCESS);
        return sucList;
    }

    public List<TaskStatus> getAllStatus() {
        List<TaskStatus> list = new ArrayList<>();
        for (TaskStatus value : TaskStatus.values()) {
            list.add(value);
        }
        return list;
    }
}

package com.zdf.client.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ScheduleLog {
    ScheduleData lastData;
    List<ScheduleData> historyDatas;
    public ScheduleLog() {
        lastData = new ScheduleData();
        historyDatas = new ArrayList<>();
    }
}

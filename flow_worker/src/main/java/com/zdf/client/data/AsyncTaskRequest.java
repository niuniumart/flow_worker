package com.zdf.client.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsyncTaskRequest {
    AsyncFlowClientData taskData;
}

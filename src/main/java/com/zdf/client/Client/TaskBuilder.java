package com.zdf.client.Client;

import com.alibaba.fastjson.JSON;
import com.zdf.client.constant.UserConfig;
import com.zdf.client.data.AsyncFlowClientData;
import com.zdf.client.data.NftTaskContext;
import com.zdf.client.data.ScheduleLog;

import java.lang.reflect.Method;

/**
 * @author zhangdafeng
 */
public class TaskBuilder {
    public static AsyncFlowClientData build(Class<?> clazz, String methodName, Object[] params, Object... envs) {
        Method method = null;
        for (Method clazzMethod : clazz.getMethods()) {
            if (clazzMethod.getName().equals(methodName)) {
                method = clazzMethod;
            }
        }
        int parameterCount = method.getParameterCount();
        if (params.length != parameterCount) {
            throw new RuntimeException("Parameters are invalid!");
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterCount; i++) {
            if (params[i].getClass() != parameterTypes[i]) {
                throw new RuntimeException(params[i].getClass().getName() + " can't cast to " + parameterTypes[i] + ".");
            }
        }
        String taskType = method.getDeclaringClass().getSimpleName();
        String taskStage = method.getName();
        ScheduleLog sl = new ScheduleLog();
        String scheduleLog = JSON.toJSONString(sl);

        NftTaskContext nftTaskContext = new NftTaskContext(params, envs, parameterTypes);
        String taskContext = JSON.toJSONString(nftTaskContext);
        return new AsyncFlowClientData(
                UserConfig.USERID,
                taskType,
                taskStage,
                scheduleLog,
                taskContext
        );
    }
}

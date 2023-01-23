package com.zdf.client.rpc;

import com.alibaba.fastjson.JSON;
import com.zdf.client.constant.TaskUrl;
import com.zdf.client.data.AsyncFlowTask;
import com.zdf.client.data.AsyncTaskRequest;
import com.zdf.client.data.AsyncTaskSetRequest;
import com.zdf.client.data.ReturnStatus;
import com.zdf.client.rpc.FlowServer;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FlowServerImpl implements FlowServer {
    OkHttpClient client = new OkHttpClient();

    public ReturnStatus get(String url) {

        Request request = new Request.Builder().url(url)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            String result = response.body().string();
            return JSON.parseObject(result, ReturnStatus.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getParamStr(Map<String, String> params) {
        StringBuffer sb = new StringBuffer();
        sb.append("?");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public <E> ReturnStatus post(String url, E body) {
        Request request = new Request.Builder()
                .addHeader("content-type", "application/json")
                .url(TaskUrl.IPORT + url)
                .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JSON.toJSONString(body)))
                .build();

        String result;
        try {
            result = client.newCall(request).execute().body().string();
            return JSON.parseObject(result, ReturnStatus.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public ReturnStatus getTaskList(String taskType, int status, int limit) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("task_type", taskType);
            put("status", status + "");
            put("limit", limit + "");
        }};
        String url = TaskUrl.IPORT + TaskUrl.GET_TASK_LIST + getParamStr(params);
        return get(url);
    }

    @Override
    public ReturnStatus createTask(AsyncTaskRequest asyncTaskRequest) {
        return post(TaskUrl.CREATE_TASK, asyncTaskRequest);
    }

    @Override
    public ReturnStatus setTask(AsyncTaskSetRequest asyncTaskSetRequest) {
        return post(TaskUrl.SET_TASK, asyncTaskSetRequest);
    }

    @Override
    public ReturnStatus getTask(Long taskId) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", taskId + "");
        String url = TaskUrl.IPORT + TaskUrl.GET_TASK + getParamStr(params);
        return get(url);
    }

    @Override
    public ReturnStatus getTaskTypeCfgList() {
       return get(TaskUrl.IPORT + TaskUrl.GET_CFG_LIST);
    }

}

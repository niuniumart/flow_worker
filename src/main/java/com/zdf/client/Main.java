package com.zdf.client;

import com.zdf.client.boot.AppLaunch;
import com.zdf.client.boot.Launch;
import com.zdf.client.task.LarkTask;


public class Main {
    public static void main(String[] args) {
        Launch l = new AppLaunch(LarkTask.class);
        l.start();
    }
}

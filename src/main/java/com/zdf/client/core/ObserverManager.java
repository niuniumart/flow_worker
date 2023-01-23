package com.zdf.client.core;

import com.zdf.client.boot.AppLaunch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ObserverManager {
    List<ObserverFunction> observers;

    public ObserverManager() {
        observers = new ArrayList<>();
    }
    public void registerEventObserver(ObserverFunction observerFunction) {
        observers.add(observerFunction);
    }

    public void wakeupObserver(AppLaunch.ObserverType observerType, Object... params) throws InvocationTargetException, IllegalAccessException {
        for (ObserverFunction observer : observers) {
            for (Method method : observer.getClass().getMethods()) {
                if (method.getName().equals(observerType.name())) {
                    method.invoke(observer, params);
                }
            }
        }
    }
}

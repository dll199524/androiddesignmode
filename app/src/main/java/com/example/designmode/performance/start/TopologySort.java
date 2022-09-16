package com.example.designmode.performance.start;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopologySort {

    public static StartUpStore sort(List<? extends StartUp<?>> startUpList) {
        //入度 0度
        Map<Class<? extends StartUp>, Integer> inDegree = new HashMap<>();
        Deque<Class<? extends StartUp>> zeroDeque = new ArrayDeque<>();

        //
        Map<Class<? extends StartUp>, StartUp<?>> startUpMap = new HashMap<>();
        Map<Class<? extends StartUp>, List<Class <? extends StartUp>>> childStartUpMap = new HashMap<>();

        for (StartUp<?> startUp : startUpList) {
            startUpMap.put(startUp.getClass(), startUp);
            int dependenceCount = startUp.getDependiceCount();
            inDegree.put(startUp.getClass(), dependenceCount);
            if (dependenceCount == 0)
                zeroDeque.offer(startUp.getClass());
            else {
                for (Class<? extends StartUp<?>> parent : startUp.dependencies()) {
                    List<Class <? extends StartUp>> child = childStartUpMap.get(parent);
                    if (child == null) {
                        child = new ArrayList<>();
                        childStartUpMap.put(parent, child);
                    }
                    child.add(startUp.getClass());
                }
            }
        }

        List<StartUp<?>> result = new ArrayList<>();
        List<StartUp<?>> main = new ArrayList<>();
        List<StartUp<?>> threads = new ArrayList<>();
        while (!zeroDeque.isEmpty()) {
            Class<? extends StartUp> clazz = zeroDeque.poll();
            StartUp<?> start = startUpMap.get(clazz);
            if (start.isMainThread())
                main.add(start);
            else threads.add(start);
            if (childStartUpMap.containsKey(clazz)) {
                List<Class <? extends StartUp>> childStart = childStartUpMap.get(clazz);
                for (Class<? extends StartUp> childClazz : childStart) {
                    int num = inDegree.get(childClazz);
                    inDegree.put(childClazz, num - 1);
                    if (num - 1 == 0) zeroDeque.offer(childClazz);
                }
            }
            result.addAll(main);
            result.addAll(threads);
        }
        return new StartUpStore(result, startUpMap, childStartUpMap);
    }

}

package com.example.designmode.performance.start;

import java.util.List;
import java.util.Map;

public class StartUpStore {

    List<StartUp<?>> result;
    Map<Class<? extends StartUp>, StartUp<?>> startUpMap;
    Map<Class<? extends StartUp>, List<Class<? extends StartUp>>> childStartUpMap;
    public StartUpStore(List<StartUp<?>> result, Map<Class<? extends StartUp>, StartUp<?>> startUpMap, Map<Class<? extends StartUp>, List<Class<? extends StartUp>>> childStartUpMap) {
        this.result = result;
        this.startUpMap = startUpMap;
        this.childStartUpMap = childStartUpMap;
    }

    public List<StartUp<?>> getResult() {return result;}

    public void setResult(List<StartUp<?>> result) {this.result = result;}

    public Map<Class<? extends StartUp>, StartUp<?>> getStartUpMap() {return startUpMap;}

    public void setStartUpMap(Map<Class<? extends StartUp>, StartUp<?>> startUpMap) {this.startUpMap = startUpMap;}

    public Map<Class<? extends StartUp>, List<Class<? extends StartUp>>> getChildStartUpMap() {return childStartUpMap;}

    public void setChildStartUpMap(Map<Class<? extends StartUp>, List<Class<? extends StartUp>>> childStartUpMap) {this.childStartUpMap = childStartUpMap;}
}

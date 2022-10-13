package com.example.designmode.performance.mem;

import android.os.Debug;

public class MemoryInfo {
    public String procName;
    public AppMemory appMemory;
    public SystemMemory systemMemory;
    public String display;
    public int activityCount;



    public static class AppMemory {
        public long dalvikPss;  //java占内存大小
        public long nativePss;  //
        public long totalPss;
        public Debug.MemoryInfo memoryInfo;
    }

    public static class SystemMemory {
        public long availMem;
        public boolean lowMem;
        public long threshold;
        public long totalMem;
    }
}

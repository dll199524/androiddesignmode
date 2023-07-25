package com.designmode.plugin.dexPlugin;

public class PatchExtension {
    boolean isDebug = true;
    String outPut;
    String applicationName;

    public void setDebug(boolean debug) {
        isDebug = debug;
    }

    public void setOutPut(String outPut) {
        this.outPut = outPut;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}

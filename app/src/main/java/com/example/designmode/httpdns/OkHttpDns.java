package com.example.designmode.httpdns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;

/**
 * 网络优化缓存dns
 * HttpDnsCache dnsCache = HttpDnsCache.getInstance();
 * dnsCache.init(....);
 * dnsCache.loadDnsCache();
 *
 */
public class OkHttpDns implements Dns {

    private static volatile OkHttpDns instance;
    private HttpDnsCache httpDnsCache = HttpDnsCache.getInstance();
    private static String TAG = "OkHttpDns";
    private OkHttpDns() {}

    public static OkHttpDns getInstance() {
        if (instance == null) {
            synchronized (OkHttpDns.class) {
                if (instance == null) instance = new OkHttpDns();
            }
        }
        return instance;
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        String ip = httpDnsCache.getIpByHostAsync(hostname);
        if (ip != null)
            return Arrays.asList(InetAddress.getAllByName(ip));
        return Dns.SYSTEM.lookup(hostname);
    }
}

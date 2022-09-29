package com.example.designmode.httpdns;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpDnsCache {
    private static volatile HttpDnsCache instance;
    private static final String TAG = "HttpDnsCache";

    private String[] mHosts;
    private Map<String, HttpDnsRes.DnsBean> mDnsCaches = new HashMap<>();
    private OkHttpClient client;
    private String HttpDnsServerIp = "203.107.1.33";
    private int account_id = 191607;
    private static final int RETRY_TIMES = 2;
    private int retryTimes = 0;
    private boolean preLoadSync = false;

    private HttpDnsCache() {}

    public static HttpDnsCache getInstance() {
        if (instance == null) {
            synchronized (HttpDnsCache.class) {
                if (instance == null)
                    instance = new HttpDnsCache();
            }
        }
        return instance;
    }

    public void init(int accountId, String... hosts) {
        account_id = accountId;
        mHosts = hosts;
        client = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .build();
    }

    public boolean loadDnsCache() {
        if (mHosts == null && mHosts.length == 0)
            throw new NullPointerException("preload dns, hostname can not be null");
        StringBuilder http_url = new StringBuilder("http://" + HttpDnsServerIp + "/" + account_id);
        if (mHosts.length == 1)
            http_url.append("/d?host=").append(mHosts[0]);
        else {
            http_url.append("/resolve?host=");
            for (String hostName : mHosts)
                http_url.append(hostName).append(",");
            http_url.deleteCharAt(http_url.length() - 1);
        }
        Request request = new Request.Builder()
                .url(http_url.toString())
                .build();

        if (preLoadSync)
            return requestCacheSync(request);
        else {
            requestCache(request);
            return true;
        }

    }

    private boolean requestCacheSync(Request request) {
        try (Response response = client.newCall(request).execute()) {
            return handleResponse(request, response);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "requestCacheSync: " + e);
            return false;
        }
    }

    private void requestCache(final Request request) {
        retryTimes++;
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                if (retryTimes < RETRY_TIMES)
                    requestCache(request);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResponse(request, response);
            }
        });
    }


    /***
     *
     * {
     *     "dns": [
     *         {
     *             "host": "api.bilibili.com",
     *             "client_ip": "58.49.114.214",
     *             "ips": [
     *                 "116.207.118.12"
     *             ],
     *             "ttl": 95,
     *             "origin_ttl": 180
     *         },
     *         {
     *             "host": "app.bilibili.com",
     *             "client_ip": "58.49.114.214",
     *             "ips": [
     *                 "116.207.118.12"
     *             ],
     *             "ttl": 180,
     *             "origin_ttl": 180
     *         }
     *     ]
     * }
     *
     */

    private boolean handleResponse(Request request, Response response) {
        if (response.isSuccessful()) {
            String res = response.body().toString();
            if (request.url().encodedPath().contains("d")) {
                HttpDnsRes.DnsBean bean = new Gson().fromJson(res, HttpDnsRes.DnsBean.class);
                saveCache(bean);
            } else if (request.url().encodedPath().contains("resolve")) {
                HttpDnsRes dnsRes = new Gson().fromJson(res, HttpDnsRes.class);
                if (dnsRes == null) throw new NullPointerException("dns resovle return null");
                for (HttpDnsRes.DnsBean bean : dnsRes.getDns())
                    saveCache(bean);
            }
            return true;
        }
        return false;
    }

    public void setTimeOutInterval(int timeOutInterval) {
        client = new OkHttpClient.Builder()
                .connectTimeout(timeOutInterval, TimeUnit.SECONDS)
                .readTimeout(timeOutInterval, TimeUnit.SECONDS)
                .build();
    }

    public void saveCache(HttpDnsRes.DnsBean bean) {mDnsCaches.put(bean.getHost(), bean);}

    public String getHttpDnsServerIp() {return HttpDnsServerIp;}

    public void setHttpDnsServerIp(String httpDnsServerIp) {HttpDnsServerIp = httpDnsServerIp;}

    public String getIpByHostAsync(String hostname) {
        if (mHosts == null || mHosts.length == 0) return null;
        for (String host : mHosts) {
            if (host.equals(hostname)) {
                if (mDnsCaches == null || mDnsCaches.isEmpty()) {
                    updateIpByHostAsync(hostname);
                    return null;
                } else {
                    HttpDnsRes.DnsBean bean = mDnsCaches.get(hostname);
                    if (bean != null) {
                        long timeSpan = Math.abs(System.currentTimeMillis() - bean.getTime());
                        if (timeSpan < bean.getTtl() * 1000)
                            if (!bean.getIps().isEmpty())
                                return bean.getIps().get(0);
                            else return null;
                        else {
                            updateIpByHostAsync(hostname);
                            return null;
                        }
                    } else {
                        updateIpByHostAsync(hostname);
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public void updateIpByHostAsync(String hostName) {
        Request request = new Request.Builder()
                .url("http://" + HttpDnsServerIp + "/" + account_id + "d/?host" + hostName)
                .build();
        retryTimes = 0;
        requestCache(request);
    }
}

package com.example.designmode.retrofit.source;


import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.ResponseBody;

public class ServiceMethod {

    final Retrofit retrofit;
    final Method method;
    String relativeUrl;
    String httpMethod;
    ParameterHandler<?>[] parameterHandler;

    public ServiceMethod(Builder builder) {
        this.retrofit = builder.retrofit;
        this.method = builder.method;
        this.relativeUrl = builder.relativeUrl;
        this.httpMethod = builder.httpMethod;
        this.parameterHandler = builder.parameterHandler;
    }



    public Call createNewCall(Object[] args) {
        RequestBulider requestBulider = new RequestBulider(retrofit.baseUrl, relativeUrl,
                parameterHandler, args);
        return retrofit.factory.newCall(requestBulider.bulid());
    }

    public <T> T parseBody(ResponseBody responseBody) {
        Type returnType = method.getGenericReturnType();
        Class<T> dataClass = (Class<T>) ((ParameterizedType)returnType).getActualTypeArguments()[0];
        Gson gson = new Gson();
        T body = gson.fromJson(responseBody.charStream(), dataClass);
        return body;
    }



    static class Builder {
        final Retrofit retrofit;
        final Method method;
        String relativeUrl;
        String httpMethod;
        Annotation[] methodAnnotations;
        Annotation[][] parameterAnnotations;
        ParameterHandler<?>[] parameterHandler;


        public Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;
            this.method = method;
            methodAnnotations = method.getAnnotations();
            parameterAnnotations = method.getParameterAnnotations();
            parameterHandler = new ParameterHandler[parameterAnnotations.length];
        }

        public ServiceMethod build() {
            for (Annotation methodAnnotation : methodAnnotations) {
                //解析url get post方法
                praseAnnotationMethod(methodAnnotation);
            }
            int size = parameterAnnotations.length;
            for (int i = 0; i < size; i++) {
                Annotation parameter = parameterAnnotations[i][0];
                if (parameter instanceof Querry) {
                    parameterHandler[i] = new ParameterHandler.Querry<>(((Querry) parameter).value());
                }
            }
            return new ServiceMethod(this);
        }

        private void praseAnnotationMethod(Annotation methodAnnotation) {
            if (methodAnnotation instanceof GET) {
                parseMethodAndPath("GET", ((GET) methodAnnotation).value());
            } else if (methodAnnotation instanceof POST) {
                parseMethodAndPath("POST", ((POST) methodAnnotation).value());
            }
        }

        private void parseMethodAndPath(String method, String value) {
            this.httpMethod = method;
            this.relativeUrl = value;
        }
    }
}

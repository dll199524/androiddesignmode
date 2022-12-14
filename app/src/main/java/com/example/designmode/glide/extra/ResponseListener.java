package com.example.designmode.glide.extra;

import com.example.designmode.glide.resource.Value;

public interface ResponseListener {
    void onSuccess(Value value);
    void onFailure(Exception e);
}

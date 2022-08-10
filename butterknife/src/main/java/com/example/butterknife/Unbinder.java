package com.example.butterknife;

import androidx.annotation.UiThread;

public interface Unbinder {
    @UiThread
    void unbind();

    Unbinder EMPTY = () -> {};
}


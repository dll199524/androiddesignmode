package com.example.designmode.performance;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class LazyFragment extends Fragment {

    private boolean isInit = false;
    private boolean isLoad = false;
    private static String TAG = "LazyFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutRes(), container, false);
        isInit = true;
        isLoadDate();
        return rootView;
    }

    private void isLoadDate() {
        if (!isInit) return;
        if (getUserVisibleHint()) {
            LazyLoad();
            isLoad = true;
        } else
            if (isLoad)
                stopLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isLoad = true;
    }

    public abstract int getLayoutRes();
    protected abstract void LazyLoad();
    protected void stopLoad() {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;
    }

    protected void showToast(String str) {
        if (!TextUtils.isEmpty(str))
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }
}

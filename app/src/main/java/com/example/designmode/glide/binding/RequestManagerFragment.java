package com.example.designmode.glide.binding;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;

import com.example.designmode.glide.core.RequestManager;

//andorid app fragment
public class RequestManagerFragment extends Fragment {

    private ActivityFragmentLifeCycle lifeCycle;
    private RequestManager requestManager;
    public RequestManagerFragment() {this(new ActivityFragmentLifeCycle());}

    @SuppressLint("ValidFragment")
    public RequestManagerFragment(ActivityFragmentLifeCycle lifeCycle) {this.lifeCycle = lifeCycle;}


    @Override
    public void onAttach(Context context) {super.onAttach(context);}

    @Override
    public void onStart() {
        super.onStart();
        lifeCycle.onStrat();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifeCycle.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifeCycle.onStop();
    }

    public RequestManager getRequestManager() {return requestManager;}

    public void setRequestManager(RequestManager requestManager) {this.requestManager = requestManager;}

    public LifeCycle getGlideLifeCycle() {return lifeCycle;}
}

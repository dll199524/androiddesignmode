package com.example.designmode.glide.binding;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.designmode.glide.core.RequestManager;

//andorid x 空白fragment
public class SupportRequestManagerFragment extends Fragment {

    private ActivityFragmentLifeCycle lifeCycle;
    private RequestManager requestManager;

    public SupportRequestManagerFragment() {this(new ActivityFragmentLifeCycle());}

    public SupportRequestManagerFragment(ActivityFragmentLifeCycle lifeCycle) {this.lifeCycle = lifeCycle;}

    @Override
    public void onAttach(@NonNull Context context) {super.onAttach(context);}

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

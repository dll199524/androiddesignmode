package com.example.designmode.mod.director;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HeaderViewRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final RecyclerView.Adapter realAdapter;
    ArrayList<View> mHeaderView;
    ArrayList<View> mFooterView;
    public HeaderViewRecycleAdapter(RecyclerView.Adapter realAdapter) {
        this.realAdapter = realAdapter;
        mHeaderView = new ArrayList<>();
        mFooterView = new ArrayList<>();
        realAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        // Header (negative positions will throw an IndexOutOfBoundsException)
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return createHeadFootViewHolder(mHeaderView.get(position));
        }

        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (realAdapter != null) {
            adapterCount = realAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                return realAdapter.createViewHolder(parent, realAdapter.getItemViewType(adjPosition));
            }
        }

        // Footer (off-limits positions will throw an IndexOutOfBoundsException)
        return createHeadFootViewHolder(mFooterView.get(adjPosition - adapterCount));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) return;

        // Adapter
        final int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (realAdapter != null) {
            adapterCount = realAdapter.getItemCount();
            if (adjPosition < adapterCount) {
                realAdapter.onBindViewHolder(holder, position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mHeaderView.size() + mFooterView.size() + realAdapter.getItemCount();
    }

    public void addHeadView(View view) {
        if (!mHeaderView.contains(view)) {
            mHeaderView.add(view);
            notifyDataSetChanged();
        }
    }

    public void removeHeadView(View view) {
        if (mHeaderView.contains(view)) {
            mHeaderView.remove(view);
            notifyDataSetChanged();
        }
    }

    public void addFootView(View view) {
        if (!mFooterView.contains(view)) {
            mFooterView.add(view);
            notifyDataSetChanged();
        }
    }

    public void removeFootView(View view) {
        if (!mFooterView.contains(view)) {
            mFooterView.add(view);
            notifyDataSetChanged();
        }
    }

    public int getHeadersCount() {
        return mHeaderView.size();
    }

    public int getFootersCount() {
        return mFooterView.size();
    }

    public RecyclerView.ViewHolder createHeadFootViewHolder(View view) {
        return new RecyclerView.ViewHolder(view) {};
    }


}

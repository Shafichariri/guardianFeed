package com.shelvz.assignment.kit.databinding;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by shafic on 1/24/17.
 */

public class DataBoundViewHolder<T, B extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private final B mBinder;
    private T mItem;

    public DataBoundViewHolder(ViewGroup parent) {
        super(createLayout(parent));
        mBinder = onCreateViewDataBinding(parent);

        final FrameLayout frameLayout = (FrameLayout) itemView;
        final View root = mBinder.getRoot();

        frameLayout.addView(root);
    }

    protected B onCreateViewDataBinding(ViewGroup parent) {
        return null;
    }

    protected void onViewDataBindingCreated(B binder) {

    }

    protected void bind(T item) {
        this.mItem = item;
        onBind(item);
    }

    protected void onBind(T item) {

    }

    protected B getViewDataBinding() {
        return mBinder;
    }

    protected T getItem() {
        return mItem;
    }

    private static FrameLayout createLayout(ViewGroup parent) {
        final FrameLayout frameLayout = new FrameLayout(parent.getContext());
        parent.addView(frameLayout, parent.getLayoutParams());
        return frameLayout;
    }
}

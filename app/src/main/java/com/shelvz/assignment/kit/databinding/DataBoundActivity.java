package com.shelvz.assignment.kit.databinding;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shelvz.assignment.kit.base.BaseActivity;

/**
 * Created by shafic on 1/5/17.
 */

public abstract class DataBoundActivity<B extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivity {
    private B mViewDataBinding;
    private VM mViewModel;

    public abstract Class<VM> classForViewModel();

    public abstract int layoutForDataBinding();

    protected VM createViewModel() {
        return ViewModelProviders.of(this).get(classForViewModel());
    }

    protected B onCreateViewDataBinding(@Nullable Bundle savedInstanceState) {
        return DataBindingUtil.setContentView(this, layoutForDataBinding());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = createViewModel();
        mViewDataBinding = onCreateViewDataBinding(savedInstanceState);
        onCreated(mViewDataBinding, mViewModel, savedInstanceState);
    }

    protected void onCreated(@NonNull B viewDataBinding, @NonNull VM viewModel, @Nullable Bundle savedInstanceState) {
    }

    protected B getViewDataBinding() {
        return mViewDataBinding;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mViewModel == null) {
            throw new NullPointerException("ViewModel was never initialized before onStart() was called.");
        }
    }

    protected VM getViewModel() {
        return mViewModel;
    }
}


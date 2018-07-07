package com.shelvz.assignment.kit.databinding;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shelvz.assignment.kit.base.BaseFragment;

/**
 * Created by Shafic on 1/5/17.
 */

public abstract class DataBoundFragment<B extends ViewDataBinding, VM extends BaseViewModel, L extends DataBoundFragment
        .Listener> extends BaseFragment {
    private L mFragmentListener;
    private B mBinder;
    private VM mViewModel;

    public abstract Class<VM> classForViewModel();

    public abstract int layoutForDataBinding();

    protected VM createViewModel() {
        return ViewModelProviders.of(this).get(classForViewModel());
    }

    protected B onCreateViewDataBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return DataBindingUtil.inflate(inflater, layoutForDataBinding(), container, false);
    }

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewModel = createViewModel();
        mBinder = onCreateViewDataBinding(inflater, container, savedInstanceState);
        return mBinder.getRoot();
    }

    @Override
    public final void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onCreated(mBinder, mViewModel, savedInstanceState);
    }

    protected void onCreated(@NonNull B viewDataBinding, @NonNull VM viewModel, @Nullable Bundle savedInstanceState) {
    }

    protected B getViewDataBinding() {
        return mBinder;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mViewModel == null) {
            throw new NullPointerException("ViewModel was never initialized before onStart() was called.");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mFragmentListener = (L) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentListener = null;
    }


    protected L getFragmentListener() {
        return mFragmentListener;
    }

    protected VM getViewModel() {
        return mViewModel;
    }

    public interface Listener {

    }
}

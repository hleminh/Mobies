package com.example.hoang.mobies.searchs;

import android.support.v7.widget.SearchView;

import com.jakewharton.rxbinding2.*;

import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

/**
 * Created by Hoang on 7/9/2017.
 */

public class MySearchViewQueryTextChangesObservable extends InitialValueObservable<CharSequence> {
    private final SearchView view;

    public MySearchViewQueryTextChangesObservable(SearchView view) {
        this.view = view;
    }

    @Override
    protected void subscribeListener(Observer<? super CharSequence> observer) {
        if (!checkMainThread(observer)) {
            return;
        }
        MyListener listener = new MyListener(view, observer);
        observer.onSubscribe(listener);
        view.setOnQueryTextListener(listener);
    }

    @Override
    protected CharSequence getInitialValue() {
        return view.getQuery();
    }

    class MyListener extends MainThreadDisposable implements SearchView.OnQueryTextListener {
        private final SearchView searchView;
        private final Observer<? super CharSequence> observer;

        public MyListener(SearchView searchView, Observer<? super CharSequence> observer) {
            this.searchView = searchView;
            this.observer = observer;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            searchView.clearFocus();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (!isDisposed()) {
                observer.onNext(newText);
                return true;
            }
            return false;
        }

        @Override
        protected void onDispose() {
            searchView.setOnQueryTextListener(null);
        }
    }
}

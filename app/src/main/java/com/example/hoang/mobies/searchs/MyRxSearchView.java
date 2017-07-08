package com.example.hoang.mobies.searchs;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;

import com.jakewharton.rxbinding2.InitialValueObservable;

import io.reactivex.functions.Consumer;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkNotNull;

/**
 * Created by Hoang on 7/9/2017.
 */

public class MyRxSearchView {

    /**
     * Create an observable of character sequences for query text changes on {@code view}.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     * <p>
     * <em>Note:</em> A value will be emitted immediately on subscribe.
     */
    @CheckResult @NonNull
    public static InitialValueObservable<CharSequence> queryTextChanges(@NonNull SearchView view) {
        checkNotNull(view, "view == null");
        return new MySearchViewQueryTextChangesObservable(view);
    }

    /**
     * An action which sets the query property of {@code view} with character sequences.
     * <p>
     * <em>Warning:</em> The created observable keeps a strong reference to {@code view}. Unsubscribe
     * to free this reference.
     *
     * @param submit weather to submit query right after updating query text
     */
    @CheckResult @NonNull
    public static Consumer<? super CharSequence> query(@NonNull final SearchView view,
                                                       final boolean submit) {
        checkNotNull(view, "view == null");
        return new Consumer<CharSequence>() {
            @Override public void accept(CharSequence text) {
                view.setQuery(text, submit);
            }
        };
    }

    private MyRxSearchView() {
        throw new AssertionError("No instances.");
    }
}

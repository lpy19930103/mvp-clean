package com.lpy.domin.interactor;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any use case
 * in the application should implement this contract).
 * <p>
 * By convention each UseCase implementation will return the result using a {@link DisposableSubscriber}
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
public abstract class UseCase<T, Params> {
    
    private final CompositeDisposable mDisposables;
    
    public UseCase() {
        this.mDisposables = new CompositeDisposable();
    }
    
    /**
     * Builds an {@link Flowable} which will be used when executing the current {@link UseCase}.
     */
    public abstract Flowable<T> buildUseCaseObservable(Params params);
    
    /**
     * Executes the current use case.
     *
     * @param subscriber {@link DisposableSubscriber} which will be listening to the observable build
     *                   by {@link #buildUseCaseObservable(Params)} ()} method.
     * @param params     Parameters (Optional) used to build/execute this use case.
     */
    public void execute(Params params, DisposableSubscriber<T> subscriber) {
        checkNotNull(subscriber);
        Flowable<T> flowable = this.buildUseCaseObservable(params);
        addDisposable(flowable.subscribeWith(subscriber));
    }
    
    public <P> void execute(Flowable<P> flowable, DisposableSubscriber<P> subscriber) {
        checkNotNull(flowable);
        checkNotNull(subscriber);
        addDisposable(flowable.subscribeWith(subscriber));
    }
    
    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    public void dispose() {
        if (!mDisposables.isDisposed()) {
            mDisposables.dispose();
        }
    }
    
    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    private void addDisposable(Disposable disposable) {
        checkNotNull(disposable);
        checkNotNull(mDisposables);
        mDisposables.add(disposable);
    }
    
    private <D> D checkNotNull(D reference) {
        if (reference == null) {
            throw new NullPointerException();
        } else {
            return reference;
        }
    }
    
}

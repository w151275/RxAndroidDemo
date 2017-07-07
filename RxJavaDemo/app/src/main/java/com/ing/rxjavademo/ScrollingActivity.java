package com.ing.rxjavademo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ScrollingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                map();
                flatmap();
            }
        });
    }

    private void map(){
        Observable <String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
                Log.i("observable==",""+Thread.currentThread().getName());
            }
        });
        observable.subscribeOn(Schedulers.newThread())
                .map(new Function<String, Integer>() {
                    @Override
                    public Integer apply(@NonNull String s) throws Exception {
                        int a = 0;
                        if("1".equals(s)){
                            a= 10000;
                        }else if("2".equals(s)){
                            a=10001;
                        }else if("3".equals(s)){
                            a=10003;
                        }
                        return a;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer o) throws Exception {
                        Log.i("",""+o);
                        Log.i("observer==",""+Thread.currentThread().getName());
                    }
                });
    }

    private void flatmap(){
        Observable <String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("1");
                e.onNext("2");
                e.onNext("3");
                Log.i("observable==",""+Thread.currentThread().getName());
            }
        });
        observable.subscribeOn(Schedulers.newThread())
                .flatMap(new Function<String, ObservableSource<Integer>>() {
                    @Override
                    public ObservableSource<Integer> apply(@NonNull String s) throws Exception {

                        return Observable.create(new ObservableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                                e.onNext(1234);
                            }
                        });
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer o) throws Exception {
                        Log.i("",""+o);
                        Log.i("observer==",""+Thread.currentThread().getName());
                    }
                });
    }


    private void flowObserver(){
        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext("11");
            }
        }, BackpressureStrategy.ERROR)
                ;
    }
}

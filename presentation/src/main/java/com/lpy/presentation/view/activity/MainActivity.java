package com.lpy.presentation.view.activity;

import android.os.Bundle;

import com.example.presentation.R;
import com.lpy.domin.interactor.login.LoginUserCase;
import com.lpy.domin.modules.BasicResponse;
import com.lpy.presentation.di.component.DaggerActivityComponent;
import com.lpy.presentation.di.module.ActivityModule;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.subscribers.DisposableSubscriber;

public class MainActivity extends AppCompatActivity {
    
    @Inject
    LoginUserCase loginUserCase;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DaggerActivityComponent.builder().activityModule(new ActivityModule(this)).build().inject(this);
        LoginUserCase.Params params = new LoginUserCase.Params();
        params.setUserName("18519121233");
        params.setUserName("1851912121133");
        loginUserCase.execute(params, new DisposableSubscriber<BasicResponse>() {
            @Override
            public void onNext(BasicResponse basicResponse) {
                System.out.print(basicResponse.data.toString());
            
            }
        
            @Override
            public void onError(Throwable t) {
                System.out.print(t.getMessage());
            
            }
        
            @Override
            public void onComplete() {
            
            }
        });
    }
}

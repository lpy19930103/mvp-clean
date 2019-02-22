package com.example.data;


import com.ltdd.domin.interactor.login.LoginUserCase;
import com.ltdd.domin.modules.BasicResponse;

import org.junit.Test;

import javax.inject.Inject;

import io.reactivex.subscribers.DisposableSubscriber;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Inject
    LoginUserCase loginUserCase;
    
    @Test
    public void addition_isCorrect() {
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
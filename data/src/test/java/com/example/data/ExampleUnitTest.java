package com.example.data;

import com.lpy.domin.modules.BasicResponse;
import comlpy.data.api.HttpHelper;
import comlpy.data.repository.LoginDataRepository;
import com.lpy.domin.repository.LoginRepository;

import org.junit.Test;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Inject
    LoginRepository loginRepository;
    
    @Test
    public void addition_isCorrect() {
        new LoginDataRepository(new HttpHelper()).login("18519121233", "aaaa1234")
                .subscribe(new Consumer<BasicResponse>() {
                    @Override
                    public void accept(BasicResponse basicResponse) throws Exception {
                        System.out.print(basicResponse.toString());
        
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        System.out.print(throwable.getMessage());
        
                    }
                });
        
    }
}
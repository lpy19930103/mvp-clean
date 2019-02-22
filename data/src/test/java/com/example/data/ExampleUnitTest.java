package com.example.data;

import com.ltdd.domin.modules.BasicResponse;
import com.ltdd.data.api.HttpHelper;
import com.ltdd.data.repository.LoginDataRepository;
import com.ltdd.domin.repository.LoginRepository;

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
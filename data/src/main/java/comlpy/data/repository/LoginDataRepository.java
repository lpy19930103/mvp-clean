package comlpy.data.repository;

import com.lpy.domin.entity.BasicResponse;

import comlpy.data.api.HttpHelper;
import comlpy.data.service.RestService;

import com.lpy.domin.entity.UserInfo;
import com.lpy.domin.repository.LoginRepository;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginDataRepository implements LoginRepository {
    
    private RestService restService;
    
    @Inject
    public LoginDataRepository(HttpHelper httpHelper) {
        restService = httpHelper.getRestApi();
    }
    
    
    @Override
    public Flowable<BasicResponse<UserInfo>> login(String memberAccount, String memberPwd) {
        return restService.login(memberAccount, memberPwd, "12", "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}

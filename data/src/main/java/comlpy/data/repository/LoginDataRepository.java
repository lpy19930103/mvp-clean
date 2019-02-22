package comlpy.data.repository;

import com.lpy.domin.modules.BasicResponse;
import comlpy.data.api.HttpHelper;
import comlpy.data.service.RestService;
import com.lpy.domin.repository.LoginRepository;

import javax.inject.Inject;

import io.reactivex.Flowable;

public class LoginDataRepository implements LoginRepository {
    
    private RestService restService;
    
    @Inject
    public LoginDataRepository(HttpHelper httpHelper) {
        restService = httpHelper.getRestApi();
    }
    
    
    @Override
    public Flowable<BasicResponse> login(String memberAccount, String memberPwd) {
        return restService.login(memberAccount, memberPwd, "12", "1");
    }
}

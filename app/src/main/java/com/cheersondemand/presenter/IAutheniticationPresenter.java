package com.cheersondemand.presenter;

import com.cheersondemand.model.AuthenticationResponse;
import com.cheersondemand.model.LoginRequest;
import com.cheersondemand.model.SignUpRequest;
import com.cheersondemand.model.SocialLoginRequest;

/**
 * Created by GAURAV on 5/30/2018.
 */

public interface IAutheniticationPresenter {
   /* interface IAuthenticationView extends IActivityView {
        void signUp(SignUpRequest signUpRequest);
        void signUpName(String signUpRequest);

        String getName();
        String getPassword();
    }
    interface IAuthenticationPresenter extends IPresenter<IAuthenticationView> {
    }*/

    public void setSignUp(SignUpRequest signUpRequest);
    public void setLoginUsingEmail(LoginRequest loginRequest);

    public void setSignUpSocail(SocialLoginRequest signUpSocail);

    void onDestroy();

    interface IAuthenticationView {
        public void getResponseSuccess(AuthenticationResponse response);
        public void getResponseError(String response);
        void showProgress();

        void hideProgress();
    }

}

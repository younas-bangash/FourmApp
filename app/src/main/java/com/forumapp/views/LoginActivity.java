package com.forumapp.views;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.forumapp.R;
import com.forumapp.databinding.ActivityLoginBinding;
import com.forumapp.viewModels.LoginViewModel;

import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding activityLoginBinding = null;
    LoginViewModel loginViewModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();

    }

    private void initDataBinding() {
        activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        loginViewModel = new LoginViewModel(this);
        activityLoginBinding.setLoginViewModel(loginViewModel);
    }

//    @Override
//    public void update(Observable observable, Object o) {
//        if (observable instanceof LoginViewModel) {
//
//        }
//    }
}

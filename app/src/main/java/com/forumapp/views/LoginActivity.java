package com.forumapp.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.forumapp.R;
import com.forumapp.databinding.ActivityLoginBinding;
import com.forumapp.viewModels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding activityLoginBinding = null;
    LoginViewModel loginViewModel = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new LoginViewModel(this);
        initDataBinding();

    }

    private void initDataBinding() {
        activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        activityLoginBinding.setLoginViewModel(loginViewModel);
    }


}

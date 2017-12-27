package com.forumapp.views;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.forumapp.R;
import com.forumapp.databinding.ActivitySignUpBinding;
import com.forumapp.viewModels.LoginViewModel;
import com.forumapp.viewModels.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding activitySignUpBinding = null;
    SignUpViewModel signUpViewModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding();
    }

    private void initDataBinding() {
        activitySignUpBinding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        signUpViewModel = new SignUpViewModel(this);
        activitySignUpBinding.setSignUpViewModel(signUpViewModel);
    }
}

package com.forumapp.viewModels;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.forumapp.BR;
import com.forumapp.R;
import com.forumapp.models.LoginModel;
import com.forumapp.utils.NetworkUtil;
import com.forumapp.views.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Observable;

/**
 * Created by YounasBangash on 12/22/2017.
 */

public class LoginViewModel extends BaseObservable {
    private Context context = null;
    private LoginModel loginModel = null;
    private FirebaseAuth.AuthStateListener mAuthListener = null;
    private FirebaseAuth firebaseAuth = null;
    public String password = null;
    public String email = null;


    public LoginViewModel(Context context) {
        this.context = context;
        this.loginModel = new LoginModel();
        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {

                }
            }
        };
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    public void onRegisterClick(){
        context.startActivity(new Intent(context, SignUpActivity.class));
    }

    public void onLoginButtonClick() {
        if (isValidEmail(email)) {
            if (validatePasswrod()) {
                if (NetworkUtil.getConnectivityStatus(context) != 0) {
                    login();
                } else {
                    Toast.makeText(context, context.getString(R.string.internet_connection_req),
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(context, context.getString(R.string.invalid_email),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void login() {
        firebaseAuth.signInWithEmailAndPassword(getEmail(), getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete() && task.isSuccessful()) {

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

//    public void setPassword(String password) {
//        this.password = password;
//        notifyPropertyChanged(BR.password);
//    }


    @Bindable
    private String getPassword() {
        return password;
    }

    @Bindable
    private String getEmail() {
        return email;
    }

    private static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean validatePasswrod() {
        if (password == null || password.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.empty_password),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(context, context.getString(R.string.short_password),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}

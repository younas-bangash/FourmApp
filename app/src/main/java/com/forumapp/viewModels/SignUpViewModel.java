package com.forumapp.viewModels;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import com.forumapp.R;
import com.forumapp.models.LoginModel;
import com.forumapp.models.SingUpModel;
import com.forumapp.utils.NetworkUtil;
import com.forumapp.views.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Observable;

/**
 * Created by YounasBangash on 12/22/2017.
 */

public class SignUpViewModel extends BaseObservable {
    private DatabaseReference databaseReferenceUsers = null;
    private FirebaseAuth firebaseAuth = null;
    public String password = null;
    public String email = null;
    public String userName = null;
    public String confPassword = null;
    private Context context = null;
    private SingUpModel signupModel = null;

    public SignUpViewModel(Context context) {
        this.context = context;
        this.signupModel = new SingUpModel();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String token = FirebaseInstanceId.getInstance().getToken();
                    signupModel.setUserEmail(getEmail());
                    signupModel.setUserName(getUserName());
                    signupModel.setUserTokenID(token);
                    signupModel.setUserID(firebaseUser.getUid());
                    storeUserInformation();
                }
            }
        };
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    public void onAlreadyAccountClick(){
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public void onSignUpButtonClick() {
        if (isValidEmail(email)) {
            if (validatePasswrod()) {
                if (userName != null && !TextUtils.isEmpty(userName)) {
                    if (confPassword != null && !TextUtils.isEmpty(confPassword)) {
                        if (getConfPassword().equals(getPassword())) {
                            if (NetworkUtil.getConnectivityStatus(context) != 0) {
                                register();
                            } else {
                                Toast.makeText(context, context.getString(R.string.internet_connection_req),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, context.getString(R.string.pass_mismatch),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, context.getString(R.string.conf_password_req),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.invalid_username),
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(context, context.getString(R.string.invalid_email),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void register() {
        firebaseAuth.createUserWithEmailAndPassword(getEmail(), getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeUserInformation() {
        //noinspection ConstantConditions
        databaseReferenceUsers.child(firebaseAuth.getUid()).setValue(signupModel.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isComplete() && task.isComplete()){
                            Toast.makeText(context, context.getString(R.string.register_success),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Bindable
    private String getPassword() {
        return password;
    }

    @Bindable
    private String getEmail() {
        return email;
    }

    @Bindable
    private String getUserName() {
        return userName;
    }

    @Bindable
    private String getConfPassword() {
        return confPassword;
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

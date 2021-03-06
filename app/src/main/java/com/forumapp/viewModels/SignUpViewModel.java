package com.forumapp.viewModels;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.forumapp.R;
import com.forumapp.models.SingUpModel;
import com.forumapp.utils.NetworkUtil;
import com.forumapp.utils.PrefManager;
import com.forumapp.views.LoginActivity;
import com.forumapp.views.TopicsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by YounasBangash on 12/22/2017.
 */

public class SignUpViewModel extends BaseObservable {
    PrefManager prefManager = null;
    private DatabaseReference databaseReferenceUsers = null;
    private FirebaseAuth firebaseAuth = null;
    public String password = null;
    public String email = null;
    private Dialog dialog = null;
    public String userName = null;
    public String confPassword = null;
    private SingUpModel signupModel = null;
    private Activity callingActivity = null;

    public SignUpViewModel(Activity callingActivity) {
        this.prefManager = new PrefManager(callingActivity);
        this.callingActivity = callingActivity;
        this.signupModel = new SingUpModel();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceUsers = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseAuth.AuthStateListener mAuthListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                String token = FirebaseInstanceId.getInstance().getToken();
                signupModel.setUserEmail(getEmail());
                signupModel.setUserName(getUserName());
                signupModel.setUserTokenID(token);
                signupModel.setUserID(firebaseUser.getUid());
                storeUserInformation();
            }
        };
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    public void onAlreadyAccountClick(){
        callingActivity.startActivity(new Intent(callingActivity, LoginActivity.class));
        callingActivity.finish();
    }

    public void onSignUpButtonClick() {
        if (isValidEmail(email)) {
            if (validatePasswrod()) {
                if (userName != null && !TextUtils.isEmpty(userName)) {
                    if (confPassword != null && !TextUtils.isEmpty(confPassword)) {
                        if (getConfPassword().equals(getPassword())) {
                            if (NetworkUtil.getConnectivityStatus(callingActivity) != 0) {
                                hideSoftKeyboard();
                                showAnimatedProgressDialog().show();
                                register();
                            } else {
                                Toast.makeText(callingActivity, callingActivity.getString(R.string.internet_connection_req),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(callingActivity, callingActivity.getString(R.string.pass_mismatch),
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(callingActivity, callingActivity.getString(R.string.conf_password_req),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(callingActivity, callingActivity.getString(R.string.invalid_username),
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(callingActivity, callingActivity.getString(R.string.invalid_email),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private Dialog showAnimatedProgressDialog() {
        if (dialog == null) {
            dialog = new Dialog(callingActivity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.progress_window);
            //noinspection ConstantConditions
            dialog.getWindow().setBackgroundDrawableResource(R.color.dialog_window_color);

        }
        dialog.setOnDismissListener(dialog -> {
        });
        dialog.setOnShowListener(dialog -> {
        });
        return dialog;
    }

    private  void hideSoftKeyboard() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) callingActivity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            //noinspection ConstantConditions
            inputMethodManager.hideSoftInputFromWindow(
                    callingActivity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void register() {
        firebaseAuth.createUserWithEmailAndPassword(getEmail(), getPassword())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.isComplete()){

                    }
                }).addOnFailureListener(e -> {
                    showAnimatedProgressDialog().dismiss();
                    e.printStackTrace();
                    Toast.makeText(callingActivity, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void storeUserInformation() {
        //noinspection ConstantConditions
        databaseReferenceUsers.child(firebaseAuth.getUid()).setValue(signupModel.toMap())
                .addOnCompleteListener(task -> {
                    if(task.isComplete() && task.isComplete()){
                        prefManager.setName(getUserName());
                        prefManager.setEmail(getEmail());
                        showAnimatedProgressDialog().dismiss();
                        callingActivity.startActivity(new Intent(callingActivity, TopicsActivity.class));
                        Toast.makeText(callingActivity, callingActivity.getString(R.string.register_success),
                                Toast.LENGTH_SHORT).show();

                        callingActivity.finish();
                    }
                }).addOnFailureListener(e -> {

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
            Toast.makeText(callingActivity, callingActivity.getString(R.string.empty_password),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(callingActivity, callingActivity.getString(R.string.short_password),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}

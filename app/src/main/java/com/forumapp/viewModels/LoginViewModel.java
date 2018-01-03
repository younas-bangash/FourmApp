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
import com.forumapp.utils.NetworkUtil;
import com.forumapp.views.SignUpActivity;
import com.forumapp.views.TopicsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by YounasBangash on 12/22/2017.
 */

public class LoginViewModel extends BaseObservable {
    private Activity callingActivity = null;
    //    private LoginModel loginModel = null;
    private FirebaseAuth.AuthStateListener mAuthListener = null;
    private FirebaseAuth firebaseAuth = null;
    public String password = null;
    public String email = null;
    private Dialog dialog = null;


    public LoginViewModel(Activity callingActivity) {
        this.callingActivity = callingActivity;
//        this.loginModel = new LoginModel();
        firebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {

            }
        };
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    public void onRegisterClick() {
        callingActivity.startActivity(new Intent(callingActivity, SignUpActivity.class));
    }

    public void onLoginButtonClick() {
        if (isValidEmail(email)) {
            if (validatePasswrod()) {
                if (NetworkUtil.getConnectivityStatus(callingActivity) != 0) {
                    hideSoftKeyboard();
                    showAnimatedProgressDialog().show();
                    login();
                } else {
                    Toast.makeText(callingActivity,
                            callingActivity.getString(R.string.internet_connection_req),
                            Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(callingActivity, callingActivity.getString(R.string.invalid_email),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void login() {
        firebaseAuth.signInWithEmailAndPassword(getEmail(), getPassword())
                .addOnCompleteListener(task -> {
                    if (task.isComplete() && task.isSuccessful()) {
                        showAnimatedProgressDialog().dismiss();
                        callingActivity.startActivity(new Intent(callingActivity, TopicsActivity.class));
                        callingActivity.finish();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(callingActivity, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    showAnimatedProgressDialog().dismiss();
        });
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

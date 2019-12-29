package com.digiads.akshhomeautomation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

public class LoginActivity extends AppCompatActivity {
    String TAG = "RegLoginActivity";
    private AutoCompleteTextView userId, phoneET, dobET, emailET, _name;
    private ShowHidePasswordEditText password, confirm_password;
    private Button signin;
    private AwesomeValidation awesomeValidation;
    private RelativeLayout progressBAr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        //postRequest();
    }

    public void showLoingAlyout(View view) {
        view.setVisibility(View.GONE);
        findViewById(R.id.loginLayout).setVisibility(View.VISIBLE);
        initLogin();
    }

    private void initLogin() {
        userId = findViewById(R.id.userid);
        password = findViewById(R.id.password);
        signin = findViewById(R.id.signIn);
        progressBAr = findViewById(R.id.progressBar);
    }


    public void showSignuplyout(View view) {
        setContentView(R.layout.reg_layout);
//         findViewById(R.id.loginLayout).setVisibility(View.VISIBLE);
        initSignUp();
    }

    private void initSignUp() {
        emailET = findViewById(R.id.email);
        _name = findViewById(R.id.name);
        phoneET = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        signin = findViewById(R.id.signIn);
        progressBAr = findViewById(R.id.progressBar);
    }

    public void signUp(View view) {

    }

    public void addSigninLayouyt(View view) {
        setContentView(R.layout.activity_login);
    }

    public void signIn(View view) {
    }

    public void googleLogin(View view) {
    }

    public void facebookLogin(View view) {
    }
}


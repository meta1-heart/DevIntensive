package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesign.devintensive.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity implements View.OnClickListener {


    TextView mForgot;
    Button mLoginButton;
    @BindView(R.id.password_et) EditText mPassword;
    @BindView(R.id.email_login_et) EditText mEmailLogin;



    /**
     * вызыватся при создании активити
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mForgot=(TextView) findViewById(R.id.forgot_tv);
        mLoginButton =(Button) findViewById(R.id.login_btn);

        ButterKnife.bind(this);

        mLoginButton.setOnClickListener(this);
        mForgot.setOnClickListener(this);

    }

    /**
     * обработчик нажатий
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                Intent loginIntent = new Intent(this, MainActivity.class);
                startActivity(loginIntent);

                break;
            case R.id.forgot_tv:
                showToast(":(");
                break;
        }

    }
}
package com.example.easychef.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.easychef.R;
import com.example.easychef.databinding.ActivityLoginBinding;
import com.example.easychef.utils.LogoGifRequestListener;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseUser.getCurrentUser() != null) {
            goToMainActivity();
        }

        final ActivityLoginBinding loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        etUsername = loginBinding.etUsername;
        etPassword = loginBinding.etPassword;

        Glide.with(this)
                .asGif()
                .load(R.drawable.logo_gif)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new LogoGifRequestListener())
                .into(loginBinding.ivLogo);

        loginBinding.btnLogin.setOnClickListener(new LoginButtonViewOnClickListener());
        loginBinding.btnRegister.setOnClickListener(new RegisterButtonViewOnClickListener());
    }

    private void loginUser(String username, String password) {
        Log.i(TAG, "Attempted to login user " + username);
        ParseUser.logInInBackground(username, password, new EasyChefLogInCallback());
    }

    private void registerUser(String username, String password) {
        Log.i(TAG, "Attempting to register user " + username);

        final ParseUser newUser = new ParseUser();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.signUpInBackground(new EasyChefSignUpCallback());
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private class LoginButtonViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick login button");
            loginUser(etUsername.getText().toString(), etPassword.getText().toString());
        }
    }

    private class RegisterButtonViewOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick register button");
            registerUser(etUsername.getText().toString(), etPassword.getText().toString());
        }
    }

    private class EasyChefLogInCallback implements LogInCallback {

        @Override
        public void done(ParseUser user, ParseException e) {
            if (e != null) {
                Log.e(TAG, "Issue with login", e);
                Toast.makeText(LoginActivity.this, "Issue with login", Toast.LENGTH_SHORT).show();
                return;
            }
            goToMainActivity();
        }
    }

    private class EasyChefSignUpCallback implements SignUpCallback {

        @Override
        public void done(ParseException e) {
            if (e != null) {
                Log.e(TAG, "Issue with registration", e);
                Toast.makeText(LoginActivity.this, "Issue with registration", Toast.LENGTH_SHORT).show();
                return;
            }
            goToMainActivity();
        }
    }
}
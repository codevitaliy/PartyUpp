package com.codevitaliy.partyupp.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.codevitaliy.partyupp.MainActivity;
import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.api.ApiClient;
import com.codevitaliy.partyupp.api.LoginRequest;
import com.codevitaliy.partyupp.api.LoginRequest.CheckLoginResult;
import com.codevitaliy.partyupp.api.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText editTextEmailLogin, editTextPasswordLogin;
    Button buttonContinueAsGuest;
    Button buttonSubmitLogin;
    TextView textViewRegisterAtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        buttonContinueAsGuest = findViewById(R.id.buttonContinueAsGuest);
        editTextEmailLogin = findViewById(R.id.editTextEmailLogin);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);
        textViewRegisterAtLogin = findViewById(R.id.textViewRegisterLogin);
        buttonSubmitLogin = findViewById(R.id.buttonSubmitLogin);

        buttonContinueAsGuest.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        buttonSubmitLogin.setOnClickListener(v -> {
            LoginRequest request = new LoginRequest(
                    editTextEmailLogin.getText().toString(),
                    editTextPasswordLogin.getText().toString()
            );
            CheckLoginResult check = request.check();
            if(check == CheckLoginResult.InvalidEmail) {
                showDialog(getString(R.string.reg_invalid_email_ttl), getString(R.string.reg_invalid_email_msg), true);
                return;
            } else if(check == CheckLoginResult.InvalidPassword){
                showDialog(getString(R.string.reg_invalid_password_ttl), getString(R.string.incorrect_password), true);
                return;
            }
            LoginActivity context = this;
            ApiClient apiClient = ApiClient.getInstance();
            apiClient.login(request, new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    LoginResponse.Result result = response.body().result;
                    if(result == LoginResponse.Result.Ok){
                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else if(result == LoginResponse.Result.WrongEmail){
                        showDialog(getString(R.string.reg_invalid_email_ttl), getString(R.string.reg_invalid_email_msg), true);
                    } else if(result == LoginResponse.Result.WrongPassword){
                        showDialog(getString(R.string.reg_invalid_password_ttl), getString(R.string.incorrect_password), true);
                    }
                }
                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    showDialog(getString(R.string.network_error_ttl), getString(R.string.network_error_msg), true);
                }
            });

        });

        textViewRegisterAtLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!(v instanceof TextInputEditText)) {
                    hideKeyboard(v);
                    clearFocusFromActivity(findViewById(android.R.id.content));
                }
                return false;
            }
        });

        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };

        editTextEmailLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(v);
                    v.clearFocus();
                    return true;
                }
                return false;
            }
        });

        editTextPasswordLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(v);
                    v.clearFocus();
                    return true;
                }
                return false;
            }
        });

        editTextEmailLogin.setOnFocusChangeListener(focusChangeListener);
        editTextPasswordLogin.setOnFocusChangeListener(focusChangeListener);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void clearFocusFromActivity(View view) {
        if (view instanceof EditText) {
            view.clearFocus();
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                clearFocusFromActivity(viewGroup.getChildAt(i));
            }
        }
    }

    private void showDialog(String title, String message, boolean isCancellable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_ok_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(isCancellable);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        TextView titleView = alertDialog.findViewById(getResources().getIdentifier("alertTitle", "id", "android"));
        if (titleView != null) {
            titleView.setTextColor(Color.WHITE);
        }

        TextView messageView = alertDialog.findViewById(android.R.id.message);
        if (messageView != null) {
            messageView.setTextColor(Color.WHITE);
        }

        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.blue));
        }
    }
}
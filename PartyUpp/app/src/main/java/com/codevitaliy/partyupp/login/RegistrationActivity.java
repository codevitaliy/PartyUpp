package com.codevitaliy.partyupp.login;

import androidx.annotation.NonNull;
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
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputEditText;
import com.codevitaliy.partyupp.MainActivity;
import com.codevitaliy.partyupp.R;
import com.codevitaliy.partyupp.api.ApiClient;
import com.codevitaliy.partyupp.api.RegistrationRequest;
import com.codevitaliy.partyupp.api.RegistrationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    TextInputEditText editTextUsernameReg, editTextEmailReg, editTextPasswordReg, editTextRepeatPassword;
    Button buttonSubmitRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_registration);

        editTextUsernameReg = findViewById(R.id.editTextUsernameRegistration);
        editTextEmailReg = findViewById(R.id.editTextEmailRegistration);
        editTextPasswordReg = findViewById(R.id.editTextPasswordRegistration);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);

        buttonSubmitRegistration = findViewById(R.id.buttonSubmitRegistration);

        buttonSubmitRegistration.setOnClickListener(view -> {
            RegistrationRequest request = new RegistrationRequest(
                    editTextUsernameReg.getText().toString(),
                    editTextEmailReg.getText().toString(),
                    editTextPasswordReg.getText().toString());

            RegistrationRequest.CheckRegisterResult checkResult = request.check();

            if (checkResult == RegistrationRequest.CheckRegisterResult.InvalidName) {
                showDialog(getString(R.string.reg_invalid_name_ttl), getString(R.string.reg_invalid_name_msg), true);
                return;
            } else if (checkResult == RegistrationRequest.CheckRegisterResult.InvalidEmail) {
                showDialog(getString(R.string.reg_invalid_email_ttl), getString(R.string.reg_invalid_email_msg), true);
                return;
            } else if (checkResult == RegistrationRequest.CheckRegisterResult.InvalidPassword) {
                showDialog(getString(R.string.reg_invalid_password_ttl), getString(R.string.reg_invalid_password_msg), true);
                return;
            } else if (request.password == null ||
                    !request.password.equals(editTextRepeatPassword.getText().toString())) {
                showDialog(getString(R.string.reg_password_doesnt_match_ttl), getString(R.string.reg_password_doesnt_match_msg), true);
                return;
            }

            RegistrationActivity context = this;
            ApiClient apiClient = ApiClient.getInstance();

            apiClient.register(request, new Callback<RegistrationResponse>() {
                @Override
                public void onResponse(@NonNull Call<RegistrationResponse> call, @NonNull Response<RegistrationResponse> response) {
                    RegistrationResponse.Result result = response.body().result;
                    if (result == RegistrationResponse.Result.Ok) {
                        Intent i = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else if (result == RegistrationResponse.Result.EmailInUse) {
                        showDialog(getString(R.string.reg_email_in_use_ttl), getString(R.string.reg_email_in_use_msg), true);
                    }
                }

                @Override
                public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                    // Network error or bad response format
                    showDialog(getString(R.string.network_error_ttl), getString(R.string.network_error_msg), true);
                }
            });
        });

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!(v instanceof TextInputEditText)) {
                    hideKeyboard(v);
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

        TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(v);
                    v.clearFocus();
                    return true;
                }
                return false;
            }
        };

        editTextEmailReg.setOnEditorActionListener(editorActionListener);
        editTextPasswordReg.setOnEditorActionListener(editorActionListener);
        editTextUsernameReg.setOnEditorActionListener(editorActionListener);
        editTextRepeatPassword.setOnEditorActionListener(editorActionListener);

        editTextEmailReg.setOnFocusChangeListener(focusChangeListener);
        editTextPasswordReg.setOnFocusChangeListener(focusChangeListener);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
package com.jithvar.ponpon.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.jithvar.ponpon.LoginActivity;
import com.jithvar.ponpon.R;

/**
 * Created by KinG on 22-10-2017.
 * Created by ${EMAIL}.
 */

public class ChangeEmail extends DialogFragment {

    private EditText emailE;
    private Button submit;
    private TextInputLayout emailLayout;

    public interface ChangeEmailDialog{
        void onChangedEmail(String email);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            try {
                dialog.getWindow().setLayout(width, height);
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_email, container);
//        re_send = (Button) view.findViewById(R.id.re_send_otp);
        submit = (Button) view.findViewById(R.id.submit);
        emailE = (EditText) view.findViewById(R.id.email_et);
        emailLayout = (TextInputLayout) view.findViewById(R.id.email_l);
        emailE.addTextChangedListener(new MyTextWatcher(emailE));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEntered();

//                VerifyLogin.OtpVerifyDialog otpVerifyDialog = (VerifyLogin.OtpVerifyDialog) getActivity();
//                otpVerifyDialog.onFinishOtpDialog(emailE.getText().toString());
//                dismiss();
            }
        });

        emailE.requestFocus();
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        getDialog().setTitle("Enter ");

        return view;
    }

    private void emailEntered(){
        if (!validateEmail()) {
            return;
        }

        ChangeEmailDialog changeEmailDialog = (ChangeEmailDialog) getActivity();
        changeEmailDialog.onChangedEmail(emailE.getText().toString());
        dismiss();
    }

    private boolean validateEmail() {
        if (emailE.getText().toString().trim().isEmpty() ||
                !emailE.getText().toString().contains("@") ||
                !emailE.getText().toString().substring(emailE.getText().toString().indexOf("@")+1).
                        contains(".")) {
            emailLayout.setError(getString(R.string.email_req));
            requestFocus(emailE);
            return false;
        } else {
            emailLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams
                    .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {
        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()){
                case R.id.email_et:
                    validateEmail();
                    break;
            }
        }
    }
}

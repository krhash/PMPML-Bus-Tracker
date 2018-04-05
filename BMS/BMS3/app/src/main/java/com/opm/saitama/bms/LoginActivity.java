package com.opm.saitama.bms;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.v4.app.FragmentActivity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    final Context context = this;
    private Button buttonSignIn,buttongotoRegister;
    private EditText useridField,pwdField;
    private TextView tvForgotPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        initVars();
        buttonSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPost();
            }
        });

        buttongotoRegister.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvForgotPwd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(LoginActivity.this);
                View promtsView = li.inflate(R.layout.promt_email_input,null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);

                alertDialogBuilder.setView(promtsView);

                final EditText inputEmailPrompt = (EditText)promtsView.findViewById(R.id.EditText_PromptEmail);

                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                resetPwd(inputEmailPrompt.getText().toString());
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });


    }

    public void initVars(){
        useridField = (EditText)findViewById(R.id.editText_UID);
        pwdField = (EditText)findViewById(R.id.editText_password_login);
        buttonSignIn = (Button)findViewById(R.id.button_SignIn);
        buttongotoRegister = (Button)findViewById(R.id.button_gotoRegister);
        tvForgotPwd = (TextView)findViewById(R.id.textView_ForgotPwd);

    }

    public void loginPost(){
        String username = useridField.getText().toString();
        String password = pwdField.getText().toString();
        new LoginUser(this).execute(username,password);
    }

    public void resetPwd(String username){
        new ResetPassword(this).execute(username);
    }
}

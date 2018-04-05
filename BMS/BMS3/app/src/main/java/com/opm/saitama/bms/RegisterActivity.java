package com.opm.saitama.bms;

//import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    EditText et_name,et_username,et_password,et_confirmPassword,et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initVars();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_confirmPassword.getText().toString().equals( et_password.getText().toString())) {
                    registerPost();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    void initVars(){
        et_name = (EditText)findViewById(R.id.editText_name);
        et_email= (EditText)findViewById(R.id.editText_email);
        et_username = (EditText)findViewById(R.id.editText_Register_UID);
        et_password = (EditText)findViewById(R.id.editText_setPassword);
        et_confirmPassword = (EditText)findViewById(R.id.editText_confirmPassword);
        registerButton = (Button)findViewById(R.id.button_Register);
    }

    public void registerPost(){

        String name = et_name.getText().toString();
        String email= et_email.getText().toString();
        String userid= et_username.getText().toString();
        String pass = et_password.getText().toString();
        new RegisterUser(this).execute(name,email,userid,pass);

    }
}

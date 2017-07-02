package com.csb.csb_test_webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button signUp = (Button) findViewById(R.id.buttonSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView loginInput = (TextView) findViewById(R.id.inputSignUpLogin);
                TextView pwdInput = (TextView) findViewById(R.id.inputSignUpPassword);

                signUp(loginInput.getText().toString(),pwdInput.getText().toString());
            }
        });
    }

    private void signUp(final String login,final String pwd){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView inputSignUp = (TextView) findViewById(R.id.inputSignUpLogin);
        builder.setMessage(getString(R.string.infoPostSignUp) + " " + inputSignUp.getText().toString() + getString(R.string.defaultMailAdress));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("login", login);
                resultIntent.putExtra("pwd", pwd);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

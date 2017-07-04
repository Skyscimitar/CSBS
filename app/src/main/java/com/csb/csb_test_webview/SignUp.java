package com.csb.csb_test_webview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
                TextView prenomInput = (TextView) findViewById(R.id.inputSignUpPrenom);
                TextView nomInput = (TextView) findViewById(R.id.inputSignUpNom);
                TextView telInput = (TextView) findViewById(R.id.inputSignUpTel);

                signUp(loginInput.getText().toString(),pwdInput.getText().toString(),prenomInput.getText().toString(),nomInput.getText().toString(),telInput.getText().toString());
            }
        });
    }

    private void signUp(final String login,final String pwd,final String prenom,final String nom,final String tel){
        TextView inputSignUp = (TextView) findViewById(R.id.inputSignUpLogin);
        if(!login.equals("") && !pwd.equals("") && !prenom.equals("") && !nom.equals("") && !tel.equals("")) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("login", login);
            resultIntent.putExtra("pwd", pwd);
            resultIntent.putExtra("prenom", prenom);
            resultIntent.putExtra("nom", nom);
            resultIntent.putExtra("tel", tel);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Tous les champs doivent être remplis !", Toast.LENGTH_SHORT).show();
        }
    }
}

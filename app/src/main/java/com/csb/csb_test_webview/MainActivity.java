package com.csb.csb_test_webview;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Login process
        Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView mailInput = (TextView) findViewById(R.id.inputLogin);
                TextView pwdInput = (TextView) findViewById(R.id.inputPassword);
                final String mail = mailInput.getText().toString() + getString(R.string.defaultMailAdress);
                final String pwd = pwdInput.getText().toString();
                mAuth.signInWithEmailAndPassword(mail, pwd)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d("Log Firebase", "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            TextView textError = (TextView) findViewById(R.id.errorLogin);
                            if (!task.isSuccessful()) {
                                Log.d("Log Firebase", "signInWithEmail:error:", task.getException());
                                textError.setVisibility(View.VISIBLE);
                            }else{
                                textError.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
            }
        });

        // Sign up activity
        Button signUp = (Button) findViewById(R.id.buttonInscription);
        signUp.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent = new Intent(getApplicationContext(), SignUp.class);
                  startActivityForResult(intent, 1);
              }
          });

        // Initialize mAuthListener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
             public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                if (user != null) {
                                        // User is signed in
                                                Log.d("Log Firebase", "onAuthStateChanged:signed_in:" + user.getUid());
                                    } else {
                                        // User is signed out
                                                Log.d("Log Firebase", "onAuthStateChanged:signed_out");
                                    }
                            }
         };
    }

    /**
     * OVERRIDES
     **/

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    final String login = data.getStringExtra("login") + getString(R.string.defaultMailAdress);
                    final String pwd = data.getStringExtra("pwd");
                    final String prenom = data.getStringExtra("prenom");
                    final String nom = data.getStringExtra("nom");
                    final String tel = data.getStringExtra("tel");
                    mAuth.createUserWithEmailAndPassword(login, pwd)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.d("Log Firebase", "createUserWithEmail:onComplete:" + task.isSuccessful());
                                    TextView errorTextView = (TextView) findViewById(R.id.inscriptionError);
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Log.d("Log Firebase", "error");
                                        errorTextView.setVisibility(View.VISIBLE);
                                    }else{
                                        sendVerificationEmail();
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if(user != null)
                                            writeNewUser(user.getUid(),user.getEmail(),prenom,nom,tel);
                                        errorTextView.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
                break;
            }
        }
    }

    /****
     *
     * CUSTOMS FUNCTIONS
     *
     ****/

    // Register function
    private void writeNewUser(String userId, String email, String prenom, String nom, String tel) {
        User user = new User(email, prenom, nom, tel);
        mDatabase.child("users").child(userId).setValue(user);
    }

    // Send mail verification function
    public void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if(user != null)
                                    Toast.makeText(getApplicationContext(), "Signup successful. Verification email sent to "+ user.getEmail(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}



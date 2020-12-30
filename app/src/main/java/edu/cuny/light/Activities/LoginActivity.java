package edu.cuny.light.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.cuny.light.R;

public class LoginActivity extends AppCompatActivity {

    private EditText email,passwords;
    private Button btnLogin,btnSignup,btnResetPwd;
    FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        email = findViewById(R.id.etEmail);
        passwords = findViewById(R.id.etPw);
        btnSignup = findViewById(R.id.btnSignupMain);
        btnLogin=findViewById(R.id.btnLogin);
        btnResetPwd=findViewById(R.id.btnResetPwd);


        //the below authentication listener allow users to log in only once;
        //it detects if the getCurrentUser() is null.
        //if the users logged in already and open the app, it will skip the log in activity
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

                if(mFirebaseUser !=null && mFirebaseUser.isEmailVerified()){
                    Toast.makeText(LoginActivity.this,"You are logged in.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this,MenuActivity.class);
                    startActivity(i);
                }
//                else if (mFirebaseUser==null){
//                    Toast.makeText(LoginActivity.this, "Please log in again.", Toast.LENGTH_SHORT).show();
//                }
            }

        };


        //the below onClickListener for log in button set conditions if email or password is empty
        //even if both are not empty, the email EditText itself has a condition to fulfill: the input must be in email format
        btnLogin.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               String emailStr=email.getText().toString();
               String pwdStr=passwords.getText().toString();

               if(emailStr.isEmpty()){
                   email.setError("Please enter a valid email address.");
                   email.requestFocus();
               }
               else if (pwdStr.isEmpty()){
                   passwords.setError("Please enter a valid password.");
                   passwords.requestFocus();
               }
               else if (!(pwdStr.isEmpty()&&emailStr.isEmpty())){
                    mAuth.signInWithEmailAndPassword(emailStr,pwdStr).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                            }

                            else if (task.isSuccessful()&&(!mAuth.getCurrentUser().isEmailVerified())){
                                Toast.makeText(LoginActivity.this,"Email address is not verified", Toast.LENGTH_LONG).show();
                            }
                            else if (task.isSuccessful() && mAuth.getCurrentUser().isEmailVerified()){
                                Intent toMenu = new Intent(LoginActivity.this, MenuActivity.class);
                                startActivity(toMenu);
                            }

                        }
                    });
               }

               else{
                   Toast.makeText(LoginActivity.this,"Error occurred.",Toast.LENGTH_SHORT).show();
               }
           }


        });


        //sign up button to move on to the sign up activity
        btnSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent toSignUp = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(toSignUp);
            }
        });

        btnResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toResetPwd = new Intent (LoginActivity.this, ResetPwdActivity.class);
                startActivity(toResetPwd);
            }
        });
    }


    //another function while opening the app to see if the user is already logged in
    //if logged in already, then direct the user to main menu
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

}

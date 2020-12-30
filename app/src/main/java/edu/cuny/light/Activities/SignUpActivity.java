package edu.cuny.light.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.cuny.light.R;

public class SignUpActivity extends AppCompatActivity {
    private EditText email,passwords;
    private CheckBox checkbox;
    private Button btnSignup;
    private Button btnBack;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.etEmail2);
        passwords = findViewById(R.id.etPw2);
        btnSignup = findViewById(R.id.btnSignup);
        btnBack=findViewById(R.id.btnBack);


        //below to set hyperlink to the private policy and EULA
        final CheckBox checkbox = findViewById(R.id.checkBoxAgreements);
        TextView tvAgreements = findViewById(R.id.tvAgreements);

        checkbox.setText("");
        tvAgreements.setText(Html.fromHtml("I have read and agree to the " +
                "<a href='https://app.termly.io/document/privacy-policy/9de5bde0-3f8d-4bad-a465-877e899938ff'>PRIVACY POLICY</a>"
                +"and" + "<a href='https://www.eulatemplate.com/live.php?token=xtAYiayC1QfQMOOXjaaWZFJBwJTQxbGm'>End-User License Agreement</a>"));
        tvAgreements.setClickable(true);
        tvAgreements.setMovementMethod(LinkMovementMethod.getInstance());


        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent toMain = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(toMain);
            }
        });

        //the below onClickListener for sign up button set conditions if email or password is empty
        //even if both are not empty, the email and password EditTexts themselves has conditions to fulfill:
        //the input must be in email format
        //passwords must be at least 6 characters
        btnSignup.setOnClickListener(new View.OnClickListener(){
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
                else if (!checkbox.isChecked()){
                    Toast.makeText(SignUpActivity.this,"Please check the privacy policy and end-user agreement checkbox.",
                            Toast.LENGTH_LONG).show();
                }
                else if (checkbox.isChecked()&&!(pwdStr.isEmpty()&&emailStr.isEmpty())){
                    mAuth.createUserWithEmailAndPassword(emailStr,pwdStr).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task){
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this, "Sign up failed, please try again.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //if task is successful, send an email verification to the email address
                                //and guide users back into login page
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(SignUpActivity.this,"Verification has been sent. Please check your email.",
                                                    Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                        }

                                        else{
                                            Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                            }
                        }
                    });
                }

                else{
                    Toast.makeText(SignUpActivity.this,"Error occurred.",Toast.LENGTH_SHORT).show();
                }
            }


        });
    }
}

package edu.cuny.light.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.cuny.light.R;

public class ResetEmailActivity extends AppCompatActivity {

    private Button btnResetEmail;
    private EditText etPwd,etNewEmail,etReEmail;

    private FirebaseUser mUser;

    //1st checker variable used to check if the user credential can be re-authenticated
    private boolean reAuthenticateChecker=false;
    //2nd checker variable to check if 2 emails are the same
    private boolean emailChecker=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_email);

        //initiate variables
        btnResetEmail=findViewById(R.id.btnResetEmail);
        etPwd=findViewById(R.id.etPwd);
        etNewEmail=findViewById(R.id.etNewEmail);
        etReEmail=findViewById(R.id.etReEmail);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        btnResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reAuthenticated()&&emailsAreSame()){
                    mUser.updateEmail(etNewEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ResetEmailActivity.this, "Email updated.Please log in with new email.", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        startActivity(new Intent(ResetEmailActivity.this,LoginActivity.class));
                                    }
                                    else{
                                        Toast.makeText(ResetEmailActivity.this, "Email update failed. Please try again.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else if (!reAuthenticated()){
                    Toast.makeText(ResetEmailActivity.this, "Password does not match with your email.", Toast.LENGTH_LONG).show();
                }
                else if (!emailsAreSame()){
                    Toast.makeText(ResetEmailActivity.this, "Emails entered do not match.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    protected boolean reAuthenticated(){
        String pwd=etPwd.getText().toString();

        AuthCredential credential = EmailAuthProvider
                .getCredential(mUser.getEmail(), pwd);

        mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    reAuthenticateChecker= true;
                }
                else{
                    reAuthenticateChecker= false;
                }
            }
        });

        return reAuthenticateChecker;
    }

    protected boolean emailsAreSame(){
        String email1=etNewEmail.getText().toString();
        String email2=etReEmail.getText().toString();

        if (email1.equals(email2)){
            emailChecker=true;
        }
        else{
            emailChecker= false;
        }

        return emailChecker;
    }
}

package edu.cuny.light.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import edu.cuny.light.R;

public class ResetPwdActivity extends AppCompatActivity {
    Button btnSendResetPwd,btnBack2;
    EditText etEmail3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);

        btnBack2=findViewById(R.id.btnBack2);
        btnSendResetPwd=findViewById(R.id.btnSendResetEmail);
        etEmail3=findViewById(R.id.etEmail3);

        final FirebaseAuth mAuth=FirebaseAuth.getInstance();

        btnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSendResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etEmail3.getText().toString();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ResetPwdActivity.this,
                                    "Passwords reset instructions have been sent to your email.",Toast.LENGTH_SHORT).show();
                            etEmail3.setText("");
                        }
                        else{
                            Toast.makeText(ResetPwdActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });


    }
}

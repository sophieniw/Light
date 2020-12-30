package edu.cuny.light.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.cuny.light.R;

public class DeleteAccountActivity extends AppCompatActivity {

    private Button btnBack3,btnDeleteAccount;
    private EditText etEmail4, etPwd4;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    boolean check=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        //below declare buttons and editTexts (Email address and passwords)
        btnBack3=findViewById(R.id.btnBack3);
        btnDeleteAccount=findViewById(R.id.btnDeleteAccount);
        etEmail4=findViewById(R.id.etEmail4);
        etPwd4=findViewById(R.id.etPwd4);

        //below declare the firebase auth
        mAuth=FirebaseAuth.getInstance();

        //below declare the current user
        mUser=mAuth.getCurrentUser();

        //below to implement back button
        btnBack3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //below set the onClick behavior for btnDeleteAccount
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //collect credentials
                String email=etEmail4.getText().toString();
                String passwords=etPwd4.getText().toString();

                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, passwords);

                //check credentials through reauthenticate method
                mUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //if reauthenticate method succeeds
                        if(task.isSuccessful()){
                            //ask users to confirm deletion
                            //      confirmDeletion <-- return boolean; also shows an alert dialog
                            if (confirmDeletion()){
                                mUser.delete();
                                Toast.makeText(DeleteAccountActivity.this,"Your account has been successfully deleted.",
                                        Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(DeleteAccountActivity.this,LoginActivity.class));
                            }
                        }
                        else{
                            Toast.makeText(DeleteAccountActivity.this,"Credentials entered does not match with current user.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }



    //below to create a function to show an alert to make sure users want to delete account
    protected boolean confirmDeletion(){

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DeleteAccountActivity.this);
        alertBuilder.setTitle("");
        alertBuilder.setMessage("Are you sure you want to delete your account? Once the account is deleted, it cannot be recovered.");

        //below to implement confirm button
        //if confirm --> return true; else --> return false
        alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //1.return true
                //2.DISMISS alert dialog (dismiss means the alert dialog task is FINISHED)
                check=true;
                dialog.dismiss(); //dialog variable is from onClick() above
            }
        });

        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //1. return false
                //2. DISMISS alert dialog (see above)
                check=false;
                dialog.dismiss();
            }
        });

        AlertDialog dialog = alertBuilder.create();
        dialog.show();

        return check;
    }
}

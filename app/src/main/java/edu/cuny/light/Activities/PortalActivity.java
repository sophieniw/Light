package edu.cuny.light.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;



import edu.cuny.light.R;

public class PortalActivity extends AppCompatActivity {

    private TextView tvMem1, tvMem2;
    private ImageButton ibPolicy,ibSetting;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);


        currentUserID=FirebaseAuth.getInstance().getCurrentUser().getUid();


        tvMem1=findViewById(R.id.tvYourMem);
        tvMem2=findViewById(R.id.tvSavedMem);
        ibPolicy=findViewById(R.id.ibPolicy);
        ibSetting=findViewById(R.id.ibSetting);


        ibSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PortalActivity.this);
                String[] options = {"Sign out","Reset email address","Reset passwords","Delete account"};
                builder.setTitle("");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case 0: //Sign out
                                FirebaseAuth.getInstance().signOut();
                                Intent toMain=new Intent(PortalActivity.this, LoginActivity.class);
                                startActivity(toMain);
                                break;
                            case 1: //Reset email address
                                startActivity(new Intent(PortalActivity.this,ResetEmailActivity.class));
                                break;
                            case 2: //Reset passwords
                                startActivity(new Intent(PortalActivity.this,ResetPwdActivity.class));
                                break;
                            case 3: //Delete account
                                startActivity(new Intent(PortalActivity.this,DeleteAccountActivity.class));
                                break;
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        ibPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(PortalActivity.this).create();
                alertDialog.setTitle("");
                alertDialog.setMessage(Html.fromHtml(
                        "<a href='https://app.termly.io/document/privacy-policy/9de5bde0-3f8d-4bad-a465-877e899938ff'>Privacy Policy</a>" +
                                "<br><a href='https://www.eulatemplate.com/live.php?token=xtAYiayC1QfQMOOXjaaWZFJBwJTQxbGm'>End-User License Agreement</a>"));

                alertDialog.show();
                ((TextView)alertDialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

            }

        });

        tvMem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showYourMemory = new Intent(new Intent(PortalActivity.this, YourMemoryActivity.class));
                startActivity(showYourMemory);
            }
        });


        tvMem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showSavedMemory = new Intent(new Intent(PortalActivity.this, SavedMemoryActivity.class));
                startActivity(showSavedMemory);

            }
        });


    }


}



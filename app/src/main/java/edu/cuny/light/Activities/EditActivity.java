package edu.cuny.light.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.cuny.light.Models.Post;
import edu.cuny.light.R;

public class EditActivity extends AppCompatActivity {
    private Button btnLogout,btnShare;
    private TextView memoryInput;
    FirebaseAuth mAuth;
    DatabaseReference UserRef;
    String currUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mAuth=FirebaseAuth.getInstance();
        UserRef=FirebaseDatabase.getInstance().getReference("Users");
        currUserID=mAuth.getCurrentUser().getUid();


        btnLogout=findViewById(R.id.btnLogout);
        btnShare=findViewById(R.id.btnShare);
        memoryInput=findViewById(R.id.etMemoryInput);

        //set onClickListener for log out button to allow users to log out
        btnLogout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                Intent toMain=new Intent(EditActivity.this, LoginActivity.class);
                startActivity(toMain);
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();
            }
        });

    }


    //a method to allow users to add what they wrote into the database
    //condition is checked to make sure input is not empty
    private void addPost(){
        String content=memoryInput.getText().toString();
        String ownerID=mAuth.getCurrentUser().getUid();

        //if input is empty, an error msg will show
        //else the user input would be added as content for a new Post object and such Post object will be added into database
        if(content.isEmpty()){
            Toast.makeText(this,"memory content cannot be empty",Toast.LENGTH_LONG).show();
        }
        else{
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mRef=database.getReference("Posts").push();

            String postRef = mRef.getKey();

            Post post = new Post(postRef,ownerID,content);

            mRef.setValue(post);

            UserRef.child(ownerID).child("Owned").child(postRef).setValue(post);

            Toast.makeText(this, "Memory successfully shared.", Toast.LENGTH_SHORT).show();
            memoryInput.setText("");


        }
    }


}

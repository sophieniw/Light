package edu.cuny.light.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.cuny.light.Models.Post;
import edu.cuny.light.R;

public class SavedMemoryActivity extends AppCompatActivity {

    DatabaseReference databasePostRef,databaseUserRef,savedMemRef;
    String currentUserID;
    private RecyclerView rvSavedMemory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_memory);

        // declare the RecyclerView object as postRecList
        rvSavedMemory=findViewById(R.id.rvSavedMem);
        rvSavedMemory.setHasFixedSize(true);
        rvSavedMemory.setLayoutManager(new LinearLayoutManager(this));


        //declare a Firebase database reference to refer to Light\Posts
        databasePostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        databaseUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        currentUserID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        savedMemRef=databaseUserRef.child(currentUserID).child("Saved");

        //keep the databases synced
        databasePostRef.keepSynced(true);
        databaseUserRef.keepSynced(true);


    }


    public void onStart() {
        super.onStart();

        //set up recycler view with Post and PostViewHolder
        //similar functionality as the recycler view under gallery activity
        FirebaseRecyclerAdapter<Post, SavedMemoryActivity.PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, SavedMemoryActivity.PostViewHolder>(
                Post.class,
                R.layout.portal_row,
                SavedMemoryActivity.PostViewHolder.class,
                savedMemRef) {
            @Override
            protected void populateViewHolder(final SavedMemoryActivity.PostViewHolder postViewHolder, final Post post, int i) {
                final String POSTKEY = getRef(i).getKey();
                postViewHolder.setPostContent(post.getContent());
                postViewHolder.setBtnSendStatus(POSTKEY);


                postViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }

        };
        rvSavedMemory.setAdapter(firebaseRecyclerAdapter);
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder{
        View view;
        private TextView reactNum;
        private int countReact;
        private String currentUserID;
        private DatabaseReference databaseReactRef;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            reactNum=view.findViewById(R.id.reactNum);
            databaseReactRef= FirebaseDatabase.getInstance().getReference().child("Reacts");
            currentUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();



        }

        public void setPostContent(String content){
            TextView postContent = view.findViewById(R.id.postContent);
            postContent.setText(content);
            postContent.setMovementMethod(new ScrollingMovementMethod());
        }

        public void setBtnSendStatus(final String postID){
            databaseReactRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postID).hasChild(currentUserID)){
                        countReact=(int)dataSnapshot.child(postID).getChildrenCount();
                        reactNum.setText(Integer.toString(countReact));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}

package edu.cuny.light.Activities.ReActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.cuny.light.Models.Post;
import edu.cuny.light.R;

public class HomeFragment extends Fragment {


    private RecyclerView postRecList;

    private boolean reactChecker=false;

    private LinearLayoutManager mManager;
    private DatabaseReference databasePostRef,databaseReactRef,databaseUserRef;
    private String currentUserID;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        postRecList=view.findViewById(R.id.post_list);

        mManager = new LinearLayoutManager(view.getContext());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);

        postRecList.setLayoutManager(mManager);

        //declare a Firebase database reference
        databasePostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        databaseReactRef = FirebaseDatabase.getInstance().getReference().child("Reacts");
        databaseUserRef = FirebaseDatabase.getInstance().getReference().child("Users");

        //get current user id
        currentUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();

        return view;
    }


    public void setAdapter(){
        //set up recycler view with Post and PostViewHolder
        //Post object class is designed under the models folder
        //PostViewHolder is defined towards the end of this java file --> please see line 212
        FirebaseRecyclerAdapter<Post, PostViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Post, PostViewHolder>(
                Post.class,
                R.layout.post_row,
                PostViewHolder.class,
                databasePostRef) {

            //use populateViewHolder to populate recycler view with each existing post
            //each existing post is looped through and has the index i. The index is used to get the post key or postID
            @Override
            protected void populateViewHolder(final PostViewHolder postViewHolder, final Post post, int i) {
                final String POSTKEY=getRef(i).getKey();

                //setPostContent method to present the post content
                //setPostContent method is defined at the end of this file
                postViewHolder.setPostContent(post.getContent());


                postViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                //SET SEND STATUS FOR EACH POST
                //setBtnSendStatus method is defined at the end of this file
                postViewHolder.setBtnSendStatus(POSTKEY);

                //SET SAVE STATUS FOR EACH POST
                //setBtnSaveStatus method is defined at the end of this file
                postViewHolder.setBtnSaveStatus(POSTKEY);

                postViewHolder.btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (postViewHolder.etReact.getText().toString().equals("You are not alone.")){

                            databaseReactRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    databaseReactRef.child(POSTKEY).child(currentUserID).setValue(true);
                                    reactChecker=true;
                                    if(reactChecker){
                                        //hide the empower button and set text for editText etReact
                                        postViewHolder.btnSend.setVisibility(View.GONE);
                                        postViewHolder.etReact.setText("You are not alone.");
                                        postViewHolder.etReact.setTextColor(Color.rgb(233,30,99));
                                        postViewHolder.etReact.setEnabled(false);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }


                        else{
                            Toast.makeText(getContext(),
                                    "You must type 'You are not alone.' to send react.", Toast.LENGTH_LONG).show();
                        }
                    }
                });




                // SET SAVE BUTTON FOR EACH POST
                postViewHolder.btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseUserRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.child(currentUserID).child("Saved").hasChild(POSTKEY)){
                                    databaseUserRef.child(currentUserID).child("Saved").child(POSTKEY).setValue(post);
                                    Toast.makeText(getContext(),"Saved to your portal.",Toast.LENGTH_SHORT).show();
                                    postViewHolder.btnSave.setText("Saved");
                                    postViewHolder.btnSave.setEnabled(false);
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
            }
        };

        postRecList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        View view;
        private Button btnSend,btnSave;
        private TextView reactNum,postContent;
        private EditText etReact;
        private int countReact;
        private String currentUserID;
        private DatabaseReference databaseReactRef,databaseUserRef;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            btnSend = view.findViewById(R.id.btnSend);
            btnSave = view.findViewById(R.id.btnSave);
            reactNum=view.findViewById(R.id.reactNum);
            etReact=view.findViewById(R.id.etReact);
            postContent=view.findViewById(R.id.postContent);


            databaseUserRef = FirebaseDatabase.getInstance().getReference().child("Users");
            databaseReactRef=FirebaseDatabase.getInstance().getReference().child("Reacts");
            currentUserID=FirebaseAuth.getInstance().getCurrentUser().getUid();

            postContent.setBackgroundColor(0xfff00000);

        }

        public void setPostContent(String content){
            //TextView postContent = view.findViewById(R.id.postContent);
            postContent.setText(content);
        }

        public void setBtnSaveStatus(final String postID){
            databaseUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(currentUserID).child("Saved").hasChild(postID)){
                        btnSave.setText("Saved");
                        btnSave.setTextColor(Color.rgb(137,137,137));
                        btnSave.setEnabled(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


        }

        public void setBtnSendStatus(final String postID){
            databaseReactRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(postID)){
                        countReact=(int)dataSnapshot.child(postID).getChildrenCount();
                        reactNum.setText(Integer.toString(countReact));

                        if(dataSnapshot.child(postID).hasChild(currentUserID)){
                            //hide the empower button and set text for editText etReact
                            btnSend.setVisibility(View.GONE);
                            etReact.setText("You are not alone.");
                            etReact.setTextColor(Color.rgb(233,30,99));
                            etReact.setEnabled(false);
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}

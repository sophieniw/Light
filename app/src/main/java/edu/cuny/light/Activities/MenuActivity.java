package edu.cuny.light.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.cuny.light.Activities.ReActivities.GalleryReActivity;
import edu.cuny.light.R;

public class MenuActivity extends AppCompatActivity {
    private Button btnStart,btnDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnDesc = findViewById(R.id.btnLightDesc);
        btnStart = findViewById(R.id.btnStart);

        btnDesc.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent toDesc = new Intent(MenuActivity.this, DescActivity.class);
                startActivity(toDesc);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toGallery = new Intent(MenuActivity.this, GalleryActivity.class);
                startActivity(toGallery);
            }
        });
    }
}

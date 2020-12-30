package edu.cuny.light.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import edu.cuny.light.R;

public class DescActivity extends AppCompatActivity {

    private Button btnExit;
    private TextView tvDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc);

        btnExit = findViewById(R.id.btnExit);
        tvDesc=findViewById(R.id.tvDesc);

        btnExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent toMenu = new Intent(DescActivity.this, MenuActivity.class);
                startActivity(toMenu);
            }
        });

        tvDesc.setText("    Light is an app dedicated to improving mental health. " +
                "It is an anonymous platform to share your memories. \n\n" +
                "    In order to guard a safe place for people who opened up, there is only one way to react to memories - type in " +
                "\"You are not alone.\" and press the Empower button.\n\n " +
                "    Remember, talking about what has been hurting you does not make you weak, it makes you human.\n\n");
    }
}

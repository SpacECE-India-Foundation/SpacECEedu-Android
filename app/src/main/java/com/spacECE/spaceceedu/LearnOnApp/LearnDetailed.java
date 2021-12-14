package com.spacECE.spaceceedu.LearnOnApp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.instamojo.android.Instamojo;
import com.spacECE.spaceceedu.R;

import java.text.MessageFormat;


public class LearnDetailed extends AppCompatActivity {

    TextView Id, Title, Description, Duration, Price, Mode_Type;
    Button Buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_detailed);

        Window window = this.getWindow();
        window.setStatusBarColor(Color.rgb(200,100,50));


        Title = findViewById(R.id.Detail_Title);
        Description = findViewById(R.id.Detail_Description);
//        Duration = findViewById(R.id.Detail_Duration);
//        Price = findViewById(R.id.Detail_Price);
        Mode_Type = findViewById(R.id.Detail_Mode_Type);


        Buy = findViewById(R.id.Buy);

        Intent intent = getIntent();
        int pos = intent.getIntExtra("pos",1);
        Learn learn = LearnOn_Main.Llist.get(pos);


//        Id.setText(MessageFormat.format("ID - {0}", learn.getId()));
        Title.setText(learn.getTitle());
        Description.setText(learn.getDescription());

//        Duration.setText(learn.getDuration());
//        Price.setText(learn.getPrice());
        Mode_Type.setText(MessageFormat.format("{0} on {1}", learn.getMode(), learn.getType()));

        Buy.setOnClickListener(v -> {
            Toast.makeText(LearnDetailed.this, "yeah!", Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(Buy.getContext(), Payment.class);
            startActivity(intent1);
            finish();
        });




    }



}
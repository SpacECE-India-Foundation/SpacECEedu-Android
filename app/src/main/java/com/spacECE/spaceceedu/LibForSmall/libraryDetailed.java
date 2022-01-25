package com.spacECE.spaceceedu.LibForSmall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.spacECE.spaceceedu.R;

public class libraryDetailed extends AppCompatActivity {

    TextView book,author,edition,desc,price,condition,owner;
    Button callbtn,orderbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_detailed);

        Window window = this.getWindow();
        window.setStatusBarColor(Color.rgb(200,100,50));

        book=findViewById(R.id.book_name);
        author=findViewById(R.id.author_name);
        edition=findViewById(R.id.edition_name);
        desc=findViewById(R.id.desc_name);
        price=findViewById(R.id.price_name);
        condition=findViewById(R.id.condition_name);
        owner=findViewById(R.id.owner_name);
        callbtn=findViewById(R.id.call_btn);
        orderbtn=findViewById(R.id.order_btn);

        Intent intent=getIntent();
        int pos=intent.getIntExtra("pos",1);
        books books=Library_main.list.get(pos);

        book.setText(books.getProduct_title());
        desc.setText(books.getProduct_desc());
        price.setText(books.getProduct_price());


    }
}
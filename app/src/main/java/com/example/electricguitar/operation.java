package com.example.electricguitar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class operation extends AppCompatActivity {
    private Button upg,rela,delete,back,compelish;
    private EditText title,infm;
    private MyDAO myDAO;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_operation);
        myDAO=new MyDAO(this);
        upg=findViewById(R.id.b_updage);
        rela=findViewById(R.id.b_release);
        delete=findViewById(R.id.b_delete);
        title=findViewById(R.id.edit_t);
        infm=findViewById(R.id.edit_ifm);
        back=findViewById(R.id.button_back);
        compelish=findViewById(R.id.finish);
        Intent intent =getIntent();
        String t=intent.getStringExtra("title");
        String inf=intent.getStringExtra("inf");
        String selid=intent.getStringExtra("id");
        title.setText(t);
        infm.setText(inf);

        //更新乐谱信息
        upg.setOnClickListener(v -> {
            String t1 =title.getText().toString();
            String inf1 =infm.getText().toString();
            myDAO.update(selid, t1, inf1);

        });
        //删除乐谱信息
        delete.setOnClickListener(v -> myDAO.delete(selid));
        //发布乐谱信息
        rela.setOnClickListener(v -> myDAO.releas(selid));
        back.setOnClickListener(v -> {
            Intent intent12 =new Intent(operation.this, MainActivity.class);
            startActivity(intent12);
        });
        compelish.setOnClickListener(v -> {
            Intent intent1 =new Intent(operation.this, MainActivity.class);
            startActivity(intent1);

        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
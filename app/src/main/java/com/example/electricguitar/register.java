package com.example.electricguitar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class register extends AppCompatActivity {
    private Button achieve,cancel;
    private EditText uname,password;
    private MyDAO myDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        myDAO=new MyDAO(this);
        achieve=findViewById(R.id.but_achieve);
        cancel=findViewById(R.id.but_cancel);
        uname=findViewById(R.id.edit_username);
        password=findViewById(R.id.edit_Password);
        achieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String c=uname.getText().toString();
                String p=password.getText().toString();
                int i=myDAO.Query(c);
                if (i==0){
                    showAlertDialog("用户名已存在！请另择用户名！");

                } else if (i==-1) {

                    myDAO.insertInfo(c,p);
                    showAlertDialog1("注册成功！点击确定返回登录！");
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              uname.setText(" ");
              password.setText(" ");
                Intent intent=new Intent(register.this, login.class);
                startActivity(intent);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void showAlertDialog(String mes){
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        adBuilder.setIcon(R.mipmap.ic_launcher);
        adBuilder.setTitle("提示");
        adBuilder.setMessage(mes);
        adBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adBuilder.show();
    }
    private void showAlertDialog1(String mes){
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        adBuilder.setIcon(R.mipmap.ic_launcher);
        adBuilder.setTitle("提示");
        adBuilder.setMessage(mes);
        adBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(register.this, login.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        adBuilder.show();
    }
}
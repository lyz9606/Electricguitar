package com.example.electricguitar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class login extends AppCompatActivity {
    private Button login,register;
    private EditText username,password;

    private MyDAO myDAO;
    private CheckBox reminder;
    private DataBaseUtil util;
    private SharedPreferences sharedPreferences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        myDAO = new MyDAO(this);
        login=findViewById(R.id.but_login);
        register=findViewById(R.id.but_register);
        username=findViewById(R.id.edit_username);
        password=findViewById(R.id.edit_Password);
        reminder=findViewById(R.id.rember);
        sharedPreferences=this.getSharedPreferences("loginInfo", MODE_PRIVATE);
        initView();
        DataBaseUtil util=new DataBaseUtil(this);
        if (!util.checkDataBase()){
            try {
                util.copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a=username.getText().toString();
                String pass=password.getText().toString();
                String queriedPassword = myDAO.query(a); // 假设查询方法返回密码的字符串
                if (queriedPassword.equals(a)){
                    showAlertDialog("暂无该用户！请检查用户名是否正确！");

                }else {
                    if(queriedPassword.equals(pass)){
                        if (reminder.isChecked()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("remember",true);
                            editor.putString("username", a);
                            editor.putString("password", pass);
                            editor.apply();
                        }else {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                        }
                        Intent intent1=new Intent();
                        intent1.setAction("abc");
                        Bundle bundle=new Bundle();
                        bundle.putString("name",a);
                        intent1.putExtras(bundle);
                        sendBroadcast(intent1);

                       Intent intent=new Intent(com.example.electricguitar.login.this, MainActivity.class);
                        intent.putExtra("name",a);
                        startActivity(intent);
                    } else {
                        showAlertDialog("密码错误！请重新输入密码！");
                    }
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(com.example.electricguitar.login.this,com.example.electricguitar.register.class);
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
    protected void initView() {
        // 获取sharedPreferences中remember对于的boolean值，true表示记住密码
        if (sharedPreferences.getBoolean("remember", false)) {
            reminder.setChecked(true);
            username.setText(sharedPreferences.getString("username", ""));
            password.setText(sharedPreferences.getString("password",""));
        }
    }

}
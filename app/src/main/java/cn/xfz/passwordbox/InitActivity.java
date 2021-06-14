package cn.xfz.passwordbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import cn.xfz.passwordbox.R;

public class InitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        final EditText passwordEdit1=findViewById(R.id.editText_password);
        final EditText passwordEdit2=findViewById(R.id.editText_password_again);
        final Button btnApply=findViewById(R.id.button_apply);
        btnApply.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                final String password1=passwordEdit1.getText().toString();
                final String password2=passwordEdit2.getText().toString();
                if(TextUtils.isEmpty(password1)){
                    Toast.makeText(InitActivity.this, "请创建一个密码后继续", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password2)){
                    Toast.makeText(InitActivity.this, "请确认您的密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password1.equals(password2)) {
                    if(password1.length()>=6) {
                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra("password", password1);
                        //设置返回数据
                        InitActivity.this.setResult(0, intent);
                        //关闭Activity
                        InitActivity.this.finish();
                    }else{
                        Toast.makeText(InitActivity.this, "亲，您的密码太短了容易被猜中哦", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(InitActivity.this, "两次输入的密码不一样哦", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

package cn.xfz.passwordbox;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import cn.xfz.passwordbox.cryption.AESUtil;
import cn.xfz.passwordbox.sql.SQLUtil;
import cn.xfz.passwordbox.R;

public class LoginActivity extends AppCompatActivity {
    private SQLUtil conn;
    private String encryptedString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        conn = new SQLUtil(this);
        EditText editText=findViewById(R.id.editText_password_login);
        editText.addTextChangedListener(new TextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length() < 6)
                    return;
                if("PasswordBox".equals(AESUtil.decrypt(encryptedString, s.toString()))){
                    Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("password",s.toString());
                    startActivity(intent);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){ }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){ }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (conn.Check_first()) {
            Intent intent = new Intent(LoginActivity.this, InitActivity.class);
            startActivityForResult(intent, 0);
        } else {
            encryptedString = conn.GetEncryptedString();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0 && resultCode == 0) {
            String password = intent.getStringExtra("password");
            conn.Init(AESUtil.encrypt("PasswordBox", password));
            Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
            intent2.putExtra("password",password);
            startActivity(intent2);
        }
    }
}

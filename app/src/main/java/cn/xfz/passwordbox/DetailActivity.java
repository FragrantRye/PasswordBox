package cn.xfz.passwordbox;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import cn.xfz.passwordbox.cryption.AESUtil;
import cn.xfz.passwordbox.sql.RecodeItem;
import cn.xfz.passwordbox.sql.SQLUtil;
import cn.xfz.passwordbox.R;

public class DetailActivity extends AppCompatActivity {
    private SQLUtil conn;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        conn=new SQLUtil(this);
        password=getIntent().getStringExtra("password");

        final AlertDialog.Builder builder=new AlertDialog.Builder(this).setTitle("该操作不可撤销！确认删除吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        conn.Delete(((RecodeItem) getIntent().getSerializableExtra("item")).getId());
                        DetailActivity.this.finish();
                    }
                })
                .setNegativeButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });

        findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.show();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        final Intent intent=getIntent();
        if ("modify".equals(intent.getStringExtra("action"))) {
            final RecodeItem item=(RecodeItem)intent.getSerializableExtra("item");
            ((EditText)findViewById(R.id.application_detail)).setText(item.getApplication());
            ((EditText)findViewById(R.id.username_detail)).setText(item.getUsername());
            ((EditText)findViewById(R.id.password_detail)).setText(AESUtil.decrypt(item.getPassword(),password));
            ((EditText)findViewById(R.id.extra_info_detail)).setText(item.getExtraInfo());

            findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String application = ((EditText) findViewById(R.id.application_detail)).getText().toString();
                    String username = ((EditText) findViewById(R.id.username_detail)).getText().toString();
                    String password_new = ((EditText) findViewById(R.id.password_detail)).getText().toString();
                    String extra_info = ((EditText) findViewById(R.id.extra_info_detail)).getText().toString();
                    if (TextUtils.isEmpty(application) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
                        Toast.makeText(DetailActivity.this, "请填写完整哦", Toast.LENGTH_SHORT).show();
                    else {
                        conn.Update(application, username, AESUtil.encrypt(password_new, password), extra_info, item.getId());
                        DetailActivity.this.finish();
                    }
                }
            });
        }else if("add".equals(intent.getStringExtra("action"))) {
            findViewById(R.id.button_delete).setVisibility(View.GONE);
            findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String application = ((EditText) findViewById(R.id.application_detail)).getText().toString();
                    String username = ((EditText) findViewById(R.id.username_detail)).getText().toString();
                    String password_new = ((EditText) findViewById(R.id.password_detail)).getText().toString();
                    String extra_info = ((EditText) findViewById(R.id.extra_info_detail)).getText().toString();

                    if (TextUtils.isEmpty(application) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
                        Toast.makeText(DetailActivity.this, "请填写完整哦", Toast.LENGTH_SHORT).show();
                    else {
                        conn.Insert(application, username, AESUtil.encrypt(password_new, password), extra_info);
                        DetailActivity.this.finish();
                    }
                }
            });
        }
    }
}

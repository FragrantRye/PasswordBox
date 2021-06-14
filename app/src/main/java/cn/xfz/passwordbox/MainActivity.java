package cn.xfz.passwordbox;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import cn.xfz.passwordbox.sql.*;
import android.app.Activity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SQLUtil conn;
    private RecodeItem[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setPopupTheme(R.style.popup_theme);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("action","add");
                intent.putExtra("password",getIntent().getStringExtra("password"));
                startActivity(intent);
            }
        });
        if(!TextUtils.isEmpty(getIntent().getStringExtra("password"))){
            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        }
        conn=new SQLUtil(this);

        ((MyListView)findViewById(R.id.item_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("action","modify");
                intent.putExtra("item",items[i]);
                intent.putExtra("password",getIntent().getStringExtra("password"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        items=conn.Search();
        ((MyListView)findViewById(R.id.item_list)).setAdapter(new ListItemAdapter(this,R.layout.item_layout,items));
        if(items.length>0){
            findViewById(R.id.text_none_notice).setVisibility(View.GONE);
            findViewById(R.id.item_list).setVisibility(View.VISIBLE);
        }else{
            findViewById(R.id.text_none_notice).setVisibility(View.VISIBLE);
            findViewById(R.id.item_list).setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_settings:
                Toast.makeText(MainActivity.this,"老公不知道要在里面放啥功能", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_import:
                importDatabase(this);
                break;
            case R.id.action_export:
                exportDatabase(this);
                break;
            case R.id.action_about:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
             Uri uri=data.getData();
             String path = uri.getPath();
             Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
        }
    }

    private void importDatabase(Context c) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/json");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(
                Intent.createChooser(intent, "Choose a file"), 1
        );
    }

    private void exportDatabase(Context c){
        if(items == null) {
            Toast.makeText(MainActivity.this,"没有要导出的数据", Toast.LENGTH_SHORT).show();
            return;
        }

        checkPermission();

        JSONObject obj = new JSONObject();
        try {
            obj.put("version", 1);
            JSONArray arr = new JSONArray();
            for(RecodeItem recode : items){
                JSONObject item_obj = new JSONObject();
                item_obj.put("application", recode.getApplication());
                item_obj.put("username", recode.getUsername());
                item_obj.put("password", recode.getPassword());
                item_obj.put("extra_info", recode.getExtraInfo());
                arr.put(item_obj);
            }
            obj.put("recodes", arr);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        String path = this.getExternalFilesDir("file").getAbsolutePath() + "/Password.json";
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(path);
            fos.write(obj.toString(4).getBytes());
            fos.flush();
            fos.close();

            Intent share = new Intent(Intent.ACTION_SEND);
            Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".FileProvider", file);

            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            share.putExtra(Intent.EXTRA_STREAM, contentUri);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            share.setType("application/json");
            share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(share, "分享文件"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 检查权限
    private void checkPermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        List<String> mPermissionList = new ArrayList<>();
        //判断哪些权限未授予
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
            }
        }
        if (!mPermissionList.isEmpty()) {//请求权限方法
            permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }
    }
}

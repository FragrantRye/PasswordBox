package cn.xfz.passwordbox.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class SQLUtil {
    private SQLiteDatabase db;

    public SQLUtil(Context c) {
        db = SQLiteDatabase.openOrCreateDatabase(c.getDatabasePath("password_box.db"), null);
        if (db != null) {
            db.execSQL("create table if not exists sch(_id INTEGER primary key autoincrement, application TEXT, username TEXT, password TEXT, extra_info TEXT)");
        }
    }

    public boolean Insert(String application, String username, String password, String extra_info) {
        try {
            ContentValues cValue = new ContentValues();
            cValue.put("application", application);
            cValue.put("username", username);
            cValue.put("password", password);
            cValue.put("extra_info", extra_info);
            db.insert("sch", null, cValue);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean Update(String application, String username, String password, String extra_info, int id) {
        try {
            ContentValues values = new ContentValues();
            values.put("application", application);
            values.put("username", username);
            values.put("password", password);
            values.put("extra_info", extra_info);
            String[] whereArgs = {Integer.toString(id)};
            db.update("sch", values, "_id=?", whereArgs);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean Delete(int id) {
        try {
            String whereClause = "_id=?";
            String[] whereArgs = {String.valueOf(id)};
            db.delete("sch", whereClause, whereArgs);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean Check_first(){
        String[] columns={"_id"};
        String[] args={"PasswordBox","PasswordBox","RandomString_yd972hcb7823c=g782tdf"};
        Cursor c = db.query("sch", columns, "application=? and username=? and extra_info=?", args, null, null, "_id");
        c.moveToFirst();
        boolean re = c.isAfterLast();
        c.close();
        return re;
    }
    public void Init(String password){
        Insert("PasswordBox","PasswordBox",password,"RandomString_yd972hcb7823c=g782tdf");
    }
    public String GetEncryptedString() {
        String[] columns = {"password"};
        String[] args = {"PasswordBox", "PasswordBox", "RandomString_yd972hcb7823c=g782tdf"};
        Cursor c = db.query("sch", columns, "application=? and username=? and extra_info=?", args, null, null, "_id");
        c.moveToFirst();
        boolean temp = c.isAfterLast();
        String re = c.isAfterLast() ? "" : c.getString(0);
        c.close();
        return re;
    }

    /*
    public Schedule[] Search(String key) {
        String[] temp = {"%" + key + "%"};
        Cursor c = db.query("sch", columns, "content like ?", temp, null, null, "create_time");
        return getResult(c);
    }

    public Schedule[] Search(Date date) {
        String[] temp = {sdf.format(date) + "%"};
        Cursor c = db.query("sch", columns, "create_time like ?", temp, null, null, "create_time");
        return getResult(c);
    }*/

    public RecodeItem[] Search() {
        String[] columns={"_id", "application", "username", "password", "extra_info"};
        Cursor c = db.query("sch", null, "",null, null, null, "_id");
        return getResult(c);
    }

    private RecodeItem[] getResult(Cursor c) {
        ArrayList<RecodeItem> items = new ArrayList<>();
        c.moveToFirst();
        c.moveToNext();
        while (!c.isAfterLast()) {
            RecodeItem s = new RecodeItem();
            s.setId(c.getInt(0));
            s.setApplication(c.getString(1));
            s.setUsername(c.getString(2));
            s.setPassword(c.getString(3));
            s.setExtraInfo(c.getString(4));
            items.add(s);
            c.moveToNext();
        }
        c.close();
        RecodeItem[] re = new RecodeItem[items.size()];
        items.toArray(re);
        return re;
    }
}

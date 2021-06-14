package cn.xfz.passwordbox.sql;

import java.io.Serializable;

public class RecodeItem implements Serializable {
    private int id = -1;
    private String application;
    private String username;
    private String password;
    private String extra_info;

    public int getId() {
        return id;
    }

    public String getApplication() {
        return application;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getExtraInfo() {
        return extra_info;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setExtraInfo(String extra_info) {
        this.extra_info = extra_info;
    }
}

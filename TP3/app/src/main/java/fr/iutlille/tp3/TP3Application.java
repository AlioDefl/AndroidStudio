package fr.iutlille.tp3;

import android.app.Application;

public class TP3Application extends Application {

    private String login;

    @Override
    public void onCreate() {
        super.onCreate();
        this.login = null;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}

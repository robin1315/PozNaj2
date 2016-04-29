package wieniacy.w.kaloszach.poznaj2;

import android.app.Application;

/**
 * Created by Robin on 28.04.2016.
 */
public class MyAplicationGlobal extends Application {

    public int USERID;
    public String USERNAME;
    public String USEREMAIL;

    public int getGlobalVarValue() {
        return USERID;
    }

    public void setGlobalVarValue(int str) {
        this.USERID = str;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getUSEREMAIL() {
        return USEREMAIL;
    }

    public void setUSEREMAIL(String USEREMAIL) {
        this.USEREMAIL = USEREMAIL;
    }
}
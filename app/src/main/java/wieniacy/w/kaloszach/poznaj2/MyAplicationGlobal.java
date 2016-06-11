package wieniacy.w.kaloszach.poznaj2;

import android.app.Application;

/**
 * Created by Robin on 28.04.2016.
 */
public class MyAplicationGlobal extends Application {


    public  String USERSURNAME;
    public int USERID;
    public String USERNAME;
    public String USEREMAIL;

    public String getUSERPASS() {
        return USERPASS;
    }

    public void setUSERPASS(String USERPASS) {
        this.USERPASS = USERPASS;
    }

    public String USERPASS;

    public String getUSERDESC() {
        return USERDESC;
    }

    public void setUSERDESC(String USERDESC) {
        this.USERDESC = USERDESC;
    }

    public String USERDESC;

    public String getUSERLOGIN() {
        return USERLOGIN;
    }

    public void setUSERLOGIN(String USERLOGIN) {
        this.USERLOGIN = USERLOGIN;
    }

    public String USERLOGIN;

    public String getUSERSURNAME() {
        return USERSURNAME;
    }

    public void setUSERSURNAME(String USERSURNAME) {
        this.USERSURNAME = USERSURNAME;
    }

    public int getUSERID() {
        return USERID;
    }

    public void setUSERID(int USERID) {
        this.USERID = USERID;
    }

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